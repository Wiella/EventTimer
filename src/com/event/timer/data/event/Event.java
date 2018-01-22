package com.event.timer.data.event;

import com.alee.managers.settings.SettingsManager;
import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.encounter.Encounter;
import com.event.timer.data.notification.NotificationSettings;
import com.event.timer.ui.notification.Notifications;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Represents single specific Guild Wars 2 boss encounter event.
 *
 * @author Mikle Garin
 */

public final class Event
{
    /**
     * {@link Event}s settings key.
     */
    private static final String EVENTS_KEY = "Events";

    /**
     * Unique {@link Event} identifier.
     */
    private final String id;

    /**
     * {@link Event} icon.
     */
    private final Supplier<Icon> icon;

    /**
     * {@link Event} name.
     */
    private final Supplier<String> name;

    /**
     * {@link Encounter} for this {@link Event}.
     */
    private final Encounter encounter;

    /**
     * {@link EventTime} for this {@link Event}.
     */
    private final EventTime time;

    /**
     * {@link AnnouncementDataProvider} for this {@link Event}.
     */
    private final AnnouncementDataProvider data;

    /**
     * Constructs new {@link Event}.
     *
     * @param id           unique {@link Event} identifier
     * @param icon         {@link Event} icon
     * @param name         {@link Event} name
     * @param encounter    {@link Encounter} for this {@link Event}
     * @param time         {@link EventTime} for this {@link Event}
     * @param dataProvider {@link AnnouncementDataProvider} for this {@link Event}
     */
    public Event ( final String id, final Supplier<Icon> icon, final Supplier<String> name, final Encounter encounter,
                   final EventTime time, final AnnouncementDataProvider dataProvider )
    {
        super ();
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.encounter = encounter;
        this.time = time;
        this.data = dataProvider;
    }

    /**
     * Returns unique {@link Event} identifier.
     *
     * @return unique {@link Event} identifier
     */
    public String id ()
    {
        return id;
    }

    /**
     * Returns {@link Event} icon.
     *
     * @return {@link Event} icon
     */
    public Icon icon ()
    {
        return icon.get ();
    }

    /**
     * Returns {@link Event} name.
     *
     * @return {@link Event} name
     */
    public String name ()
    {
        return name.get ();
    }

    /**
     * Returns {@link Encounter} for this {@link Event}.
     *
     * @return {@link Encounter} for this {@link Event}
     */
    public Encounter encounter ()
    {
        return encounter;
    }

    /**
     * Returns {@link EventTime} for this {@link Event}.
     *
     * @return {@link EventTime} for this {@link Event}
     */
    public EventTime time ()
    {
        return time;
    }

    /**
     * Returns {@link Announcement}s for this {@link Event}.
     *
     * @return {@link Announcement}s for this {@link Event}
     */
    public List<Announcement> announcements ()
    {
        final List<Announcement> announcements = new ArrayList<> ( 3 );
        int cycle = 0;
        for ( final Long time : time ().times () )
        {
            final AnnouncementData ad = data.get ( cycle );
            announcements.add ( new Announcement ( time, this, ad ) );
            cycle++;
        }
        return announcements;
    }

    /**
     * Returns whether or not this {@link Event} is enabled.
     *
     * @return {@code true} if this {@link Event} is enabled
     */
    public boolean enabled ()
    {
        return load ().enabled ();
    }

    /**
     * Sets whether or not this {@link Event} is enabled.
     *
     * @param enabled whether or not this {@link Event} is enabled
     */
    public void setEnabled ( final boolean enabled )
    {
        final EventSettings settings = load ();
        settings.setEnabled ( enabled );
        save ( settings );
    }

    /**
     * Returns {@link NotificationSettings} used to display {@link Announcement}s for this {@link Event}.
     *
     * @return {@link NotificationSettings} used to display {@link Announcement}s for this {@link Event}
     */
    public NotificationSettings notification ()
    {
        final String notificationId = load ().notification ();
        return Notifications.get ( notificationId );
    }

    /**
     * Sets {@link NotificationSettings} used to display {@link Announcement}s for this {@link Event}.
     *
     * @param notification {@link NotificationSettings} used to display {@link Announcement}s for this {@link Event}
     */
    public void setNotification ( final NotificationSettings notification )
    {
        final EventSettings settings = load ();
        settings.setNotification ( notification.id () );
        save ( settings );
    }

    /**
     * Returns loaded {@link EventSettings}.
     *
     * @return loaded {@link EventSettings}
     */
    private EventSettings load ()
    {
        final String group = encounter ().settingsGroup ();
        final List<EventSettings> all = SettingsManager.get ( group, EVENTS_KEY, new ArrayList<> ( 1 ) );
        return all.stream ().filter ( s -> id ().equals ( s.id () ) ).findFirst ().orElse ( new EventSettings ( id () ) );
    }

    /**
     * Saves {@link EventSettings}.
     *
     * @param settings {@link EventSettings} to save
     */
    private void save ( final EventSettings settings )
    {
        final String group = encounter ().settingsGroup ();
        final List<EventSettings> all = SettingsManager.get ( group, EVENTS_KEY, new ArrayList<> ( 1 ) );
        all.removeIf ( s -> id ().equals ( s.id () ) );
        all.add ( settings );
        SettingsManager.set ( group, EVENTS_KEY, all );
    }
}