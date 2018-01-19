package com.event.timer.data.announcement;

import com.alee.managers.settings.SettingsManager;
import com.event.timer.data.encounter.Encounter;
import com.event.timer.data.event.Event;
import com.event.timer.style.format.TimerUnits;

import javax.swing.*;
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
     * {@link Announcement} icon.
     */
    private final Icon icon;

    /**
     * {@link Announcement} text.
     */
    private final String text;

    /**
     * {@link Event} for which this specific {@link Announcement} exists.
     */
    private final Event event;

    /**
     * Constructs new {@link Announcement}.
     *
     * @param time  exact {@link Announcement} time
     * @param text  {@link Announcement} text
     * @param event {@link Event} for which this specific {@link Announcement} exists
     */
    public Announcement ( final long time, final String text, final Event event )
    {
        this ( time, null, text, event );
    }

    /**
     * Constructs new {@link Announcement}.
     *
     * @param time  exact {@link Announcement} time
     * @param icon  {@link Announcement} icon
     * @param text  {@link Announcement} text
     * @param event {@link Event} for which this specific {@link Announcement} exists
     */
    public Announcement ( final long time, final Icon icon, final String text, final Event event )
    {
        super ();
        this.time = time;
        this.icon = icon;
        this.text = text;
        this.event = event;
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
     * Returns {@link Announcement} icon.
     *
     * @return {@link Announcement} icon
     */
    public Icon icon ()
    {
        return icon;
    }

    /**
     * Returns {@link Announcement} text.
     *
     * @return {@link Announcement} text
     */
    public String text ()
    {
        return text;
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