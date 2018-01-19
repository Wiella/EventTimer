package com.event.timer.ui.settings;

import com.alee.extended.behavior.ComponentMoveBehavior;
import com.alee.extended.window.WebPopup;
import com.event.timer.style.skin.Styles;
import com.event.timer.ui.behavior.CtrlMoveBehavior;

/**
 * @author Mikle Garin
 */

public final class Notification extends WebPopup<Notification>
{
    public Notification ( final boolean sample )
    {
        super ( Styles.announcementPopup );

        /**
         * Disable default behavior.
         */
        setCloseOnFocusLoss ( false );
        setCloseOnOuterAction ( false );

        /**
         * Setup move behavior.
         */
        final ComponentMoveBehavior moveBehavior;
        if ( sample )
        {
            moveBehavior = new ComponentMoveBehavior ( Notification.this );
        }
        else
        {
            moveBehavior = new CtrlMoveBehavior ( Notification.this );
        }
        moveBehavior.install ();

        /**
         * Hide behavior.
         */
        onDoubleClick ( e -> hidePopup () );
    }
}