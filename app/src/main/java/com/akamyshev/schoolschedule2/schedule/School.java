package com.akamyshev.schoolschedule2.schedule;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alexandr on 04.04.2017.
 */

public class School extends RealmObject {
    @PrimaryKey
    private
    String schoolName;

    public School() { }

    private int version;

    private RealmList<ScheduleOfWeek> scheduleOfWeeks = new RealmList<>();

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public RealmList<ScheduleOfWeek> getScheduleOfWeeks() {
        return scheduleOfWeeks;
    }

    public void addScheduleOfWeek(ScheduleOfWeek week) {
        scheduleOfWeeks.add(week);
    }

    public ScheduleOfWeek getScheduleOfWeekByClassName(String className) {
        for(ScheduleOfWeek week : scheduleOfWeeks)
            if(className.equals(week.getClassName()))
                return week;
        return new ScheduleOfWeek();
    }

    public void setScheduleOfWeeks(RealmList<ScheduleOfWeek> scheduleOfWeeks) {
        this.scheduleOfWeeks = scheduleOfWeeks;
    }
}
