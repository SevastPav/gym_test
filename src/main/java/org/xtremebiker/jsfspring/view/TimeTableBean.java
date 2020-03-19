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
import org.xtremebiker.jsfspring.services.WeekService;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
@SessionScope
@Getter
public class TimeTableBean {

	Table<LocalDate, LocalTime, Training> table = HashBasedTable.create();

	List<LocalTime> times = new ArrayList<>();

	Training training;

	private final TrainingRepository trainingRepository;

	private final UserProfileRepository userProfileRepository;

	private final WeekService weekService;

	public TimeTableBean(TrainingRepository trainingRepository, UserProfileRepository userProfileRepository, WeekService weekService) {
		this.trainingRepository = trainingRepository;
		this.userProfileRepository = userProfileRepository;
		this.weekService = weekService;
	}

	public List<LocalTime> getAllTimes(){
		weekService.setCurrentWeek();
		List<Training> trainingsList = trainingRepository
				.findAllByDateAfterAndDateBeforeAndActive(weekService.getMonday().minusDays(1), weekService.getSunday().plusDays(1), true);
		for (Training training:trainingsList) {
			if(!table.contains(training.getDate(), training.getTime())){
				table.put(training.getDate(), training.getTime(), training);
				if (!times.contains(training.getTime()))
					times.add(training.getTime());
			}
		}
		Collections.sort(times);
		return times;
	}

	public String getTrainingTitleByDateTime(LocalDate date, LocalTime time){
		Training training = table.get(date, time);
		if(training != null)
			return training.getTrainingDescription().getTitle();
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

}
