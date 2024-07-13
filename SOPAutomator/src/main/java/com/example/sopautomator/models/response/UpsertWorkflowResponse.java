package com.example.sopautomator.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpsertWorkflowResponse {

    @JsonProperty("workflow_id")
    String workflowId;
}
