package piechna.konrad.githubrepositoriesapi.user.domain;

import java.util.ArrayList;
import java.util.Collection;

public class UserRepositories extends ArrayList<UserRepository> {

    private long totalStars = 0;

    @Override
    public boolean add(UserRepository userRepository) {
        boolean result = super.add(userRepository);
        if (result) totalStars += userRepository.getStars();
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends UserRepository> c) {
        boolean result = super.addAll(c);
        if (result) totalStars += ((UserRepositories) c).getTotalStars();
        return result;
    }

    public long getTotalStars() {
        return totalStars;
    }
}
