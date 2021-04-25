package piechna.konrad.githubrepositoriesapi.repository.api;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import piechna.konrad.githubrepositoriesapi.repository.domain.UserRepositories;
import piechna.konrad.githubrepositoriesapi.repository.service.UserReposService;

import org.slf4j.Logger;

@RestController
@RequestMapping("/api/repositories")
public class UserRepositoryApi {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryApi.class);
    private final UserReposService userReposService;

    public UserRepositoryApi(UserReposService userReposService) {
        this.userReposService = userReposService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserRepositories> getUserRepository(@PathVariable("username") String username) {
        logger.info("Got request: api/repositories/" + username);
        ResponseEntity<UserRepositories> responseEntity = userReposService.getRepos(username);
        logger.info("Sent response for api/repositories/" + username + " request, status code: " +
                responseEntity.getStatusCode());
        return responseEntity;
    }

    @GetMapping("/{username}/stars")
    public ResponseEntity<Long> getUserStars(@PathVariable("username") String username) {
        logger.info("Got request: api/repositories/" + username + "/stars");
        ResponseEntity<Long> responseEntity = userReposService.getStars(username);
        logger.info("Sent response for api/repositories/" + username + "/repos request, status code: " +
                responseEntity.getStatusCode());
        return responseEntity;
    }
}
