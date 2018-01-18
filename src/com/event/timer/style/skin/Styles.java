package com.event.timer.style.skin;

import com.alee.managers.style.ChildStyleId;
import com.alee.managers.style.StyleId;

/**
 * @author Mikle Garin
 */

public final class Styles
{
    /**
     * Basic styles.
     */
    public static final StyleId announcementLabel = StyleId.of ( "announcement" );
    public static final StyleId customizedSeparator = StyleId.of ( "customized" );
    public static final StyleId customizedButton = StyleId.of ( "customized" );
    public static final StyleId customizedToggleButton = StyleId.of ( "customized" );
    public static final StyleId customizedCheckBox = StyleId.of ( "customized" );

    /**
     * Dialog styles.
     */
    public static final StyleId dialog = StyleId.of ( "timer-dialog" );
    public static final ChildStyleId dialogTitle = ChildStyleId.of ( "title" );
    public static final ChildStyleId dialogTitleSettings = ChildStyleId.of ( "settings" );
    public static final ChildStyleId dialogContentArea = ChildStyleId.of ( "content-area" );
}