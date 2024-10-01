package org.cosmic.backend.domainsTest;

import java.util.Map;

public class UrlGenerator {
    public String buildUrl(String urlTemplate, Map<String, Object> params) {
        String finalUrl = urlTemplate;

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            finalUrl = finalUrl.replace(placeholder, entry.getValue().toString());
        }

        return finalUrl;
    }

}