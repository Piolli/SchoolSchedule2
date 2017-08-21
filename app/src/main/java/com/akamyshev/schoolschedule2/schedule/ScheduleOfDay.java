package com.akamyshev.schoolschedule2.schedule;

import android.support.annotation.IntRange;

import com.example.alexandr.schooltime.schedule.Lesson;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alexandr on 03.04.2017.
 */

public class ScheduleOfDay extends RealmObject {

    private int day;

    public ScheduleOfDay() {
    }

    public ScheduleOfDay(int day) {
        this.day = day;
    }

    public RealmList<Lesson> lessons = new RealmList<>();


    public int getDay() {
        return day;
    }

    public void setDay(@IntRange(from=0,to=6) int day) {
        this.day = day;
    }

    public RealmList<Lesson> getLessons() {
        return lessons;
    }


    public void setLessons(RealmList<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void addLesson(String name, String office) {
        lessons.add(new Lesson(name, office, "time"));
    }
}
