package org.xtremebiker.jsfspring.view;

/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/

import lombok.Data;
import org.primefaces.PrimeFaces;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.xtremebiker.jsfspring.entity.Rle;
import org.xtremebiker.jsfspring.entity.Training;
import org.xtremebiker.jsfspring.entity.UserProfile;
import org.xtremebiker.jsfspring.repository.TrainingRepository;
import org.xtremebiker.jsfspring.repository.UserProfileRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
@SessionScope
@Data
public class TrainerStatisticBean {

    private Long trainerId;

    public Date startDate;

    public Date endDate;

    List<Training> topTrainings = new ArrayList<>();

    List<Training> trainings = new ArrayList<>();

    private final UserProfileRepository userProfileRepository;

    private final TrainingRepository trainingRepository;

    public TrainerStatisticBean(UserProfileRepository userProfileRepository, TrainingRepository trainingRepository) {
        this.userProfileRepository = userProfileRepository;
        this.trainingRepository = trainingRepository;
    }

    public Map<String, Long> getTrainers() {
        List<UserProfile> trainers = userProfileRepository.findAllByRoles(Collections.singleton(Rle.TRAINER));
        return trainers.stream().collect(Collectors.toMap(UserProfile::getLogin, UserProfile::getUserId));
    }

    public void error(String details) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!", details));
    }

    public void createTop(List<Training> trainings){
        trainings.sort((l, r) -> r.getClients().size() - l.getClients().size());
        if (trainings.size() >= 3){
            for (int i = 0; i < 3; i++)
                topTrainings.add(trainings.get(i));
        }
    }

    public void showTrainings() {
        try {
            topTrainings = new ArrayList<>();
            Optional<UserProfile> trainer = userProfileRepository.findByUserId(trainerId);
            if (trainer.isPresent()) {
                trainings = trainingRepository.findAllByDateAfterAndDateBeforeAndTrainerId(
                        startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1),
                        endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1),
                        trainer.get());
            } else {
                trainings = trainingRepository.findAllByDateAfterAndDateBefore(
                        startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusDays(1),
                        endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1));
            }
            createTop(trainings);
        } catch (Exception ex) {
    		error("Некорректные данные");
        }
    }

}
