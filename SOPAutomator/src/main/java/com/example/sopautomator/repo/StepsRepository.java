package com.example.sopautomator.repo;

import com.example.sopautomator.models.entity.StepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepsRepository extends JpaRepository<StepEntity, Long> {
}
