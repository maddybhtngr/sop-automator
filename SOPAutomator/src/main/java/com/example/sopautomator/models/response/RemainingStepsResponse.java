package com.example.sopautomator.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemainingStepsResponse {

    @JsonProperty("step_id")
    String stepId;

    String name;

    String level;

    String reviewers;

    String description;

}
