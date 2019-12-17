package org.xtremebiker.jsfspring.view;

/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.xtremebiker.jsfspring.entity.Training;
import org.xtremebiker.jsfspring.repository.TrainingRepository;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
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

	List<LocalTime> times = new ArrayList<>();;

	private final TrainingRepository trainingRepository;

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

	public TimeTableBean(TrainingRepository trainingRepository) {
		this.trainingRepository = trainingRepository;
		setCurrentWeek();
		List<Training> trainingsList = trainingRepository
				.findAllByDateAfterAndDateBefore(monday, sunday);
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

	@PostConstruct
	public void init() {

	}

}
