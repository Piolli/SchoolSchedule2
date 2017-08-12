package com.akamyshev.expandablerecyclerview

import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by alexandr on 24.07.17.
 */
object DisplayUtils {

    fun pxToDp(context: Context, px: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        val dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        return dp
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        val px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        return px
    }
}