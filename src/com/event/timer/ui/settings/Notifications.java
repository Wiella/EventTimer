package com.event.timer.ui.settings;

import com.alee.laf.label.WebLabel;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.SystemUtils;
import com.event.timer.style.icons.Icons;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Separate feature providing small view of a single event at any point on the screen.
 * Notifications can be configured to display announcement of current or any specific event.
 * Notifications only show up whenever announcement is in active range (after entering advance period and before running out of time).
 *
 * @author Mikle Garin
 */

public final class Notifications implements Icons
{
    /**
     * {@link Notification}s settings.
     */
    private static final String NOTIFICATIONS_KEY = "Notifications";

//    private static final List<Notification> notifications;

    public static void position ( final NotificationSettings settings )
    {
        final Notification notification = new Notification ( true );
        notification.add ( new WebLabel ( StyleId.labelShadow, "Sample notification", laurel32 ) );

        final GraphicsDevice screen = SystemUtils.getDefaultScreenDevice ();
        final Rectangle bounds = screen.getDefaultConfiguration ().getBounds ();
        final Dimension ps = notification.getPreferredSize ();
        notification.showPopup ( null, bounds.x / 2 + bounds.width / 2 - ps.width / 2,
                bounds.y / 2 + bounds.height / 2 - ps.height / 2 );
    }

    public static List<NotificationSettings> list ()
    {
        return SettingsManager.get ( NOTIFICATIONS_KEY, new ArrayList<> ( 1 ) );
    }

    public static NotificationSettings create ()
    {
        final NotificationSettings settings = new NotificationSettings ();
        save ( settings );
        return settings;
    }

    public static void save ( final NotificationSettings settings )
    {
        final List<NotificationSettings> all = list ();
        all.removeIf ( s -> settings.id ().equals ( s.id () ) );
        all.add ( settings );
        SettingsManager.set ( NOTIFICATIONS_KEY, all );
    }

    public static void delete ( final NotificationSettings settings )
    {
        final List<NotificationSettings> all = list ();
        all.removeIf ( s -> settings.id ().equals ( s.id () ) );
        SettingsManager.set ( NOTIFICATIONS_KEY, all );
    }
}