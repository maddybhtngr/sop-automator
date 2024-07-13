package com.example.sopautomator.controller;

import com.example.sopautomator.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("step")
@RequiredArgsConstructor
public class StepController {

    private final StepService stepService;

    @GetMapping("get/{reviewer}")
    public ResponseEntity<Object> getStepsForReviewer(@PathVariable(name = "reviewer") String reviewer) {
        return stepService.getStepsForReviewer(reviewer);
    }

    @GetMapping("complete/{step_id}")
    public ResponseEntity<Object> completeStep(@PathVariable(name = "step_id") long stepId) {
        return stepService.completeStep(stepId);
    }

}
