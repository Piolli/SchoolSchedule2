package com.akamyshev.schoolschedule2

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.akamyshev.timetableclock.timetable_mvp.TimetableClock
import de.jonasrottmann.realmbrowser.RealmBrowser
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.AccelerateDecelerateInterpolator
import com.akamyshev.schoolschedule2.schedule_of_day.ScheduleOfDayFragment
import android.graphics.drawable.GradientDrawable




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        RealmGenerateUtils.clear()
//        RealmGenerateUtils.init(Realm.getDefaultInstance())

//        setSupportActionBar(main_toolbar)
//        main_toolbar?.title = "Расписание"

        val themeColor = ThemeUtils.getThemeColor(this);
        val color2 = PictureUtils.setBrightnessColor(themeColor, 1.15f);
        val color3 = PictureUtils.setBrightnessColor(themeColor, 1.20f);
        val color4 = PictureUtils.setBrightnessColor(themeColor, 1.25f);

        val gradientDrawable = PictureUtils
                .createGradient( intArrayOf(themeColor, color2, color3, color4), GradientDrawable.Orientation.LEFT_RIGHT, 0f)
        supportActionBar?.setBackgroundDrawable(gradientDrawable)
        ThemeUtils.initStatusNavigateHeadColor(this, gradientDrawable)


        setContentView(R.layout.activity_main)
        initBottomNavigation()
        navigation.setBackgroundDrawable(gradientDrawable)
        navigation.itemIconTintList = ColorStateList.valueOf(Color.WHITE)
        navigation.itemTextColor = ColorStateList.valueOf(Color.WHITE)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fragment: Fragment? = null
        var returnValue = false
        when (item.itemId) {
            R.id.navigation_home -> {
//                supportActionBar?.setShowHideAnimationEnabled(true)
                val handler = Handler()
//                handler.postDelayed(Runnable { supportActionBar?.hide() }, 1000)
//                supportActionBar?.hide()

                supportActionBar?.title = "Расписание"
                supportActionBar?.elevation = 0f
                fragment = ScheduleViewPager()
                returnValue = true
            }
            R.id.navigation_dashboard -> {
                fragment = TimetableClock()
//                supportActionBar?.show()
                supportActionBar?.title = "Звонки"
//                supportActionBar?.elevation = 8F
                returnValue = true
            }
            R.id.navigation_notifications -> {
                fragment = ScheduleOfDayFragment()
//                supportActionBar?.show()
                supportActionBar?.title = "Заметки"
                supportActionBar?.elevation = 8F
                returnValue = true
            }
        }
        if(fragment != null) {
            supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.fragment_transition_fade_in, R.anim.fragment_transition_fade_out).replace(R.id.container_fragment, fragment).commit()
//            supportFragmentManager.beginTransaction().replace(R.id.container_fragment, fragment).commit()
            savePosition(item.itemId)
        }
        returnValue
    }

    private fun hideActionBar() {
        val anim = ValueAnimator.ofInt(supportActionBar?.height!!, 0)
        anim.duration = 350
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.addUpdateListener {
            val animValue = it.animatedValue
//            supportActionBar?.
        }
    }



    fun savePosition(position: Int) {
        val prefs = getSharedPreferences("asd", Context.MODE_APPEND)
        prefs.edit().putInt("position", position).apply()
    }

    fun getPosition(): Int {
        val prefs = getSharedPreferences("asd", Context.MODE_APPEND)
        return prefs.getInt("position", R.id.navigation_home)
    }

    private fun initBottomNavigation() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //Обязательно присваивать первоначальное значение, без него не инициалзируется
        navigation.selectedItemId = getPosition()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_realm_browser -> {
                RealmBrowser.startRealmFilesActivity(this)
                return true
            }
        }
        return false
    }
}
