package com.infinitydheer.themanager.domain.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Encapsulates all the display related utility methods
 */
public class DisplayUtils {

    private DisplayUtils(){}

    public static float dpToPixels(float dp, DisplayMetrics dm){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                dm
        );
    }
}
