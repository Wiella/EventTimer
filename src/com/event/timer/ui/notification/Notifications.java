package com.event.timer.ui.notification;

import com.alee.managers.settings.SettingsManager;
import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.notification.NotificationSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Separate feature providing small view of a single event at any point on the screen.
 * Notifications can be configured to display announcement of current or any specific event.
 * Notifications only show up whenever announcement is in active range (after entering advance period and before running out of time).
 *
 * @author Mikle Garin
 */

public final class Notifications
{
    /**
     * {@link Notification}s settings.
     */
    private static final String NOTIFICATIONS_KEY = "Notifications";

    /**
     * Displays sample {@link Notification}.
     *
     * @param settings {@link NotificationSettings}
     */
    public static void position ( final NotificationSettings settings )
    {
        final Notification notification = new Notification ( settings );
        notification.showNotification ();
    }

    /**
     * Displays {@link Notification} for the specified {@link Announcement} if it has been configured to display one.
     *
     * @param announcement {@link Announcement} to display
     */
    public static void show ( final Announcement announcement )
    {
        final NotificationSettings settings = announcement.notification ();
        if ( settings != null )
        {
            final Notification notification = new Notification ( settings, announcement );
            notification.showNotification ();
        }
    }

    /**
     * Returns {@link List} of all {@link NotificationSettings} available.
     *
     * @return {@link List} of all {@link NotificationSettings} available
     */
    public static List<NotificationSettings> list ()
    {
        return SettingsManager.get ( NOTIFICATIONS_KEY, new ArrayList<> ( 1 ) );
    }

    /**
     * Returns {@link NotificationSettings} with the specified identifier if they exist.
     *
     * @param id {@link NotificationSettings} identifier
     * @return {@link NotificationSettings} with the specified identifier if they exist
     */
    public static NotificationSettings get ( final String id )
    {
        return list ().stream ().filter ( ns -> ns.id ().equals ( id ) ).findFirst ().orElse ( null );
    }

    /**
     * Returns newly created {@link NotificationSettings}.
     *
     * @return newly created {@link NotificationSettings}
     */
    public static NotificationSettings create ()
    {
        final NotificationSettings settings = new NotificationSettings ();
        save ( settings );
        return settings;
    }

    /**
     * Saves {@link NotificationSettings} modifications.
     *
     * @param settings {@link NotificationSettings} to save
     */
    public static void save ( final NotificationSettings settings )
    {
        final List<NotificationSettings> all = list ();
        all.removeIf ( s -> settings.id ().equals ( s.id () ) );
        all.add ( settings );
        SettingsManager.set ( NOTIFICATIONS_KEY, all );
    }

    /**
     * Deletes specified {@link NotificationSettings}.
     *
     * @param settings {@link NotificationSettings} to delete
     */
    public static void delete ( final NotificationSettings settings )
    {
        final List<NotificationSettings> all = list ();
        all.removeIf ( s -> settings.id ().equals ( s.id () ) );
        SettingsManager.set ( NOTIFICATIONS_KEY, all );
    }
}