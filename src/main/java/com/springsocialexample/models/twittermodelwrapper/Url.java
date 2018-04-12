
package com.springsocialexample.models.twittermodelwrapper;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "url",
    "expanded_url",
    "display_url",
    "indices"
})
public class Url {

    @JsonProperty("url")
    public String url;
    @JsonProperty("expanded_url")
    public String expandedUrl;
    @JsonProperty("display_url")
    public String displayUrl;
    @JsonProperty("indices")
    public List<Long> indices = null;

}
