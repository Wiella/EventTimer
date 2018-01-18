package com.event.timer.data.event;

import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.encounter.Encounter;
import com.event.timer.style.sound.SoundEffect;

import javax.swing.*;
import java.util.List;

/**
 * Represents single specific Guild Wars 2 boss encounter event.
 *
 * @author Mikle Garin
 */

public interface Event
{
    /**
     * Returns unique {@link Event} identifier.
     *
     * @return unique {@link Event} identifier
     */
    public String id ();

    /**
     * Returns {@link Icon} for this {@link Event}.
     *
     * @return {@link Icon} for this {@link Event}
     */
    public Icon icon ();

    /**
     * Returns {@link Encounter} for this {@link Event}.
     *
     * @return {@link Encounter} for this {@link Event}
     */
    public Encounter encounter ();

    /**
     * Returns delay used for displaying {@link Announcement}s in advance.
     *
     * @return delay used for displaying {@link Announcement}s in advance
     */
    public long advance ();

    /**
     * Returns sound effect for this {@link Event}.
     *
     * @return sound effect for this {@link Event}
     */
    public SoundEffect sound ();

    /**
     * Returns {@link Announcement} for this event.
     *
     * @return {@link Announcement} for this event
     */
    public List<Announcement> announcements ();
}