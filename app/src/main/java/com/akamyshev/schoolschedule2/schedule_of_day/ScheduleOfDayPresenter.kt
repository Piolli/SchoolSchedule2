package com.akamyshev.schoolschedule2.schedule_of_day

import android.util.Log
import com.akamyshev.schoolschedule2.QuickSettings
import com.akamyshev.schoolschedule2.schedule.ScheduleOfDay
import com.akamyshev.schoolschedule2.schedule.School
import com.akamyshev.usemvp.MvpBasePresenter
import com.example.alexandr.schooltime.schedule.Lesson
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import java.util.*

/**
 * Created by alexandr on 16.08.17.
 */
class ScheduleOfDayPresenter : MvpBasePresenter<ScheduleOfDayContract.View>(), ScheduleOfDayContract.Presenter {
    val LOG_TAG = "ScheduleOfDayPresenter"

    override fun loadLessonData(day: Int) {
        val realm = Realm.getDefaultInstance()

        var school =
                realm.where(School::class.java)
                        .equalTo("schoolName", QuickSettings.getSchoolName())
                        .findFirst()
        val lessons = school
                ?.getScheduleOfWeekByClassName(QuickSettings.getNameClass())
                ?.getScheduleOfDayByDay(day)
                ?.lessons
        if(lessons != null)
            view?.setLessonData(lessons)


    }



}