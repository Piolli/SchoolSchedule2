package com.akamyshev.schoolschedule2.schedule_of_day


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.akamyshev.expandablerecyclerview.*
import com.akamyshev.schoolschedule2.R
import com.example.alexandr.schooltime.schedule.Lesson
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_schedule_of_day.*
import io.realm.OrderedCollectionChangeSet
import io.realm.log.RealmLog
import io.realm.log.RealmLogger
import io.realm.RealmResults
import io.realm.OrderedRealmCollectionChangeListener




/**
 * A simple [Fragment] subclass.
 */
class ScheduleOfDayFragment : Fragment(), OnStartDragListener, ScheduleOfDayContract.View {
    private val LOG_TAG = "ScheduleOfDayFragment"
    private var mItemTouchHelper: ItemTouchHelper? = null
    private var selectedDay = 0
    private var adapter: MyDraggableAndExpandableADapter? = null
    private var presenter: ScheduleOfDayPresenter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_schedule_of_day, container, false)
    }

    companion object {
        fun newInstance(day: Int): ScheduleOfDayFragment {
            val fragment = ScheduleOfDayFragment()
            fragment.selectedDay = day
            return fragment
        }
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "onViewCreated")

        initRecyclerView()
        initPresenter()
        initItemTouchHelper()

    }

    private fun initItemTouchHelper() {
        if (adapter != null) {
            val callback = SimpleItemTouchHelperCallback(adapter!!)
            mItemTouchHelper = ItemTouchHelper(callback)
            mItemTouchHelper?.attachToRecyclerView(recycler)
        }
    }

    private fun initPresenter() {
        presenter = ScheduleOfDayPresenter()
        presenter?.bindView(this)
        presenter?.loadLessonData(selectedDay)
    }

    override fun setLessonData(lessons: RealmList<Lesson>) {
        if(adapter == null) {
            adapter = MyDraggableAndExpandableADapter(recycler, context, lessons, this, R.layout.schedule_item)
            recycler.adapter = adapter
        }
        adapter?.data = lessons
    }


    fun initRecyclerView() {
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.setHasFixedSize(true)
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy")
        super.onDestroy()
    }


    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper?.startDrag(viewHolder)
    }

    fun addLesson(lesson: Lesson) {
//        adapter?.
    }


    class MyDraggableAndExpandableADapter(recyclerView: RecyclerView, context: Context, data: RealmList<Lesson>, mDragStartListener: OnStartDragListener, childLayoutId: Int) :
            DraggableAndExpandableRecyclerViewAdapter<Lesson, MyDragExpVH>(context, data, mDragStartListener, childLayoutId) {
        override fun createViewHolder(view: View): MyDragExpVH = MyDragExpVH(view)

        var prevSize = data.size

        init {
            data.addChangeListener { _, changeSet ->
                if (changeSet == null) {
                    notifyDataSetChanged()
                } else {
                    // For deletions, the adapter has to be notified in reverse order.
//                    val deletions = changeSet.deletionRanges
//                    for (i in deletions.indices.reversed()) {
//                        val range = deletions[i]
//                        notifyItemRangeRemoved(range.startIndex, range.length)
//                    }
//
                    if(prevSize != data.size) {
                        val insertions = changeSet.insertionRanges
                        for (range in insertions) {
                            notifyItemRangeInserted(range.startIndex, 1)
                        }
                        prevSize = data.size
                    }

//                    val modifications = changeSet.changeRanges
//                    for (range in modifications) {
//                        notifyItemRangeChanged(range.startIndex, range.length)
//                    }
                    recyclerView.scrollToPosition(0)
                }

            }
        }

        fun addLesson(lesson: Lesson) {
            data.add(lesson)
            notifyItemInserted(0)
        }


    }

    class MyDragExpVH(itemView: View) : DraggableAndExpandableViewHolder<Lesson>(itemView) {
        override fun bindView(data: Lesson) {
//            text?.text = "${adapterPosition+1}. ${data?.name}"
            text?.text = data?.name
            office?.text = data?.office
        }

        override fun getChild(): View = details

        val text = itemView.findViewById<TextView>(R.id.parent_lesson_name)
        val details = itemView.findViewById<View>(R.id.details)
        val office = itemView.findViewById<TextView>(R.id.child_office)
        val mainCard = itemView.findViewById<CardView>(R.id.main_card)
    }
}// Required empty public constructor
