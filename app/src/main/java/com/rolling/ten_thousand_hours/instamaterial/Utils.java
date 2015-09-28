package com.rolling.ten_thousand_hours.instamataterial;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by 10000_hours on 2015/9/26.
 */
public class Utils {
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    /**
     * 将dp转换为px,用dp*屏幕的密度
     * @param dp
     * @return
     */
    public static int dpTopx( int dp ) {
        int screenDensity = (int) (dp * Resources.getSystem().getDisplayMetrics().density);
        return screenDensity;
    }

    /**
     * 获取屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        if ( screenHeight == 0 ) {
            WindowManager windowManager =
                    (android.view.WindowManager)
                            context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            screenHeight = point.y;
        }
        return screenHeight;
    }

    /**
     * 获取屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            WindowManager windowManager =
                    (WindowManager)
                            context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            screenWidth = point.x;
        }
        return screenWidth;
    }

    /**
     * 判断是否是android 5 系统
     */
    public static boolean isAndroid5 () {
        boolean isAndroid5 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        return isAndroid5;
    }
}
