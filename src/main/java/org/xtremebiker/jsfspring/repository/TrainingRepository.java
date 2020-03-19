package org.xtremebiker.jsfspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xtremebiker.jsfspring.entity.Training;
import org.xtremebiker.jsfspring.entity.UserProfile;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the ClientSystem entity.
 */
//@SuppressWarnings("unused")
@Repository
public interface TrainingRepository extends JpaRepository<Training, Long>, JpaSpecificationExecutor<Training> {

    List<Training> findAll();

    List<Training> findAllByDateAfterAndDateBeforeAndActive(LocalDate date, LocalDate date2, Boolean active);

    List<Training> findAllByDateAfterAndDateBeforeAndTrainerId(LocalDate date, LocalDate date2, UserProfile trainer);

    List<Training> findAllByDateAfterAndDateBefore(LocalDate date, LocalDate date2);

    List<Training> findAllByTrainerId(UserProfile trainer);

    Optional<Training> findByTrainingId(Long trainingId);
}
