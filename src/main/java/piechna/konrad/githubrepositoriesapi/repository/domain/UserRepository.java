package piechna.konrad.githubrepositoriesapi.repository.domain;

public class UserRepository {

    private String name;
    private int stars;

    public UserRepository(String name, int stars) {
        this.name = name;
        this.stars = stars;
    }

    public String getName() {
        return name;
    }

    public int getStars() {
        return stars;
    }
}
