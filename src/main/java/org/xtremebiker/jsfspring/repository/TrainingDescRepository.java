package org.xtremebiker.jsfspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xtremebiker.jsfspring.entity.Training;
import org.xtremebiker.jsfspring.entity.TrainingDescription;
import org.xtremebiker.jsfspring.entity.UserProfile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the ClientSystem entity.
 */
//@SuppressWarnings("unused")
@Repository
public interface TrainingDescRepository extends JpaRepository<TrainingDescription, Long>, JpaSpecificationExecutor<TrainingDescription> {

    List<TrainingDescription> findAll();

    Optional<TrainingDescription> findByTrainingDescId(Long trainingDescId);

}
