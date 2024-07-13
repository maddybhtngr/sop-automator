package com.example.sopautomator.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workflows")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column
    String name;

    @OneToOne
    @JoinColumn(name = "start_step")
    StepEntity startStep;

}
