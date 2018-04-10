package com.springsocialexample.utility;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.social.facebook.api.ImageType;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class ServiceUtil {
    public static String fetchPictureUrl(String userId, ImageType imageType) {
        URI uri = URIBuilder.fromUri(SnsConstant.FACEBOOK_GRAPH_API_URL + userId + "/picture" +
            "?type=" + imageType.toString().toLowerCase() + "&redirect=false").build();
        JsonNode response = new RestTemplate().getForObject(uri, JsonNode.class);
        return response.get("data").get("url").asText();
    }
}
