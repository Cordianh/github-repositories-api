package piechna.konrad.githubrepositoriesapi.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import piechna.konrad.githubrepositoriesapi.user.domain.UserRepositories;
import piechna.konrad.githubrepositoriesapi.user.support.LinkHeaderMapper;
import piechna.konrad.githubrepositoriesapi.user.support.UserReposMapper;

@Service
public class UserReposService {

    private final static Logger logger = LoggerFactory.getLogger(UserReposService.class);
    private final UserReposMapper userReposMapper;

    @Value("${github-api-users.uri}")
    private String githubApiUsersUri;

    public UserReposService(UserReposMapper userReposMapper) {
        this.userReposMapper = userReposMapper;
    }

    public ResponseEntity<UserRepositories> getRepos(String username) {
        String nextPageLink = githubApiUsersUri + username + "/repos";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(nextPageLink, String.class);

            UserRepositories userRepositories = userReposMapper.toUserRepos(responseEntity.getBody());

            LinkHeaderMapper mapper = new LinkHeaderMapper();
            Object linkHeader = responseEntity.getHeaders().get("Link");
            while (mapper.findNextPageLink(linkHeader)) {
                nextPageLink = mapper.getNextPageLink();

                responseEntity = restTemplate.getForEntity(nextPageLink, String.class);
                userRepositories.addAll(userReposMapper.toUserRepos(responseEntity.getBody()));

                linkHeader = responseEntity.getHeaders().get("Link");
            }

            logger.info(username + "'s repositories successfully parsed");
            return new ResponseEntity<>(userRepositories, HttpStatus.OK);
        } catch (RestClientException e) {
            logger.warn(username + " user cannot be found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
