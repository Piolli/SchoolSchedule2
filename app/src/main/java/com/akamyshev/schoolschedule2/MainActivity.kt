package com.akamyshev.schoolschedule2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fragment: Fragment? = null
        var returnValue = false
        when (item.itemId) {
            R.id.navigation_home -> {
                supportActionBar?.title = "Расписание"
                supportActionBar?.elevation = 0F

                fragment = ScheduleViewPager()
                returnValue = true
            }
            R.id.navigation_dashboard -> {
                fragment = ScheduleOfDayFragment()
                supportActionBar?.title = "Звонки"
                returnValue = true
            }
            R.id.navigation_notifications -> {
                fragment = ScheduleOfDayFragment()
                supportActionBar?.title = "Заметки"
                returnValue = true
            }
        }
        if(fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit()
            savePosition(item.itemId)
        }
        returnValue
    }

    fun savePosition(position: Int) {
        val prefs = getSharedPreferences("asd", Context.MODE_APPEND)
        prefs.edit().putInt("position", position).apply()
    }

    fun getPosition(): Int {
        val prefs = getSharedPreferences("asd", Context.MODE_APPEND)
        return prefs.getInt("position", R.id.navigation_home)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //Обязательно присваивать первоначальное значение, без него не инициалзируется
        navigation.selectedItemId = getPosition()
    }
}
