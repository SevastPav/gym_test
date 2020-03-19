package org.xtremebiker.jsfspring.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * A ClientSystem.
 */
@Data
@Entity
@Table(name = "training_desc")
public class TrainingDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGeneratorForTrainingDesc")
    @SequenceGenerator(name = "sequenceGeneratorForTrainingDesc")
    @Column(name = "training_desc_id", nullable = false, unique = true)
    private Long trainingDescId;

    @OneToMany(mappedBy="trainingDescription", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Training> trainings;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

}
