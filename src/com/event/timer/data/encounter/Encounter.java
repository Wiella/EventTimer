package com.event.timer.data.encounter;

import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.event.Event;

import javax.swing.*;
import java.util.List;

/**
 * Represents single specific Guild Wars 2 boss encounter.
 *
 * @author Mikle Garin
 */

public interface Encounter
{
    /**
     * Returns unique {@link Encounter} identifier.
     *
     * @return unique {@link Encounter} identifier
     */
    public String id ();

    /**
     * Returns {@link Encounter} duration.
     *
     * @return {@link Encounter} duration
     */
    public long duration ();

    /**
     * Returns {@link Encounter} name.
     *
     * @return {@link Encounter} name
     */
    public String name ();

    /**
     * Returns all {@link Event}s that should be announced for this {@link Encounter}.
     *
     * @return all {@link Event}s that should be announced for this {@link Encounter}
     */
    public List<Event> events ();

    /**
     * Returns all {@link Announcement}s for this {@link Encounter}.
     *
     * @return all {@link Announcement}s for this {@link Encounter}
     */
    public List<Announcement> announcements ();

    /**
     * Returns unique settings group for this {@link Encounter}.
     *
     * @return unique settings group for this {@link Encounter}
     */
    public String settingsGroup ();

    /**
     * Returns settings panel for this {@link Encounter}.
     * Anything related to {@link Encounter} settings can be placed on it.
     *
     * @return settings panel for this {@link Encounter}
     */
    public JComponent settings ();
}