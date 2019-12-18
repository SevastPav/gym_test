package org.xtremebiker.jsfspring.view;

/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/
import lombok.Data;
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
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
@SessionScope
@Data
public class TrainingBean {

	private Training trainingDto;

	public Date date;

	public Date time;

	private final TrainingRepository trainingRepository;

	private final UserProfileRepository userProfileRepository;

	public TrainingBean(TrainingRepository trainingRepository, UserProfileRepository userProfileRepository) {
		this.userProfileRepository = userProfileRepository;
		this.trainingRepository = trainingRepository;
	}

	@PostConstruct
	public void init() {
		trainingDto = new Training();
		date = new Date();
	}

	public void save() {
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();
		Optional<UserProfile> trainer = userProfileRepository.findByLogin(authentication.getName());
		if (trainer.isPresent()){
			trainingDto.setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			trainingDto.setTime(time.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
			trainingDto.setTrainerId(trainer.get());
			trainingRepository.save(trainingDto);
		}
	}

	public void delete(Long id) {
		Optional<Training> training = trainingRepository.findByTrainingId(id);
		training.ifPresent(trainingRepository::delete);
	}

	public void deleteFromRecord(Long id) {
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();
		Optional<UserProfile> client = userProfileRepository.findByLogin(authentication.getName());
		Optional<Training> training = trainingRepository.findByTrainingId(id);
		if (client.isPresent() && training.isPresent()){
			UserProfile clientProfile = client.get();
			Training trainingProfile = training.get();
			for (Training tr:clientProfile.getClientTrainings()){
				if (tr.getTrainingId().equals(trainingProfile.getTrainingId())){
					clientProfile.getClientTrainings().remove(tr);
					userProfileRepository.save(clientProfile);
					break;
				}
			}
		}
	}

	public void closeAction() {
		trainingDto = new Training();
		date = new Date();
	}

}
