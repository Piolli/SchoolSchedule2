package com.akamyshev.schoolschedule2.schedule;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alexandr on 04.04.2017.
 */

public class ScheduleOfWeek extends RealmObject {
    @PrimaryKey
    private String className;
    private RealmList<ScheduleOfDay> scheduleOfDays = new RealmList<>();

    public ScheduleOfWeek() {
    }

    public ScheduleOfWeek(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public RealmList<ScheduleOfDay> getScheduleOfDays() {
        return scheduleOfDays;
    }

    public void addScheduleOfDay(ScheduleOfDay day) {
        scheduleOfDays.add(day);
    }

    public ScheduleOfDay getScheduleOfDayByDay(int day) {
        for(ScheduleOfDay ofDay : scheduleOfDays)
            if(ofDay.getDay() == day)
                return ofDay;
        return new ScheduleOfDay();
    }

    public void replaceScheduleOfDay(ScheduleOfDay newScheduleOfday, int position) {
        scheduleOfDays.add(position, newScheduleOfday);
    }

    public void setScheduleOfDays(RealmList<ScheduleOfDay> scheduleOfDays) {
        this.scheduleOfDays = scheduleOfDays;
    }


}
