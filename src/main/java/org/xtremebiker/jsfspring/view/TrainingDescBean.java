package org.xtremebiker.jsfspring.view;

/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/

import lombok.Data;
import org.primefaces.PrimeFaces;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.xtremebiker.jsfspring.entity.Training;
import org.xtremebiker.jsfspring.entity.TrainingDescription;
import org.xtremebiker.jsfspring.entity.UserProfile;
import org.xtremebiker.jsfspring.repository.TrainingDescRepository;
import org.xtremebiker.jsfspring.repository.TrainingRepository;
import org.xtremebiker.jsfspring.repository.UserProfileRepository;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@SessionScope
@Data
public class TrainingDescBean {

	private TrainingDescription trainingDescDto;

	private final TrainingDescRepository trainingDescRepository;

	public TrainingDescBean(TrainingDescRepository trainingDescRepository) {
		this.trainingDescRepository = trainingDescRepository;
	}

	@PostConstruct
	public void init() {
		trainingDescDto = new TrainingDescription();
	}

	private void error(String details) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!", details));
	}

	private boolean checkTrainingDescForm(){
		if (trainingDescDto.getTitle().isEmpty()){
			error("Пустое название");
			return false;
		}
		if (trainingDescDto.getDescription().isEmpty()){
			error("Пустое описание");
			return false;
		}
		return true;
	}

	public void save() {
		if (!checkTrainingDescForm())
			return;

		trainingDescRepository.save(trainingDescDto);
		trainingDescDto = new TrainingDescription();
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('trainingDescAddDlg').hide();");
	}

	public void closeAction() {
		trainingDescDto = new TrainingDescription();
	}

}
