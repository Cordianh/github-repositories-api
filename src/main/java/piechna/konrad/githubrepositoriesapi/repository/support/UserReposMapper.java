package piechna.konrad.githubrepositoriesapi.repository.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;
import piechna.konrad.githubrepositoriesapi.repository.domain.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserReposMapper {

    public List<UserRepository> toUserRepos(String json) throws JsonProcessingException {
        List<UserRepository> userRepositories = new ArrayList<>();
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
