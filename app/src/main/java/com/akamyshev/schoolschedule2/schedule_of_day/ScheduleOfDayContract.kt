package com.akamyshev.schoolschedule2.schedule_of_day

import android.widget.Adapter
import com.akamyshev.usemvp.MvpPresenter
import com.akamyshev.usemvp.MvpView
import com.example.alexandr.schooltime.schedule.Lesson
import io.realm.RealmList

/**
 * Created by alexandr on 16.08.17.
 */
interface ScheduleOfDayContract {

    interface View : MvpView {
        fun setLessonData(lessons: RealmList<Lesson>)
    }

    interface Presenter : MvpPresenter<View> {
        fun loadLessonData(day: Int)
    }

}