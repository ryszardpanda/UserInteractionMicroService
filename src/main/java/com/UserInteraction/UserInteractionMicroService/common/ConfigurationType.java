package com.UserInteraction.UserInteractionMicroService.common;

import com.UserInteraction.UserInteractionMicroService.client.exceptions.WrongTypeException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public enum ConfigurationType {
    RAM("Ram"),
    PROCESSOR("Processor"),
    ACCESSORY("Accessory");

    private String name;

    ConfigurationType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static ConfigurationType from(String name){
        return Arrays.stream(ConfigurationType.values())
                .filter(configurationType -> configurationType.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new WrongTypeException("Unknow enum value for Configuration Type : " + name, HttpStatus.BAD_REQUEST));
    }
}
