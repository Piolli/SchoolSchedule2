package com.akamyshev.timetableclock.timetable_mvp

import android.content.Context
import com.akamyshev.timetableclock.timez.LessonPeriod
import com.akamyshev.timetableclock.timez.TimeAnalyzerResult
import com.akamyshev.usemvp.MvpPresenter
import com.akamyshev.usemvp.MvpView

/**
 * Created by alexandr on 10.08.17.
 */
interface TimetableContract {

    interface View : MvpView {
        fun setTitleProgressLesson(result: TimeAnalyzerResult)
        fun setInfoRemainTime(result: TimeAnalyzerResult)
        fun setProgress(value: Int)
        fun setMaxProgress(maxValue: Int)
        fun initRecyclerView()
        fun setNumberLessonAdapter(numberLesson: Int)
        fun setDataTimes(dataTime: ArrayList<LessonPeriod>)
    }

    interface Presenter : MvpPresenter<View> {
        fun startAnalyzeLoop(context: Context)
        fun stopAnalyzeLoop()
        fun initData()
    }

}