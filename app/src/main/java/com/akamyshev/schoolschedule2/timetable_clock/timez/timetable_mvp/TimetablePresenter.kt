package com.akamyshev.timetableclock.timetable_mvp

import android.content.Context
import android.os.Handler
import com.akamyshev.timetableclock.timez.LessonPeriod
import com.akamyshev.timetableclock.timez.TimeAnalyzer
import com.akamyshev.timetableclock.timez.TimeAnalyzerResult
import com.akamyshev.usemvp.MvpBasePresenter
import java.util.*


/**
 * Created by alexandr on 10.08.17.
 */
class TimetablePresenter : MvpBasePresenter<TimetableContract.View>(), TimetableContract.Presenter {
    private var dataTimes: ArrayList<LessonPeriod>? = null
    private val handler = Handler()
    private var mResreshInfoRunnable: Runnable? = null
    private var isInitData = false


    override fun initData() {
        if(dataTimes == null) {
            //get data from repository
            dataTimes = arrayListOf(LessonPeriod("8:30", "9:15", 15),
                    LessonPeriod("9:30", "10:15", 20),
                    LessonPeriod("10:35", "11:20", 20),
                    LessonPeriod("11:40", "12:25", 5),
                    LessonPeriod("12:30", "13:15", 15),
                    LessonPeriod("13:30", "14:15", 15))
            view?.setDataTimes(dataTimes!!)
            isInitData = true
        }
    }

    override fun destroy() {
        super.destroy()
        stopAnalyzeLoop()
        dataTimes = null
        mResreshInfoRunnable = null
    }

    override fun bindView(view: TimetableContract.View) {
        super.bindView(view)
        initData()
    }

    override fun startAnalyzeLoop(context: Context) {
        if(dataTimes == null || !isInitData) return
        val gregC = GregorianCalendar()


        mResreshInfoRunnable = object : Runnable {
            override fun run() {

                gregC.add(GregorianCalendar.MINUTE, 1)

                //Log.d("NowTime", "${gregC[Calendar.HOUR_OF_DAY]}:${gregC[Calendar.MINUTE]}")

                val timeA = TimeAnalyzer(dataTimes!!, gregC.time)
                val result: TimeAnalyzerResult = timeA.analyzeTime()

                //Log.d("TimeRESULT",result.toString())
                context.run {
                    view?.setProgress(result.passedTimeInMinutes)
                    view?.setMaxProgress(result.lenghtAllTimeInMinutes)
                    view?.setNumberLessonAdapter(result.numberLesson)
                    view?.setTitleProgressLesson(result)
                    view?.setInfoRemainTime(result)
                }
                if(result.message == TimeAnalyzerResult.AnalyzerMessage.empty)
                    handler.postDelayed(this, 500)
            }
        }
        mResreshInfoRunnable?.run()
    }

    override fun stopAnalyzeLoop() {
        if(!isInitData) return
        handler.removeCallbacks(mResreshInfoRunnable)
    }


}