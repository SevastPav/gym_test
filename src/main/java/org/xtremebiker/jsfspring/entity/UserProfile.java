package org.xtremebiker.jsfspring.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * A ClientSystem.
 */
@Data
@Entity
@Table(name = "user_profile")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGeneratorForCommonInfo")
    @SequenceGenerator(name = "sequenceGeneratorForCommonInfo")
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "fio", nullable = false)
    private String fio;

/*    @ManyToOne
    @JoinColumn(name="role_id", nullable = false)
    private Role roleId;*/

    @Column(name = "roles")
    @ElementCollection(targetClass = Rle.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Rle> roles;

    @Column(name = "login", nullable = false, length = 255)
    private String login;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "entry_date")
    private Date entryDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "client_training",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id"))
    List<Training> clientTrainings;

    @OneToMany(mappedBy="trainerId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Training> trainings;

    public void setRoles(Set<Rle> roles) {
        this.roles = roles;
    }

    public void setFio(String test) {
        this.fio = test;
    }
}
