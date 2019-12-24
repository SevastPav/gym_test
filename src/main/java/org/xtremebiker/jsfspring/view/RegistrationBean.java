package org.xtremebiker.jsfspring.view;

/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/

import lombok.Data;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@SessionScope
@Data
public class RegistrationBean {

    List<String> errors = new ArrayList<>();

    UserProfile userProfile;

	public Date date;

    private final TrainingRepository trainingRepository;

    private final UserProfileRepository userProfileRepository;

    public RegistrationBean(TrainingRepository trainingRepository, UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
        this.trainingRepository = trainingRepository;
        userProfile = new UserProfile();
    }

    private boolean checkRegistrationForm(UserProfile userProfile) {
        errors = new ArrayList<>();
        if (userProfile.getLogin().isEmpty()) {
            errors.add("Некорректный логин");
            error("Некорректный логин");
            return false;
        }
        if (userProfile.getFio().isEmpty()) {
            errors.add("Введите ФИО");
            error("Введите ФИО");
            return false;
        }
        if (userProfileRepository.findByLogin(userProfile.getLogin()).isPresent()) {
            errors.add("Такой пользователь уже существует");
            error("Такой пользователь уже существует");
        }
        if (userProfile.getPassword().isEmpty()) {
        	errors.add("Введите пароль");
            error("Введите пароль");
        }
		Date nowDate = new Date();
		if (nowDate.before(date)) {
			errors.add("Вы не могли родиться в будущем :)");
			error("Вы не могли родиться в будущем :)");
		}
        return errors.isEmpty();
    }

    private void error(String details) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!", details));
    }

    public void saveAdmin() {
        if (!checkRegistrationForm(userProfile))
            return;
        userProfile.setRoles(Collections.singleton(Rle.ADMIN));
		userProfile.setBirthday(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        userProfileRepository.save(userProfile);
        date = new Date();
		userProfile = new UserProfile();
    }

    public void saveClient() {
        if (!checkRegistrationForm(userProfile))
            return;
        userProfile.setRoles(Collections.singleton(Rle.USER));
		userProfile.setBirthday(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        userProfileRepository.save(userProfile);
		date = new Date();
		userProfile = new UserProfile();
    }

    public void saveTrainer() {
        if (!checkRegistrationForm(userProfile))
            return;
        userProfile.setRoles(Collections.singleton(Rle.TRAINER));
		userProfile.setBirthday(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        userProfileRepository.save(userProfile);
		date = new Date();
		userProfile = new UserProfile();
    }

}
