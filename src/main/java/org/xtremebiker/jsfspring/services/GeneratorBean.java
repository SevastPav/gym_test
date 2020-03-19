package org.xtremebiker.jsfspring.services;

/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;*/

import lombok.Data;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.xtremebiker.jsfspring.entity.Rle;
import org.xtremebiker.jsfspring.entity.Training;
import org.xtremebiker.jsfspring.entity.TrainingDescription;
import org.xtremebiker.jsfspring.entity.UserProfile;
import org.xtremebiker.jsfspring.repository.TrainingDescRepository;
import org.xtremebiker.jsfspring.repository.TrainingRepository;
import org.xtremebiker.jsfspring.repository.UserProfileRepository;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Component
@SessionScope
@Data
public class GeneratorBean {

    List<String> errors = new ArrayList<>();

    String userLoginPrefix = "";

    int countOfUsers = 0;

    int countOfTrainings = 0;

    private final TrainingRepository trainingRepository;

    private final UserProfileRepository userProfileRepository;

    private final TrainingDescRepository trainingDescRepository;

    private final WeekService weekService;

    public GeneratorBean(TrainingRepository trainingRepository, UserProfileRepository userProfileRepository,
                         TrainingDescRepository trainingDescRepository, WeekService weekService) {
        this.userProfileRepository = userProfileRepository;
        this.trainingRepository = trainingRepository;
        this.trainingDescRepository = trainingDescRepository;
        this.weekService = weekService;
    }

    private void error(String details) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!", details));
    }

    private void generateUsers(String loginPrefix, int countOfUsers, Rle role) {
        if (countOfUsers != 0) {
            for (int i = 0; i < countOfUsers; i++) {
                if (userProfileRepository.findByLogin(loginPrefix + i).isPresent())
                    continue;
                UserProfile userProfile = new UserProfile();
                userProfile.setLogin(loginPrefix + i);
                userProfile.setPassword("password");
                userProfile.setFio(loginPrefix + i);
                Date date = new Date();
                userProfile.setBirthday(date.toInstant().atZone(ZoneId.systemDefault())
                        .toLocalDate().minusWeeks(i));
                userProfile.setRoles(Collections.singleton(role));
                userProfileRepository.save(userProfile);
            }
        }
    }

    public void generateClients() {
        if (!userLoginPrefix.isEmpty() && countOfUsers != 0) {
            generateUsers(userLoginPrefix, countOfUsers, Rle.USER);
            userLoginPrefix = "";
            countOfUsers = 0;
        } else {
            error("Вы ввели некорректные значения");
        }
    }

    public void generateTrainers() {
        if (!userLoginPrefix.isEmpty() && countOfUsers != 0) {
            generateUsers(userLoginPrefix, countOfUsers, Rle.TRAINER);
            userLoginPrefix = "";
            countOfUsers = 0;
        } else {
            error("Вы ввели некорректные значения");
        }
    }

    //Генерирует рандомное число от first до second
    private int generateRandomValue(int first, int second) {
        return first + (int) (Math.random() * second);
    }

    public void generateTrainings() {
        if (countOfTrainings != 0) {
            List<UserProfile> trainers = userProfileRepository
                    .findAllByRoles(Collections.singleton(Rle.TRAINER));
            List<UserProfile> clients = userProfileRepository
                    .findAllByRoles(Collections.singleton(Rle.USER));

            List<TrainingDescription> trainingsDesc = trainingDescRepository.findAll();

            for (int i = 0; i < countOfTrainings; i++) {
                Training training = new Training();
                Date date = new Date();
                Date time = new Date();
                training.setDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().minusWeeks(1).minusDays(i));
                training.setTime(time.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().minusMinutes(i));
                training.setTrainerId(trainers.get(generateRandomValue(0, trainers.size() - 1)));
                training.setTrainingDescription(trainingsDesc.get(generateRandomValue(0, trainingsDesc.size() - 1)));
                trainingRepository.save(training);

                int clientsCount = generateRandomValue(0, clients.size() - 1);
                for (int j = 0; j < 4; j++) {
                    int index = generateRandomValue(0, clientsCount);
                    UserProfile client = clients.get(index);
                    if (client.getClientTrainings().contains(training))
                        continue;

                    client.getClientTrainings().add(training);
                    userProfileRepository.save(client);
                }
            }
            countOfTrainings = 0;
        } else {
            error("Вы ввели некорректные значения");
        }
    }

    public void generateTrainingsOnCurrentWeek() {
        weekService.setCurrentWeek();
        generateTrainingOnDay(weekService.getMonday());
        generateTrainingOnDay(weekService.getSunday());
        generateTrainingOnDay(weekService.getFriday());
        generateTrainingOnDay(weekService.getSaturday());
        generateTrainingOnDay(weekService.getThursday());
        generateTrainingOnDay(weekService.getTuesday());
        generateTrainingOnDay(weekService.getWednesday());
    }

    private void generateTrainingOnDay(LocalDate day){
        List<UserProfile> trainers = userProfileRepository
                .findAllByRoles(Collections.singleton(Rle.TRAINER));
        List<UserProfile> clients = userProfileRepository
                .findAllByRoles(Collections.singleton(Rle.USER));

        List<TrainingDescription> trainingsDesc = trainingDescRepository.findAll();

        for (int i = 0; i < 5; i++) {
            Training training = new Training();
            Date time = new Date();
            LocalTime localTime = time.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().minusMinutes(i);
            training.setDate(day);
            training.setTime(localTime.minusHours(generateRandomValue(0, 24)));
            training.setTrainerId(trainers.get(generateRandomValue(0, trainers.size() - 1)));
            training.setTrainingDescription(trainingsDesc.get(generateRandomValue(0, trainingsDesc.size() - 1)));
            trainingRepository.save(training);

            int clientsCount = generateRandomValue(0, (clients.size() - 1) / 2);
            for (int j = 0; j < clientsCount; j++) {
                int index = generateRandomValue(0, clientsCount);
                UserProfile client = clients.get(index);
                if (client.getClientTrainings().contains(training))
                    continue;

                client.getClientTrainings().add(training);
                userProfileRepository.save(client);
            }
        }
    }
}
