
package com.springsocialexample.models.twittermodelwrapper;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "hashtags",
    "symbols",
    "user_mentions",
    "urls"
})
public class Entities_ {

    @JsonProperty("hashtags")
    public List<Object> hashtags = null;
    @JsonProperty("symbols")
    public List<Object> symbols = null;
    @JsonProperty("user_mentions")
    public List<UserMention> userMentions = null;
    @JsonProperty("urls")
    public List<Url> urls = null;

}
