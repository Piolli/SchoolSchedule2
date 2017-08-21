package com.akamyshev.schoolschedule2

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_create_lesson.*
import kotlinx.android.synthetic.main.content_create_lesson.*

class CreateLessonActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_lesson)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        fab.setOnClickListener { view ->
            if(create_lesson_name?.text?.toString()?.isNotEmpty() ?: false &&
                    create_lesson_office?.text?.toString()?.isNotEmpty()?: false) {
                val intent = Intent()
                intent.putExtra(ID_LESSON_NAME, create_lesson_name.text.toString())
                intent.putExtra(ID_LESSON_OFFICE, create_lesson_office.text.toString())
                intent.putExtra(ID_LESSON_START_TIME, "8:30")
                intent.putExtra(ID_LESSON_END_TIME, "9:15")
                setResult(ScheduleViewPager.REQUEST_CODE_FOR_CREATE_LESSON, intent)
                finish()
            }
        }
    }

    companion object {
        val ID_LESSON_NAME = "lesson_name"
        val ID_LESSON_OFFICE = "lesson_office"
        val ID_LESSON_START_TIME = "lesson_start_time"
        val ID_LESSON_END_TIME = "lesson_end_time"
    }

}
