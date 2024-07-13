package com.example.sopautomator.service;

import com.example.sopautomator.etl.StepQueue;
import com.example.sopautomator.models.common.Step;
import com.example.sopautomator.models.entity.StepEntity;
import com.example.sopautomator.models.entity.WorkflowEntity;
import com.example.sopautomator.models.request.UpsertWorkflowRequest;
import com.example.sopautomator.repo.StepsRepository;
import com.example.sopautomator.repo.WorkflowRepository;
import com.example.sopautomator.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final StepsRepository stepsRepository;
    private final ObjectMapperUtils mapperUtils;
    private final StepQueue stepQueue;

    private static final Logger logger = LoggerFactory.getLogger(WorkflowService.class);

    public ResponseEntity<Object> startWorkflow(long workflowId) {
        return stepQueue.loadQueueForWorkflow(workflowId);
    }

    public ResponseEntity<Object> createWorkflow(UpsertWorkflowRequest request) {
        return handleWorkflowOperation(request, null);
    }

    public ResponseEntity<Object> editWorkflow(long workflowId, UpsertWorkflowRequest request) {
        return handleWorkflowOperation(request, workflowId);
    }

    private ResponseEntity<Object> handleWorkflowOperation(UpsertWorkflowRequest request, Long workflowId) {
        try {
            Step startingStep = validateAndGetStartingStep(request.getSteps());
            if (startingStep == null) {
                return ResponseEntity.badRequest().body("issue with starting step found");
            }

            LinkedHashMap<Integer, List<Step>> stepMapping = createStepMapping(request.getSteps());
            if (!validateStepLevels(stepMapping)) {
                return ResponseEntity.badRequest().body("Levels are not in proper ordering");
            }

            List<StepEntity> stepEntities = createStepEntities(stepMapping);
            WorkflowEntity workflowEntity = saveWorkflowEntity(workflowId, request.getName(), stepEntities);

            return ResponseEntity.ok().body("Workflow request completed successfully with id: " + workflowEntity.getId());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    private Step validateAndGetStartingStep(List<Step> steps) {
        // find all steps that have level 0
        List<Step> startingSteps = steps.stream().filter(step -> step.getLevel() == 0).toList();
        if (startingSteps.size() != 1) {
            logger.error("Starting step not found or multiple starting steps found");
            return null;
        }
        return startingSteps.get(0);
    }

    private LinkedHashMap<Integer, List<Step>> createStepMapping(List<Step> steps) {
        LinkedHashMap<Integer, List<Step>> stepMapping = new LinkedHashMap<>();
        steps.forEach(step -> stepMapping.computeIfAbsent(step.getLevel(), k -> new ArrayList<>()).add(step));
        return stepMapping;
    }

    private boolean validateStepLevels(LinkedHashMap<Integer, List<Step>> stepMapping) {
        int maxStepsInMap = stepMapping.keySet().stream().max(Integer::compare).orElse(0);
        for (int level = 0; level < maxStepsInMap; level++) {
            if (!stepMapping.containsKey(level)) {
                return false;
            }
        }
        return true;
    }

    private List<StepEntity> createStepEntities(LinkedHashMap<Integer, List<Step>> stepMapping) {

        HashMap<Integer, List<StepEntity>> levelToStepMap = new HashMap<>();

        for (int level = stepMapping.size() -1; level >= 0; level--) {
            List<Step> steps = stepMapping.get(level);
            List<StepEntity> stepEntities = new ArrayList<>();

            for (Step step : steps) {
                StepEntity stepEntity = StepEntity.builder()
                        .name(step.getName())
                        .level(step.getLevel())
                        .description(step.getDescription())
                        .reviewers(mapperUtils.convertObjectToJson(step.getReviewers()))
                        .build();
                stepEntities.add(stepEntity);
            }

            levelToStepMap.put(level, stepEntities);
        }

        List<StepEntity> stepEntities = new ArrayList<>();
        for (int level = 0; level < levelToStepMap.size(); level++) {
            List<StepEntity> currentLevelSteps = levelToStepMap.get(level);
            List<StepEntity> nextLevelSteps = levelToStepMap.getOrDefault(level + 1, new ArrayList<>());

            for (StepEntity currentStep : currentLevelSteps) {
                currentStep.setNextSteps(nextLevelSteps);
            }
            stepEntities.addAll(currentLevelSteps);
        }
        return stepEntities;
    }

    private WorkflowEntity saveWorkflowEntity(Long workflowId, String workflowName, List<StepEntity> stepEntities) {
        WorkflowEntity workflowEntity = Optional.ofNullable(workflowId)
                .flatMap(workflowRepository::findById)
                .orElse(new WorkflowEntity());

        workflowEntity.setName(workflowName);
        workflowEntity.setStartStep(stepEntities.get(0));

        stepsRepository.saveAll(stepEntities);
        return workflowRepository.save(workflowEntity);
    }
}