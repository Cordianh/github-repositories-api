package piechna.konrad.githubrepositoriesapi.user.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;
import piechna.konrad.githubrepositoriesapi.user.domain.UserRepositories;
import piechna.konrad.githubrepositoriesapi.user.domain.UserRepository;

@Component
public class UserReposMapper {

    public UserRepositories toUserRepos(String json) throws JsonProcessingException {
        UserRepositories userRepositories = new UserRepositories();
        ObjectMapper objectMapper = new ObjectMapper();

        ArrayNode arrayNode = objectMapper.readValue(json, ArrayNode.class);
        for (JsonNode node : arrayNode) {
            String name = node.get("name").asText();
            int stars = node.get("stargazers_count").asInt();
            userRepositories.add(new UserRepository(name, stars));
        }

        return userRepositories;
    }
}
