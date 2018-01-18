package com.event.timer.data.announcement;

import com.event.timer.data.event.Event;

import javax.swing.*;

/**
 * Single {@link Announcement} that will be displayed on the UI.
 *
 * @author Mikle Garin
 */

public final class Announcement
{
    /**
     * Remaining encounter time when {@link Announcement} should be made.
     */
    private final long time;

    /**
     * {@link Event} cycle for which this {@link Announcement} should be made.
     */
    private final int cycle;

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
     * @param cycle {@link Event} cycle for which this {@link Announcement} should be made
     * @param text  {@link Announcement} text
     * @param event {@link Event} for which this specific {@link Announcement} exists
     */
    public Announcement ( final long time, final int cycle, final String text, final Event event )
    {
        this ( time, cycle, null, text, event );
    }

    /**
     * Constructs new {@link Announcement}.
     *
     * @param time  exact {@link Announcement} time
     * @param cycle {@link Event} cycle for which this {@link Announcement} should be made
     * @param icon  {@link Announcement} icon
     * @param text  {@link Announcement} text
     * @param event {@link Event} for which this specific {@link Announcement} exists
     */
    public Announcement ( final long time, final int cycle, final Icon icon, final String text, final Event event )
    {
        super ();
        this.time = time;
        this.cycle = cycle;
        this.icon = icon;
        this.text = text;
        this.event = event;
    }

    /**
     * Returns remaining encounter time when {@link Announcement} should be made.
     *
     * @return remaining encounter time when {@link Announcement} should be made
     */
    public long time ()
    {
        return time;
    }

    /**
     * Returns {@link Event} cycle for which this {@link Announcement} should be made.
     *
     * @return {@link Event} cycle for which this {@link Announcement} should be made
     */
    public int cycle ()
    {
        return cycle;
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
}