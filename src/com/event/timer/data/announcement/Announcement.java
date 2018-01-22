package com.event.timer.data.announcement;

import com.alee.managers.settings.SettingsManager;
import com.event.timer.data.encounter.Encounter;
import com.event.timer.data.event.AnnouncementData;
import com.event.timer.data.event.Event;
import com.event.timer.data.notification.NotificationSettings;
import com.event.timer.style.format.TimerUnits;

import java.util.ArrayList;
import java.util.List;

/**
 * Single {@link Announcement} that will be displayed on the UI.
 *
 * @author Mikle Garin
 */

public final class Announcement
{
    /**
     * {@link Announcement}s settings.
     */
    private static final String ANNOUNCEMENTS_KEY = "Announcements";

    /**
     * Remaining {@link Encounter} time when {@link Announcement} should be made.
     */
    private final long time;

    /**
     * {@link Event} for which this specific {@link Announcement} exists.
     */
    private final Event event;

    /**
     * {@link AnnouncementData} containing detailed information about occurred {@link Event} cycle.
     */
    private final AnnouncementData data;

    /**
     * Constructs new {@link Announcement}.
     *
     * @param time  exact {@link Announcement} time
     * @param event {@link Event} for which this specific {@link Announcement} exists
     * @param data  {@link AnnouncementData} containing detailed information about occurred {@link Event} cycle
     */
    public Announcement ( final long time, final Event event, final AnnouncementData data )
    {
        super ();
        this.time = time;
        this.event = event;
        this.data = data;
    }

    /**
     * Returns unique {@link Announcement} identifier.
     * It should be unique within the {@link Encounter} this {@link Announcement} comes from.
     *
     * @return unique {@link Announcement} identifier
     */
    public String id ()
    {
        return event ().id () + " @ " + TimerUnits.get ().toString ( time );
    }

    /**
     * Returns remaining {@link Encounter} time when {@link Announcement} should be made.
     *
     * @return remaining {@link Encounter} time when {@link Announcement} should be made
     */
    public long time ()
    {
        return time;
    }

    /**
     * Returns {@link Event} for which this specific {@link Announcement} exists.
     *
     * @return {@link Event} for which this specific {@link Announcement} exists
     */
    public Event event ()
    {
        return event;
    }

    /**
     * Returns {@link AnnouncementData} containing detailed information about occurred {@link Event} cycle.
     *
     * @return {@link AnnouncementData} containing detailed information about occurred {@link Event} cycle
     */
    public AnnouncementData data ()
    {
        return data;
    }

    /**
     * Returns {@link Announcement} text with the specified time left.
     *
     * @param timeLeft time left until {@link Announcement}
     * @return {@link Announcement} text with the specified time left
     */
    public String text ( final long timeLeft )
    {
        /**
         * Exact time of announcement.
         */
        final String exactTimeText = TimerUnits.get ().toString ( time () );

        /**
         * Announcement text.
         */
        final String infoText = data ().text ();

        /**
         * Time left until announcement.
         */
        final long advance = event ().time ().advance ();
        final String advanceText = 1000 <= timeLeft && timeLeft <= advance ? " (" + TimerUnits.get ().toString ( timeLeft ) + ")" : "";

        /**
         * Returning resulting text.
         */
        return exactTimeText + ": " + infoText + advanceText;
    }

    /**
     * Returns whether or not this {@link Announcement} is enabled.
     *
     * @return {@code true} if this {@link Announcement} is enabled
     */
    public boolean enabled ()
    {
        return event ().enabled () && load ().enabled ();
    }

    /**
     * Sets whether or not this {@link Announcement} is enabled.
     *
     * @param enabled whether or not this {@link Announcement} is enabled
     */
    public void setEnabled ( final boolean enabled )
    {
        /**
         * Ensure event is enabled.
         */
        if ( enabled && !event ().enabled () )
        {
            event ().setEnabled ( true );
        }

        /**
         * Update announcement state.
         */
        final AnnouncementSettings settings = load ();
        settings.setEnabled ( enabled );
        save ( settings );
    }

    /**
     * Returns {@link NotificationSettings} used to display this {@link Announcement}.
     *
     * @return {@link NotificationSettings} used to display this {@link Announcement}
     */
    public NotificationSettings notification ()
    {
        return event ().notification ();
    }

    /**
     * Returns loaded {@link AnnouncementSettings}.
     *
     * @return loaded {@link AnnouncementSettings}
     */
    private AnnouncementSettings load ()
    {
        final String group = event ().encounter ().settingsGroup ();
        final List<AnnouncementSettings> all = SettingsManager.get ( group, ANNOUNCEMENTS_KEY, new ArrayList<> ( 1 ) );
        return all.stream ().filter ( s -> id ().equals ( s.id () ) ).findFirst ().orElse ( new AnnouncementSettings ( id () ) );
    }

    /**
     * Saves {@link AnnouncementSettings}.
     *
     * @param settings {@link AnnouncementSettings} to save
     */
    private void save ( final AnnouncementSettings settings )
    {
        final String group = event ().encounter ().settingsGroup ();
        final List<AnnouncementSettings> all = SettingsManager.get ( group, ANNOUNCEMENTS_KEY, new ArrayList<> ( 1 ) );
        all.removeIf ( announcementSettings -> id ().equals ( announcementSettings.id () ) );
        all.add ( settings );
        SettingsManager.set ( group, ANNOUNCEMENTS_KEY, all );
    }
}