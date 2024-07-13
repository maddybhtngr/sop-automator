package com.example.sopautomator.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "step_entity")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column
    String name;

    @Column
    String description;

    @Column
    int level;

    @Column
    String reviewers;

    @OneToMany
    @JoinColumn(name = "next_steps")
    List<StepEntity> nextSteps;

}
