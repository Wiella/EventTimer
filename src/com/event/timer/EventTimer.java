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
                WebLookAndFeel.install ( DarkSkin.class );
                StyleManager.addExtensions ( new EventTimerExtension () );

                /**
                 * Manager settings.
                 */
                SettingsManager.setDefaultSettingsGroup ( "Announcer" );
                SettingsManager.setDefaultSettingsDirName ( ".EventTimer" );

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