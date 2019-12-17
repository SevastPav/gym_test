package org.xtremebiker.jsfspring.view;

/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.xtremebiker.jsfspring.entity.Training;
import org.xtremebiker.jsfspring.repository.TrainingRepository;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.Date;

@Component
@SessionScope
@Data
public class TrainingBean {

	private Training trainingDto;

	public Date date;

	public Date time;

	private final TrainingRepository trainingRepository;

	public TrainingBean(TrainingRepository trainingRepository) {
		this.trainingRepository = trainingRepository;
	}

	@PostConstruct
	public void init() {
		trainingDto = new Training();
		date = new Date();
	}

	public void save() {
		int i = 1;
		trainingDto.setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		trainingDto.setTime(time.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
		trainingRepository.save(trainingDto);
	}

	public void closeAction() {
		trainingDto = new Training();
		date = new Date();
	}

}
