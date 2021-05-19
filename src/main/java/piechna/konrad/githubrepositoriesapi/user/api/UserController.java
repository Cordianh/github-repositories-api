package piechna.konrad.githubrepositoriesapi.user.api;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import piechna.konrad.githubrepositoriesapi.user.domain.UserRepository;
import piechna.konrad.githubrepositoriesapi.user.service.UserReposService;

import org.slf4j.Logger;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserReposService userReposService;

    public UserController(UserReposService userReposService) {
        this.userReposService = userReposService;
    }

    @GetMapping("/{username}/repos")
    public ResponseEntity<List<UserRepository>> getUserRepositories(@PathVariable("username") String username) {
        logger.info("Got request: api/users/" + username + "/repos");
        ResponseEntity<List<UserRepository>> responseEntity = userReposService.getRepos(username);
        logger.info("Sent response for api/users/" + username + "/repos request, status code: " +
                responseEntity.getStatusCode());
        return responseEntity;
    }

    @GetMapping("/{username}/repos/stars")
    public ResponseEntity<Long> getUserStars(@PathVariable("username") String username) {
        logger.info("Got request: api/users/" + username + "/repos/stars");
        ResponseEntity<Long> responseEntity = userReposService.getStars(username);
        logger.info("Sent response for api/users/" + username + "/repos/stars request, status code: " +
                responseEntity.getStatusCode());
        return responseEntity;
    }
}
