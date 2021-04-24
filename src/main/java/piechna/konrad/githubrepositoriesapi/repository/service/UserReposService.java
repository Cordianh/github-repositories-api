package piechna.konrad.githubrepositoriesapi.repository.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import piechna.konrad.githubrepositoriesapi.repository.domain.UserRepository;
import piechna.konrad.githubrepositoriesapi.repository.support.UserReposMapper;

import java.util.List;

@Service
public class UserReposService {

    private final static Logger logger = LoggerFactory.getLogger(UserReposService.class);
    private final UserReposMapper userReposMapper;

    @Value("${github-api-users.uri}")
    private String githubApiUsersUri;

    public UserReposService(UserReposMapper userReposMapper) {
        this.userReposMapper = userReposMapper;
    }

    public ResponseEntity<List<UserRepository>> getRepos(String username) {
        String url = githubApiUsersUri + username + "/repos";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            List<UserRepository> userRepositories = userReposMapper.toUserRepos(responseEntity.getBody());
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
}
