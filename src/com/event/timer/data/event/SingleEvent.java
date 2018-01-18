package com.event.timer.data.event;

import com.alee.utils.CollectionUtils;
import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.encounter.Encounter;
import com.event.timer.style.icons.Icons;
import com.event.timer.style.sound.SoundEffect;

import javax.swing.*;
import java.util.List;

/**
 * An {@link Event} that occurs once per encounter.
 *
 * @author Mikle Garin
 */

public final class SingleEvent extends AbstractEvent
{
    /**
     * Remaining encounter time when {@link Event} occurs.
     */
    private final long time;

    /**
     * Constructs new {@link SingleEvent}.
     *
     * @param time        remaining encounter time when {@link Event} occurs
     * @param advance     delay for announcement in advance
     * @param description {@link Event} description
     * @param sound       sound effect for this {@link Event}
     */
    public SingleEvent ( final long time, final long advance, final String description, final SoundEffect sound )
    {
        this ( time, advance, Icons.empty32, description, sound );
    }

    /**
     * Constructs new {@link SingleEvent}.
     *
     * @param time        remaining encounter time when {@link Event} occurs
     * @param advance     delay for announcement in advance
     * @param icon        {@link Event} icon
     * @param description {@link Event} description
     * @param sound       sound effect for this {@link Event}
     */
    public SingleEvent ( final long time, final long advance, final Icon icon, final String description,
                         final SoundEffect sound )
    {
        super ( cycle -> icon, cycle -> description, advance, sound );
        this.time = time;
    }

    @Override
    public List<Announcement> announcements ( final Encounter encounter )
    {
        return CollectionUtils.asList ( new Announcement ( time, 0, icon ( 0 ), description ( 0 ), this ) );
    }
}