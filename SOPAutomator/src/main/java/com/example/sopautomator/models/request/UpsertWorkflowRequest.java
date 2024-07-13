package com.example.sopautomator.models.request;

import com.example.sopautomator.models.common.Step;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpsertWorkflowRequest {

    String name;

    List<Step> steps;

}
