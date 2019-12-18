package org.xtremebiker.jsfspring.services;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@Getter
public class WeekService {

    private LocalDate today = LocalDate.now();
    private LocalDate monday;
    private LocalDate tuesday;
    private LocalDate wednesday;
    private LocalDate thursday;
    private LocalDate friday;
    private LocalDate saturday;
    private LocalDate sunday;

    public void setCurrentWeek(){
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

}
