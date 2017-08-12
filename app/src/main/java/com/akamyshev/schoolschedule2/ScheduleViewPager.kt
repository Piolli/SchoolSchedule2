package com.akamyshev.schoolschedule2

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.fragment_schedule_view_pager.*


class ScheduleViewPager : Fragment() {



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_schedule_view_pager, container, false)

        val viewPager = view.findViewById<ViewPager>(R.id.viewpager)
        setupViewPager(viewPager)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        viewPager.currentItem = 0
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(ScheduleOfDayFragment(), "Понедельник")
        adapter.addFragment(ScheduleOfDayFragment(), "Вторник")
        adapter.addFragment(ScheduleOfDayFragment(), "Среда")
        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
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
