package com.example.sopautomator.models.common;

import lombok.Data;

import java.util.List;

@Data
public class Step {

    String name;

    String description;

    int level;

    List<String> reviewers;

}
