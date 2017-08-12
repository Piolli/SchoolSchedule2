package com.akamyshev.expandablerecyclerview

import android.view.MotionEvent
import android.support.v4.view.MotionEventCompat

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import java.util.Collections;


/**
 * Created by alexandr on 25.07.17.
 */

/**
 * This class realize drag and expand recycler view items
 * @param T the type of data input
 * @param K the type of view holder class (Object: ViewHolder)
 * @param data<T> the collection for bind view in adapter
 * @param childLayoutId the xml layout for a item in recycler view
 */
abstract class DraggableAndExpandableRecyclerViewAdapter<T, K : DraggableAndExpandableViewHolder<T>>
    (context: Context, data: MutableList<T>, private val mDragStartListener: OnStartDragListener, childLayoutId: Int)
    : ExpandableRecyclerViewAdapter<T, K>(context, childLayoutId, data), ItemTouchHelperAdapter {

    override fun onBindViewHolder(holder: K, position: Int) {
        super.onBindViewHolder(holder, position)

        //Start drag on click on child view
        // Start a drag whenever the handle view it touched
//        holder.getChild().setOnTouchListener { v, event ->
//            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                mDragStartListener.onStartDrag(holder)
//            }
//            false
//        }
    }


    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(data, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

abstract class DraggableAndExpandableViewHolder<in T>(itemView: View) : ExpandableViewHolder<T>(itemView), ItemTouchHelperViewHolder {

    override fun onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY)
    }

    override fun onItemClear() {
        itemView.setBackgroundColor(0)
    }

    //return view which expand or collapse
    abstract override fun getChild(): View

    abstract override fun bindView(data: T)
}