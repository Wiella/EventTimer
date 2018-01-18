package com.event.timer.style.color;

import com.alee.utils.CollectionUtils;

import java.util.List;

/**
 * Collection of various colors.
 *
 * @author Mikle Garin
 */

public interface Colors
{
    /**
     * Common colors.
     */
    public static final String green = "44,206,120"; // ColorUtils.toRGB ( ColorUtils.softColor ( Color.GREEN ) );
    public static final String red = "252,42,56"; // ColorUtils.toRGB ( ColorUtils.softColor ( Color.RED ) );
    public static final String blue = "104,90,226";  // ColorUtils.toRGB ( ColorUtils.softColor ( Color.BLUE ) );

    /**
     * Guild Wars 2 colors.
     */
    public static final String greenGW2 = "34,187,17";

    /**
     * List of all colors.
     */
    public static final List<String> allColors = CollectionUtils.asList (
            green,
            red,
            blue
    );
}