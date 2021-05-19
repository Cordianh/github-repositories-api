package piechna.konrad.githubrepositoriesapi.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import piechna.konrad.githubrepositoriesapi.user.domain.UserRepository;
import piechna.konrad.githubrepositoriesapi.user.support.LinkHeaderMapper;
import piechna.konrad.githubrepositoriesapi.user.support.UserReposMapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class UserReposService {

    private final static Logger logger = LoggerFactory.getLogger(UserReposService.class);
    private final UserReposMapper userReposMapper;

    @Value("${github-auth.token}")
    private String githubToken;

    @Value("${github-api-users.uri}")
    private String githubApiUsersUri;

    public UserReposService(UserReposMapper userReposMapper) {
        this.userReposMapper = userReposMapper;
    }

    public ResponseEntity<List<UserRepository>> getRepos(String username) {
        String requestLink = githubApiUsersUri + username + "/repos";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        if (!githubToken.isEmpty()) headers.add("Authorization", "token " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(requestLink, HttpMethod.GET, entity, String.class);

            List<UserRepository> userRepositories;
            LinkHeaderMapper mapper = new LinkHeaderMapper();

            if (mapper.findNextPageLink(responseEntity.getHeaders().get("Link")))
                userRepositories = getUserReposFromPages(mapper.getBasePageLink(), mapper.getLastPageNumber());
            else
                userRepositories = userReposMapper.toUserRepos(responseEntity.getBody());

            logger.info(username + "'s repositories successfully parsed");
            return new ResponseEntity<>(userRepositories, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.warn(e.getMessage());
            return new ResponseEntity<>(e.getStatusCode());
        } catch (JsonProcessingException e) {
            logger.warn(username + "'s repositories hasn't been parsed");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (InterruptedException | ExecutionException e) {
            logger.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Long> getStars(String username) {
        ResponseEntity<List<UserRepository>> responseEntity = getRepos(username);
        List<UserRepository> userRepositories = responseEntity.getBody();

        if (userRepositories == null) return new ResponseEntity<>(responseEntity.getStatusCode());

        long totalStars = 0;
        for (UserRepository repository : userRepositories) {
            totalStars += repository.getStars();
        }

        return new ResponseEntity<>(totalStars, HttpStatus.OK);
    }

    private List<UserRepository> getUserReposFromPages(String basePageUrl, int lastPageNumber)
            throws RestClientException, JsonProcessingException, InterruptedException, ExecutionException {

        List<Callable<List<UserRepository>>> callables = new ArrayList<>();

        for (int i = 0; i < lastPageNumber; i++) {
            int currentPageNumber = i + 1;
            callables.add(() -> {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                if (!githubToken.isEmpty()) headers.add("Authorization", "token " + githubToken);
                HttpEntity<String> entity = new HttpEntity<>("", headers);

                ResponseEntity<String> responseEntity =
                        restTemplate.exchange(basePageUrl + currentPageNumber, HttpMethod.GET, entity, String.class);

                return userReposMapper.toUserRepos(responseEntity.getBody());
            });
        }

        ExecutorService executorService = Executors.newFixedThreadPool(lastPageNumber);
        List<Future<List<UserRepository>>> futureList = executorService.invokeAll(callables);

        List<UserRepository> userRepositories = new LinkedList<>();

        for (Future<List<UserRepository>> future : futureList) {
            userRepositories.addAll(future.get());
        }

        return userRepositories;
    }
}
