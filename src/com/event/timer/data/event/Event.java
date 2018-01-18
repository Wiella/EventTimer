package com.event.timer.data.event;

import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.encounter.Encounter;
import com.event.timer.style.sound.SoundEffect;

import java.util.List;

/**
 * Represents single specific Guild Wars 2 boss encounter event.
 *
 * @author Mikle Garin
 */

public interface Event
{
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
     * Returns {@link Announcement} for this event for the specified encounter.
     *
     * @param encounter {@link Encounter}
     * @return {@link Announcement} for this event for the specified encounter
     */
    public List<Announcement> announcements ( Encounter encounter );
}