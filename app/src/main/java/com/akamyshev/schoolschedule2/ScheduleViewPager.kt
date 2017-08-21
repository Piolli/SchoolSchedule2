package com.akamyshev.schoolschedule2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import com.akamyshev.schoolschedule2.schedule.School
import com.akamyshev.schoolschedule2.schedule_of_day.ScheduleOfDayFragment
import com.example.alexandr.schooltime.schedule.Lesson
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_schedule_view_pager.*


class ScheduleViewPager : Fragment() {
    val LOG_TAG = "ScheduleViewPager"
    private var adapter: ViewPagerAdapter? = null
    companion object {
        val REQUEST_CODE_FOR_CREATE_LESSON = 0x1
    }



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_schedule_view_pager, container, false)

        adapter = ViewPagerAdapter(fragmentManager)

        val viewPager = view.findViewById<ViewPager>(R.id.viewpager)
        setupViewPager(viewPager)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        viewPager.currentItem = 0
        tabLayout.setupWithViewPager(viewPager)

        view.findViewById<FloatingActionButton>(R.id.createNewLessonFloatingButton).setOnClickListener {
            val intent = Intent(context, CreateLessonActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_FOR_CREATE_LESSON)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if(resultCode == REQUEST_CODE_FOR_CREATE_LESSON && requestCode == Activity.RESULT_OK) {
        if(data == null) return
        val lessonName = data.extras[CreateLessonActivity.ID_LESSON_NAME] as String
        val lessonOffice = data.extras[CreateLessonActivity.ID_LESSON_OFFICE] as String
        val lessonStartTime = data.extras[CreateLessonActivity.ID_LESSON_START_TIME] as String
        val lessonEndTime = data.extras[CreateLessonActivity.ID_LESSON_END_TIME] as String

        val lesson = Lesson(lessonName, lessonOffice, lessonStartTime + lessonEndTime)

        val currentFragment = adapter?.getItem(viewpager.currentItem)
//        currentFragment?.

        Realm.getDefaultInstance().executeTransaction {
            var school =
                    it.where(School::class.java)
                            .equalTo("schoolName", QuickSettings.getSchoolName())
                            .findFirst()
            val lessons = school
                    ?.getScheduleOfWeekByClassName(QuickSettings.getNameClass())
                    ?.getScheduleOfDayByDay(0)
                    ?.lessons
            if(lessons != null)
                lessons.add(0, lesson)

        }
//        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val days = arrayOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")

        for (i in 0..days.size-1)
            adapter?.addFragment(ScheduleOfDayFragment.newInstance(i), days[i])

        viewPager.adapter = adapter
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy")
        super.onDestroy()
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        private val mFragmentList: MutableList<Fragment> = mutableListOf()
        private val mFragmentTitleList: MutableList<String> = mutableListOf()


        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

}
