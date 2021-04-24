package piechna.konrad.githubrepositoriesapi.repository.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkHeaderMapper {

    public String getNextPageLink(String link) {
        String urlRegex = "<([^<>]*?)>; rel=\"next\"";

        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(link);

        if (matcher.find()) return matcher.group(1);
        return null;
    }
}
