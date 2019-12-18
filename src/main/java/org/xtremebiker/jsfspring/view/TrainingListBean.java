package org.xtremebiker.jsfspring.view;

/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.xtremebiker.jsfspring.entity.Rle;
import org.xtremebiker.jsfspring.entity.Training;
import org.xtremebiker.jsfspring.entity.UserProfile;
import org.xtremebiker.jsfspring.repository.TrainingRepository;
import org.xtremebiker.jsfspring.repository.UserProfileRepository;
import org.xtremebiker.jsfspring.services.WeekService;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@SessionScope
@Getter
public class TrainingListBean {

	List<Training> trainings = new ArrayList<>();

	List<LocalTime> times = new ArrayList<>();

	private final TrainingRepository trainingRepository;

	private final UserProfileRepository userProfileRepository;

	private final WeekService weekService;


	public TrainingListBean(TrainingRepository trainingRepository, UserProfileRepository userProfileRepository, WeekService weekService) {
		this.userProfileRepository = userProfileRepository;
		this.trainingRepository = trainingRepository;
		this.weekService = weekService;
	}

	public List<Training> getAllTrainings(){
		weekService.setCurrentWeek();
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();
		Optional<UserProfile> trainer = userProfileRepository.findByLogin(authentication.getName());
		if(trainer.isPresent()){
			UserProfile user = trainer.get();
			if (user.getRoles().contains(Rle.TRAINER))
				return user.getTrainings();
			if (user.getRoles().contains(Rle.USER))
				return user.getClientTrainings();
		}
/*		return trainingRepository
				.findAllByDateAfterAndDateBeforeAndTrainerId(weekService.getMonday().minusDays(1), weekService.getSunday().plusDays(1), trainer.get());*/
		return new ArrayList<>();
	}

	@PostConstruct
	public void init() {

	}

}
