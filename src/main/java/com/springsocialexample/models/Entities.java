
package com.springsocialexample.models;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
public class Entities implements Serializable
{
    private final static long serialVersionUID = 3116497409407450477L;
    private Description description;
}
