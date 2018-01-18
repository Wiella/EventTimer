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
     * Settings key for disabled {@link Announcement}s.
     */
    private static final String DISABLED_ANNOUNCEMENTS_KEY = "DisabledAnnouncements";

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
        final String group = event ().encounter ().settingsGroup ();
        final List<String> disabled = SettingsManager.get ( group, DISABLED_ANNOUNCEMENTS_KEY, ( List<String> ) null );
        return disabled == null || !disabled.contains ( id () );
    }

    /**
     * Sets whether or not this {@link Announcement} is enabled.
     *
     * @param enabled whether or not this {@link Announcement} is enabled
     */
    public void setEnabled ( final boolean enabled )
    {
        final String id = id ();
        final String group = event ().encounter ().settingsGroup ();
        final List<String> disabled = SettingsManager.get ( group, DISABLED_ANNOUNCEMENTS_KEY, new ArrayList<> ( 0 ) );
        if ( enabled )
        {
            disabled.remove ( id );
        }
        else if ( !disabled.contains ( id ) )
        {
            disabled.add ( id );
        }
        SettingsManager.set ( group, DISABLED_ANNOUNCEMENTS_KEY, !disabled.isEmpty () ? disabled : null );
    }
}