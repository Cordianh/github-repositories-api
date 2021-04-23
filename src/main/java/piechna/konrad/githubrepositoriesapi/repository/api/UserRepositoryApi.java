package piechna.konrad.githubrepositoriesapi.repository.api;

import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import piechna.konrad.githubrepositoriesapi.repository.domain.UserRepository;
import piechna.konrad.githubrepositoriesapi.repository.service.UserReposService;

import java.util.List;

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
    public ResponseEntity<List<UserRepository>> getUserRepository(@PathVariable("username") String username) {
        logger.info("Got request: api/repositories/" + username);
        ResponseEntity<List<UserRepository>> responseEntity = userReposService.getRepos(username);
        logger.info("Sent response for api/repositories/" + username + " request, status code: " +
                responseEntity.getStatusCode());

        return responseEntity;
    }
}
