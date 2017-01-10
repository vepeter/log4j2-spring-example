package com.vepeter.example.log4j2spring.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HelloResponse {

    private final String name;

    @JsonCreator
    public HelloResponse(@JsonProperty("name") String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
