package com.akamyshev.expandablerecyclerview

import android.animation.Animator
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.animation.DecelerateInterpolator
import android.animation.ValueAnimator
import android.support.annotation.IdRes
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.akamyshev.schoolschedule2.R
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo


/**
 * Created by alexandr on 01.07.17.
 */

/**
 * This class realize expand and collapse recycler view items
 * @param T the type of data input
 * @param K the type of view holder class (Object: ViewHolder)
 * @param data<T> the collection for bind view in adapter
 * @param childLayoutId the xml layout for a item in recycler view
 */
abstract class ExpandableRecyclerViewAdapter<T, K : ExpandableViewHolder<T>>(
        val context: Context, @IdRes val childLayoutId: Int, val data: MutableList<T>) : RecyclerView.Adapter<K>() {


    override fun onBindViewHolder(holder: K, position: Int) {
        holder.itemView?.setTag(R.string.id_tag_details_view, holder.getChild())
        holder.bindView(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): K {
        val view: View = LayoutInflater.from(context).inflate(childLayoutId, parent, false)
        val viewHolder: K = createViewHolder(view)

        return viewHolder
    }

    abstract fun createViewHolder(view: View): K

}

abstract class ExpandableViewHolder<in T>(val view: View) : ViewHolder(view), View.OnClickListener {

    init {
        view.setOnClickListener(this)
    }

    final override fun onClick(p0: View?) {
        val details = p0?.getTag(R.string.id_tag_details_view) as View
//        if details view is expanded
        if (details.getTag(R.string.id_tag_is_expand) != null && details.getTag(R.string.id_tag_is_expand) as Boolean) {
            collapse(details, 200, 0)
            details.setTag(R.string.id_tag_is_expand, false)
        }
        else {
            expand(details, 200)
            details.setTag(R.string.id_tag_is_expand, true)
        }
    }

    abstract fun getChild(): View

    abstract fun bindView(data: T)

    /**
     * Function expand @view with animation
     * @param targetHeight the height of @view (can be default value from xml resource)
     */
    fun expand(view: View, duration: Int,
               targetHeight: Int = this.view.context.resources.getDimension(R.dimen.expandable_item_recycler_view_height).toInt()) {
        val prevHeight = view.height


        val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
        valueAnimator.addUpdateListener { animation ->
            view.layoutParams.height = animation.animatedValue as Int
            view.requestLayout()
        }
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = duration.toLong()
        val listener: Animator.AnimatorListener = object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                view.visibility = View.VISIBLE
                YoYo.with(Techniques.FadeIn)
                        .duration(200)
                        .repeat(0)
                        .playOn(view);

            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
                view.visibility = View.INVISIBLE
            }

        }
        valueAnimator.addListener(listener)
        valueAnimator.start()
    }

    /**
     * Function collapse @view with animation
     * @param targetHeight
     */
    fun collapse(v: View, duration: Int, targetHeight: Int) {
        val prevHeight = v.height
        val valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight)
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            v.layoutParams.height = animation.animatedValue as Int
            v.requestLayout()
        }
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = duration.toLong()
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                v.visibility = View.GONE
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {
//                v.visibility = View.INVISIBLE
                YoYo.with(Techniques.FadeOut)
                        .duration(200)
                        .repeat(0)
                        .playOn(v);
            }

        })
        valueAnimator.start()

    }
}


