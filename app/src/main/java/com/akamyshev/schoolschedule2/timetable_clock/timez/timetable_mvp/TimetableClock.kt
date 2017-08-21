package com.akamyshev.timetableclock.timetable_mvp


import android.os.Bundle
import android.support.annotation.IntRange
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.akamyshev.schoolschedule2.R

import com.akamyshev.timetableclock.timez.LessonPeriod
import com.akamyshev.timetableclock.timez.TimeAnalyzerResult
import kotlinx.android.synthetic.main.fragment_timetable_clock.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class TimetableClock : Fragment(), TimetableContract.View {
    val lessonAdapter = LessonRecyclerAdapter(1, arrayListOf())
    val presenter = TimetablePresenter()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_timetable_clock, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        (activity as AppCompatActivity).supportActionBar?.elevation = 8f


        presenter.bindView(this)
        presenter.startAnalyzeLoop(context)
    }

    override fun setTitleProgressLesson(result: TimeAnalyzerResult) {
        clock_title_progress_lesson.text =
                "Идет ${if(result.typeResult == TimeAnalyzerResult.TypeResult.Lesson) "${result.numberLesson} урок" else "перемена после ${result.numberLesson} урока"}"
    }

    override fun setInfoRemainTime(result: TimeAnalyzerResult) {
        clock_info_remain_time.text =
                "До конца ${if(result.typeResult == TimeAnalyzerResult.TypeResult.Lesson) "урока" else "перемены"} осталось ${result.remainTimeInMinutes} минут(ы)"
    }

    override fun setDataTimes(dataTime: ArrayList<LessonPeriod>) {
        lessonAdapter?.setDataTimes(dataTime)
    }

    override fun setProgress(value: Int) {
        clock_progress_view?.progress = value.toFloat()
    }

    override fun setMaxProgress(maxValue: Int) {
        clock_progress_view?.max = maxValue.toFloat()
    }

    override fun initRecyclerView() {
        clock_lessons_recycler_view?.layoutManager = LinearLayoutManager(context)
        clock_lessons_recycler_view?.setHasFixedSize(true)
        clock_lessons_recycler_view?.adapter = lessonAdapter
    }

    override fun setNumberLessonAdapter(numberLesson: Int) {
        lessonAdapter?.setNumber(numberLesson)
    }

    override fun onDestroy() {
        presenter.unbindView()
        presenter.destroy()
        super.onDestroy()
    }

    class LessonRecyclerAdapter(var numberLesson: Int, var data: ArrayList<LessonPeriod>) : RecyclerView.Adapter<LessonViewHolder>() {

        override fun onBindViewHolder(holder: LessonViewHolder?, position: Int) {
            holder?.bindView(LessonTime(Time(data[position].startHour, data[position].startMinute), Time(data[position].endHour, data[position].endtMinute), 10), numberLesson, position+1)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LessonViewHolder {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.timetable_item, parent, false)
            return LessonViewHolder(view)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        fun setDataTimes(dataT: ArrayList<LessonPeriod>) {
            this.data = dataT
            notifyDataSetChanged()
        }

        fun setNumber(newNumber: Int) {
            Log.d("LessonAdapter", "Adapter.setNumber($newNumber)")
            if(newNumber != numberLesson) {
                numberLesson = newNumber
                notifyDataSetChanged()
            }
        }

    }

    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numberLesson = itemView.findViewById<TextView>(R.id.clock_info_item_number_lesson_text_view)
        val titleLesson = itemView.findViewById<TextView>(R.id.clock_info_item_title_lesson)
        val freeTime = itemView.findViewById<TextView>(R.id.clock_info_item_free_time_text_view)
        val imageView = itemView.findViewById<ImageView>(R.id.clock_info_item_is_now_image_view)
        val lessonPeriod = itemView.findViewById<TextView>(R.id.clock_info_item_lesson_period)

        fun bindView(lessonTime: LessonTime, goingLessonPosition: Int, position: Int) {
            numberLesson.text = position.toString()
            imageView.setImageDrawable(if (goingLessonPosition == position) ContextCompat.getDrawable(itemView.context, android.R.drawable.presence_online)
            else ContextCompat.getDrawable(itemView.context, android.R.drawable.presence_invisible))

            lessonPeriod.text = "С  ${lessonTime.startTime} до ${lessonTime.endTime}"
            titleLesson.text = listOf("Русский язык", "Математика")[position % 2] + " (30$position)"
            freeTime.text = "перемена после урока ${lessonTime.freeTime} минут"
        }
    }

    class Time(@IntRange(from = 0, to = 23) val hour: Int, @IntRange(from = 0, to = 59) val min: Int) {
        override fun toString(): String = "$hour:$min"
    }

    class LessonTime(val startTime: Time, val endTime: Time, val freeTime: Int)

}
