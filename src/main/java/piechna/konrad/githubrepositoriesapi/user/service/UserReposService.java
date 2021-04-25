package piechna.konrad.githubrepositoriesapi.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import piechna.konrad.githubrepositoriesapi.user.domain.UserRepositories;
import piechna.konrad.githubrepositoriesapi.user.support.LinkHeaderMapper;
import piechna.konrad.githubrepositoriesapi.user.support.UserReposMapper;

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

    public ResponseEntity<UserRepositories> getRepos(String username) {
        String nextPageLink = githubApiUsersUri + username + "/repos";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        if (!githubToken.isEmpty()) headers.add("Authorization", "token " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(nextPageLink, HttpMethod.GET, entity, String.class);

            UserRepositories userRepositories = userReposMapper.toUserRepos(responseEntity.getBody());

            LinkHeaderMapper mapper = new LinkHeaderMapper();
            Object linkHeader = responseEntity.getHeaders().get("Link");
            while (mapper.findNextPageLink(linkHeader)) {
                nextPageLink = mapper.getNextPageLink();

                responseEntity = restTemplate.exchange(nextPageLink, HttpMethod.GET, entity, String.class);
                userRepositories.addAll(userReposMapper.toUserRepos(responseEntity.getBody()));

                linkHeader = responseEntity.getHeaders().get("Link");
            }

            logger.info(username + "'s repositories successfully parsed");
            return new ResponseEntity<>(userRepositories, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            logger.warn(e.getMessage());
            return new ResponseEntity<>(e.getStatusCode());
        } catch (JsonProcessingException e) {
            logger.warn(username + "'s repositories hasn't been parsed");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<Long> getStars(String username) {
        ResponseEntity<UserRepositories> responseEntity = getRepos(username);
        UserRepositories userRepositories = responseEntity.getBody();

        if (userRepositories != null) return new ResponseEntity<>(userRepositories.getTotalStars(), HttpStatus.OK);
        return new ResponseEntity<>(responseEntity.getStatusCode());
    }
}
