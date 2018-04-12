
package com.springsocialexample.models.twittermodelwrapper;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
public class Description implements Serializable
{
    private List<Object> urls = null;
    private final static long serialVersionUID = 7485849078702454331L;
}
