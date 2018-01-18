package com.event.timer;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleManager;
import com.alee.skin.dark.DarkSkin;
import com.alee.utils.SwingUtils;
import com.event.timer.style.skin.EventTimerExtension;
import com.event.timer.ui.EventTimerDialog;

/**
 * Main timer class used for launching.
 *
 * @author Mikle Garin
 */

public final class EventTimer
{
    /**
     * Settings group name.
     * Can be used to flush previous timer settings.
     */
    private static final String group = "EventTimer.v1";

    /**
     * Launches {@link EventTimer}.
     */
    public static void main ( final String[] args )
    {
        SwingUtils.invokeLater ( () -> {
            try
            {
                /**
                 * L&F installation.
                 */
                WebLookAndFeel.setForceSingleEventsThread ( true );
                WebLookAndFeel.install ( DarkSkin.class );
                StyleManager.addExtensions ( new EventTimerExtension () );

                /**
                 * Manager settings.
                 */
                SettingsManager.setDefaultSettingsDirName ( ".EventTimer" );
                SettingsManager.setDefaultSettingsGroup ( group );

                /**
                 * Application.
                 */
                final EventTimerDialog eventTimer = new EventTimerDialog ();
                eventTimer.setVisible ( true );
            }
            catch ( final Exception e )
            {
                /**
                 * Something went wrong.
                 */
                e.printStackTrace ();

                /**
                 * Exiting application.
                 */
                System.exit ( 0 );
            }
        } );
    }
}