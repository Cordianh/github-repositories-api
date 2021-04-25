package piechna.konrad.githubrepositoriesapi.user.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkHeaderMapper {

    private String nextPageLink;

    public boolean findNextPageLink(Object linkHeaderResources) {
        if (linkHeaderResources == null) return false;

        String urlRegex = "<([^<>]*?)>; rel=\"next\"";

        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(linkHeaderResources.toString());

        boolean result;
        if (result = matcher.find()) nextPageLink = matcher.group(1);
        return result;
    }

    public String getNextPageLink() {
        return nextPageLink;
    }
}
