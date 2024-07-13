package com.example.sopautomator.controller;

import com.example.sopautomator.models.request.UpsertWorkflowRequest;
import com.example.sopautomator.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/workflow")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    @PostMapping("/create")
    public ResponseEntity<Object> createWorkflow(@RequestBody UpsertWorkflowRequest request) {
        return workflowService.createWorkflow(request);
    }

    @GetMapping("/start/{workflow_id}")
    public ResponseEntity<Object> startWorkflow(@PathVariable(name = "workflow_id") long workflowId) {
        return workflowService.startWorkflow(workflowId);
    }

    @PutMapping("/update/{workflow_id}")
    public ResponseEntity<Object> editWorkflow(@PathVariable(name = "workflow_id") long workflowId,
                                               @RequestBody UpsertWorkflowRequest request) {
        return workflowService.editWorkflow(workflowId, request);
    }
}
