package com.akamyshev.timetableclock.timez

import com.akamyshev.timetableclock.timez.TimeAnalyzerResult.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by alexandr on 31.07.17.
 */

class TimeAnalyzer(val dataTimes: ArrayList<LessonPeriod>, val timeForAnalyze: Date) {


    fun analyzeTime(): TimeAnalyzerResult {
        val resultInfo = TimeAnalyzerResult()
        val analyzatedTime = GregorianCalendar()
        analyzatedTime.time = timeForAnalyze
        val timeInMillis: Long = analyzatedTime.get(GregorianCalendar.HOUR_OF_DAY).toLong() * 60 * 60 * 1000 +
                analyzatedTime.get(GregorianCalendar.MINUTE) * 60 * 1000

//        Log.d("TimeMillis", "timeInMillis: $timeInMillis, firstStart: ${dataTimes.first().getStartLessonTimeInMillis()}, lastEnd: ${dataTimes.last().getEndLessonTimeInMillis()}")


        //Проверка: если уроки еще не начались или уже закончились
        if(timeInMillis < dataTimes.first().getStartLessonTimeInMillis()) {
            resultInfo.message = AnalyzerMessage.beforeLessons
            return resultInfo
        }
        else if(timeInMillis > dataTimes.last().getEndLessonTimeInMillis()) {
            resultInfo.message = AnalyzerMessage.afterLessons
            return resultInfo
        }


        for(i in 0..dataTimes.size) {
            if(between(timeInMillis, dataTimes[i].getStartLessonTimeInMillis(),
                    dataTimes[i].getEndLessonTimeInMillis())) {
                with(resultInfo) {
                    typeResult = TypeResult.Lesson
                    lenghtAllTimeInMinutes = dataTimes[i].getLenghtAllTimeInMinutes()
                    numberLesson = i+1
                    remainTimeInMinutes = substractionTimeToMinutes(dataTimes[i].getEndLessonTimeInMillis(),
                            timeInMillis)
                    passedTimeInMinutes = lenghtAllTimeInMinutes - remainTimeInMinutes
                }
                break
            }
            else if(i+1 < dataTimes.size &&
                    between(timeInMillis, dataTimes[i].getEndLessonTimeInMillis(),
                            dataTimes[i+1].getStartLessonTimeInMillis())) {
                with(resultInfo) {
                    typeResult = TypeResult.FreeTime
                    lenghtAllTimeInMinutes = dataTimes[i].freeTime
                    numberLesson = i+1
                    remainTimeInMinutes = substractionTimeToMinutes(dataTimes[i+1].getStartLessonTimeInMillis(),
                            timeInMillis)
                    passedTimeInMinutes = lenghtAllTimeInMinutes - remainTimeInMinutes
                }
                break
            }

        }

        return resultInfo
    }

    companion object {
        fun substractionTimeToMinutes(bigTime: Long, shortTime: Long): Int {
            val diff = bigTime - shortTime
            val diffMinutes = diff / (60 * 1000) % 60
            val diffHours = diff / (60 * 60 * 1000) % 24
            return (diffMinutes + diffHours * 60).toInt()
        }

        fun between(timeInBetween: Long, leftTime: Long, rightTime: Long): Boolean =  timeInBetween in leftTime..rightTime
    }

}


class LessonPeriod(startLesson: String, endLesson: String, val freeTime: Int) {
    val startHour: Int = startLesson.split(":")[0].toInt()
    val startMinute: Int = startLesson.split(":")[1].toInt()

    val endHour: Int = endLesson.split(":")[0].toInt()
    val endtMinute: Int = endLesson.split(":")[1].toInt()

    fun getStartLessonTimeInMillis() : Long {
        return startMinute.toLong() * 60 * 1000 + startHour.toLong() * 60 * 60 * 1000
    }

    fun getEndLessonTimeInMillis() : Long {
        return endtMinute.toLong() * 60 * 1000 + endHour.toLong() * 60 * 60 * 1000
    }

    fun getLenghtAllTimeInMinutes(): Int {
        return TimeAnalyzer.substractionTimeToMinutes(getEndLessonTimeInMillis(), getStartLessonTimeInMillis())
    }

}

/**
 * @param passedTime - Пройденное время с начала урока
 * @param allTime - длина всего отрезка времени для урока или перемены
 * @param message - сообщение о возможной исключительной ситуации
 * @param remainTime - Прошло времени с начала урока или перемены
 */
class TimeAnalyzerResult {
    var remainTimeInMinutes: Int = -1
    var lenghtAllTimeInMinutes: Int = -1
    var typeResult: TypeResult = TypeResult.Lesson
    var numberLesson: Int = -1
    var message: AnalyzerMessage = AnalyzerMessage.empty
    var passedTimeInMinutes: Int = -1

    enum class TypeResult { Lesson, FreeTime }

    enum class AnalyzerMessage { beforeLessons, afterLessons, empty }

    override fun toString(): String {
        return "TimeAnalyzerResult(remainTimeInMinutes=$remainTimeInMinutes\nlenghtAllTimeInMinutes=$lenghtAllTimeInMinutes\ntypeResult=$typeResult\nnumberLesson=$numberLesson\nmessage=$message)"
    }


}