package com.example.sopautomator.etl;

import com.example.sopautomator.models.entity.StepEntity;
import com.example.sopautomator.models.response.RemainingStepsResponse;
import com.example.sopautomator.repo.StepsRepository;
import com.example.sopautomator.repo.WorkflowRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StepQueue {

    private final StepsRepository stepsRepository;
    private List<StepEntity> loadedSteps;

    private final WorkflowRepository workflowRepository;

    @PostConstruct
    public void init() {
        loadedSteps = new ArrayList<>();
    }

    public ResponseEntity<Object> loadQueueForWorkflow(long workflowId) {
        if (workflowRepository.findById(workflowId).isPresent()) {
            StepEntity startingStep = workflowRepository.findById(workflowId).get().getStartStep();

            loadedSteps.add(startingStep);
            return ResponseEntity.ok().body("Workload is started successfully");
        }
        return ResponseEntity.badRequest().body("Workload not found");
    }

    public List<RemainingStepsResponse> getStepsForReviewer(String reviewer) {
        List<RemainingStepsResponse> responses = new ArrayList<>();
        for (StepEntity step : loadedSteps) {
            if (step.getReviewers().contains(reviewer)) {
                responses.add(RemainingStepsResponse.builder()
                                .stepId(String.valueOf(step.getId()))
                                .name(step.getName())
                                .reviewers(step.getReviewers())
                                .level(String.valueOf(step.getLevel()))
                                .description(step.getDescription())
                        .build());
            }
        }

        return responses;
    }

    public ResponseEntity<Object> completeStep(long id) {

        StepEntity completedStep = null;
        for (StepEntity step : loadedSteps) {
            if (step.getId() == id) {
                completedStep = step;
            }
        }

        // if step existed and just got completed, find next steps
        if (completedStep != null) {
            loadedSteps.remove(completedStep);
            List<StepEntity> nextSteps = stepsRepository.findById(completedStep.getId()).get().getNextSteps();
            loadedSteps.addAll(nextSteps);
            return ResponseEntity.ok().body("Step completed");
        } else {
            return ResponseEntity.badRequest().body("Step does not exist");
        }
    }

}
