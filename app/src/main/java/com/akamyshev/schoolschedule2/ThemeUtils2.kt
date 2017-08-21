package com.akamyshev.schoolschedule2

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat


/**
 * Created by alexandr on 20.08.17.
 */
object ThemeUtils2 {



    fun getThemeColor(context: Context): Int = ContextCompat.getColor(context, R.color.defaultThemeColor)

    fun createGradient(colors: IntArray, orientation: GradientDrawable.Orientation, cornerRadius: Float): GradientDrawable {
        val gradientDrawable = GradientDrawable(orientation, colors)
        gradientDrawable.cornerRadius = cornerRadius
        gradientDrawable.setGradientCenter(0f, 100f)

        return gradientDrawable
    }

    fun setBrightnessColor(color: Int, factor: Float): Int {
        val r: Float = Color.red(color)*factor;
        val g: Float = Color.green(color)*factor;
        val b: Float = Color.blue(color)*factor;
        val ir = Math.min(255,r.toInt())
        val ig = Math.min(255,g.toInt())
        val ib = Math.min(255,b.toInt())
        val ia = Color.alpha(color)

        return Color.argb(ia, ir, ig, ib)
    }


}