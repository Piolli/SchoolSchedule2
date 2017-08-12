package com.akamyshev.schoolschedule2


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.akamyshev.expandablerecyclerview.*
import kotlinx.android.synthetic.main.fragment_schedule_of_day.*


/**
 * A simple [Fragment] subclass.
 */
class ScheduleOfDayFragment : Fragment(), OnStartDragListener {
    private var mItemTouchHelper: ItemTouchHelper? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_schedule_of_day, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        val data: MutableList<String> = mutableListOf("Русский язык", "Литература", "Математика", "Физ-ра", "Обществознание", "Культуроведение")

        //Drag & Expand adapter
        val adapter = MyDraggableAndExpandableADapter(context, data, this, R.layout.item)
        recycler.adapter = adapter

        val callback = SimpleItemTouchHelperCallback(adapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(recycler)
    }

    fun initRecyclerView() {
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.setHasFixedSize(true)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper?.startDrag(viewHolder)
    }


    class MyDraggableAndExpandableADapter(context: Context, data: MutableList<String>, mDragStartListener: OnStartDragListener, childLayoutId: Int) :
            DraggableAndExpandableRecyclerViewAdapter<String, MyDragExpVH<String>>(context, data, mDragStartListener, childLayoutId) {
        override fun createViewHolder(view: View): MyDragExpVH<String> = MyDragExpVH(view)
    }

    class MyDragExpVH<in T>(itemView: View) : DraggableAndExpandableViewHolder<T>(itemView) {
        override fun bindView(data: T) {
            if(data is String) {
                text?.text = data
            }
        }

        override fun getChild(): View = details

        val text = itemView.findViewById<TextView>(R.id.text)
        val details = itemView.findViewById<View>(R.id.details)
        val mainCard = itemView.findViewById<CardView>(R.id.main_card)
    }
}// Required empty public constructor
