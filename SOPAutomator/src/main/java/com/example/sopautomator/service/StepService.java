package com.example.sopautomator.service;

import com.example.sopautomator.etl.StepQueue;
import com.example.sopautomator.models.entity.StepEntity;
import com.example.sopautomator.models.response.RemainingStepsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StepService {

    private final StepQueue queue;

    public ResponseEntity<Object> getStepsForReviewer(String reviewer) {
        return ResponseEntity.ok(queue.getStepsForReviewer(reviewer));
    }

    public ResponseEntity<Object> completeStep(long stepId) {
        return queue.completeStep(stepId);
    }

}
