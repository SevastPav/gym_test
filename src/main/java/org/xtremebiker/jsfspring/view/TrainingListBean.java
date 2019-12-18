package org.xtremebiker.jsfspring.view;

/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.xtremebiker.jsfspring.entity.Training;
import org.xtremebiker.jsfspring.entity.UserProfile;
import org.xtremebiker.jsfspring.repository.TrainingRepository;
import org.xtremebiker.jsfspring.repository.UserProfileRepository;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@SessionScope
@Getter
public class TrainingListBean {

	List<Training> trainings = new ArrayList<>();

	private LocalDate today = LocalDate.now();
	private LocalDate monday;
	private LocalDate tuesday;
	private LocalDate wednesday;
	private LocalDate thursday;
	private LocalDate friday;
	private LocalDate saturday;
	private LocalDate sunday;

	List<LocalTime> times = new ArrayList<>();

	private final TrainingRepository trainingRepository;

	private final UserProfileRepository userProfileRepository;

	private void setCurrentWeek(){
		monday = today;
		while (monday.getDayOfWeek() != DayOfWeek.MONDAY)
		{
			monday = monday.minusDays(1);
		}
		tuesday = monday.plusDays(1);
		wednesday = tuesday.plusDays(1);
		thursday = wednesday.plusDays(1);
		friday = thursday.plusDays(1);
		saturday = friday.plusDays(1);
		sunday = saturday.plusDays(1);
	}

	public TrainingListBean(TrainingRepository trainingRepository, UserProfileRepository userProfileRepository) {
		this.userProfileRepository = userProfileRepository;
		this.trainingRepository = trainingRepository;
/*		trainer.ifPresent(userProfile -> trainings = trainingRepository
				.findAllByDateAfterAndDateBeforeAndTrainerId(monday, sunday, userProfile));*/
	}

	public List<Training> getAllTrainings(){
		setCurrentWeek();
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();
		Optional<UserProfile> trainer = userProfileRepository.findByLogin(authentication.getName());
		trainings = trainingRepository
				.findAllByDateAfterAndDateBefore(monday.minusDays(1), sunday.plusDays(1));
		return trainings;
	}

	@PostConstruct
	public void init() {

	}

}
