package org.xtremebiker.jsfspring.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * A ClientSystem.
 */
@Data
@Entity
@Table(name = "training")
public class Training implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGeneratorForTraining")
    @SequenceGenerator(name = "sequenceGeneratorForTraining")
    @Column(name = "training_id", nullable = false, unique = true)
    private Long trainingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="trainer_id")
    private UserProfile trainerId;

/*    @Column(name = "datetime")
    private Date dateTime;*/

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="training_desc_id")
    private TrainingDescription trainingDescription;

/*    @Column(name = "title", length = 255)
    private String title;*/

/*    @Column(name = "description", length = 255)
    private String description;*/

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @ManyToMany(mappedBy = "clientTrainings", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<UserProfile> clients;

}
