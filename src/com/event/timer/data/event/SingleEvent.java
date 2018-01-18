package com.event.timer.data.event;

import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.encounter.Encounter;
import com.event.timer.style.icons.Icons;
import com.event.timer.style.sound.SoundEffect;

import javax.swing.*;
import java.util.ArrayList;
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
     * @param id          unique {@link Event} identifier
     * @param encounter   {@link Encounter} for this {@link Event}
     * @param time        remaining encounter time when {@link Event} occurs
     * @param advance     delay for announcement in advance
     * @param description {@link Event} description
     * @param sound       sound effect for this {@link Event}
     */
    public SingleEvent ( final String id, final Encounter encounter,
                         final long time, final long advance, final String description, final SoundEffect sound )
    {
        this ( id, encounter, time, advance, Icons.empty32, description, sound );
    }

    /**
     * Constructs new {@link SingleEvent}.
     *
     * @param id          unique {@link Event} identifier
     * @param encounter   {@link Encounter} for this {@link Event}
     * @param time        remaining encounter time when {@link Event} occurs
     * @param advance     delay for announcement in advance
     * @param icon        {@link Event} icon
     * @param description {@link Event} description
     * @param sound       sound effect for this {@link Event}
     */
    public SingleEvent ( final String id, final Encounter encounter,
                         final long time, final long advance, final Icon icon, final String description,
                         final SoundEffect sound )
    {
        super ( id, encounter, cycle -> icon, cycle -> description, advance, sound );
        this.time = time;
    }

    @Override
    public List<Announcement> announcements ()
    {
        final List<Announcement> announcements = new ArrayList<> ( 1 );
        announcements.add ( new Announcement ( time, icon ( 0 ), description ( 0 ), this ) );
        return announcements;
    }
}