package piechna.konrad.githubrepositoriesapi.repository.domain;

import java.util.ArrayList;

public class UserRepositories extends ArrayList<UserRepository> {

    private long totalStars = 0;

    @Override
    public boolean add(UserRepository userRepository) {
        boolean result = super.add(userRepository);
        if (result) totalStars+= userRepository.getStars();
        return result;
    }

    public long getTotalStars() {
        return totalStars;
    }
}
