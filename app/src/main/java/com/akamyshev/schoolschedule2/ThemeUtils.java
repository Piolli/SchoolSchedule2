package com.akamyshev.schoolschedule2;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by Alexandr on 21.08.2016.
 */
public class ThemeUtils {
    private static final String PREF_THEME_COLOR = "theme_color";

    public static int getThemeColor(Context context) {
        int defaultColor = ContextCompat.getColor(context, R.color.defaultThemeColor);
        return context.getSharedPreferences("ThemeUtils", Context.MODE_PRIVATE).getInt(PREF_THEME_COLOR, defaultColor);
    }

    public static void saveThemeColor(Context context, int color) {
        context.getSharedPreferences("ThemeUtils", Context.MODE_PRIVATE).edit().putInt(PREF_THEME_COLOR, color).apply();
    }


    public static void initStatusNavigateHeadColor(Activity activity, Drawable themeGradientColor) {
        if (Build.VERSION.SDK_INT >= 21) {
            int color = getThemeColor(activity.getApplicationContext());
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(PictureUtils.setBrightnessColor(color, 0.92f));
            window.setBackgroundDrawable(themeGradientColor);
            initNavigateAndHeadStackApp(activity, color);
//            initNavigateAndHeadStackApp(activity, themeGradientColor);
        }
    }

    private static void initNavigateAndHeadStackApp(Activity activity, int color) {
        if(Build.VERSION.SDK_INT >= 21) {
            Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription taskDescription =
                    new ActivityManager.TaskDescription(activity.getResources().getString(R.string.app_name), bm, color);
            activity.setTaskDescription(taskDescription);
            //Цвет нижней строки с кнопками
            //activity.getWindow().setNavigationBarColor(color);
        }
    }


}
