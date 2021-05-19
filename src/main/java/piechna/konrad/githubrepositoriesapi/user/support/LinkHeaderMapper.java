package piechna.konrad.githubrepositoriesapi.user.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkHeaderMapper {

    private String basePageLink;
    private int lastPageNumber;

    public boolean findNextPageLink(Object linkHeaderResources) {
        if (linkHeaderResources == null) return false;

        String urlRegex = "<([^<>]*?page=)([1-9]*?)>; rel=\"last\"";

        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(linkHeaderResources.toString());

        boolean result;
        if (result = matcher.find()) {
            basePageLink = matcher.group(1);
            lastPageNumber = Integer.parseInt(matcher.group(2));
        }
        return result;
    }

    public String getBasePageLink() {
        return basePageLink;
    }

    public int getLastPageNumber() {
        return lastPageNumber;
    }
}
