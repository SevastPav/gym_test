package org.xtremebiker.jsfspring.view;

/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.xtremebiker.jsfspring.entity.Training;
import org.xtremebiker.jsfspring.entity.UserProfile;
import org.xtremebiker.jsfspring.repository.TrainingRepository;
import org.xtremebiker.jsfspring.repository.UserProfileRepository;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
@SessionScope
@Getter
public class TimeTableBean {

	Table<LocalDate, LocalTime, Training> table = HashBasedTable.create();

	private LocalDate today = LocalDate.now();
	private LocalDate monday;
	private LocalDate tuesday;
	private LocalDate wednesday;
	private LocalDate thursday;
	private LocalDate friday;
	private LocalDate saturday;
	private LocalDate sunday;

	List<LocalTime> times = new ArrayList<>();

	Training training;

	private final TrainingRepository trainingRepository;

	@Autowired
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

	public TimeTableBean(TrainingRepository trainingRepository, UserProfileRepository userProfileRepository) {
		this.trainingRepository = trainingRepository;
		this.userProfileRepository = userProfileRepository;
		setCurrentWeek();
		List<Training> trainingsList = trainingRepository
				.findAllByDateAfterAndDateBefore(monday.minusDays(1), sunday.plusDays(1));
		for (Training training:trainingsList) {
			table.put(training.getDate(), training.getTime(), training);
			times.add(training.getTime());
		}
		Collections.sort(times);
	}

	public String getTrainingTitleByDateTime(LocalDate date, LocalTime time){
		Training training = table.get(date, time);
		if(training != null)
			return training.getTitle();
		return "";
	}

	public Training getTrainingByDateTime(LocalDate date, LocalTime time){
		return table.get(date, time);
	}

	public void attrListener(ActionEvent event){
		training = (Training)event.getComponent().getAttributes().get("training");
	}

	public void recordToTraining(){
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();
		Optional<UserProfile> client = userProfileRepository.findByLogin(authentication.getName());
		if (client.isPresent()){
			UserProfile clientProfile = client.get();
			clientProfile.getClientTrainings().add(training);
			userProfileRepository.save(clientProfile);
		}
	}

	@PostConstruct
	public void init() {

	}

}
