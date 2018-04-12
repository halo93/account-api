
package com.springsocialexample.models.twittermodelwrapper;

import lombok.Data;

import java.io.Serializable;

@Data
public class Entities implements Serializable
{
    private final static long serialVersionUID = 3116497409407450477L;
    private Description description;
}
