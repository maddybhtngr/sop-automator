package com.example.sopautomator.models.common;

import lombok.Data;

import java.util.List;

@Data
public class Workflow {

    String name;

    List<Step> steps;

}
