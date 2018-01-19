package com.event.timer.data.event;

import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.encounter.Encounter;
import com.event.timer.style.icons.Icons;
import com.event.timer.style.sound.SoundEffect;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An {@link Event} that is repeated multiple times throughout the encounter.
 *
 * @author Mikle Garin
 */

public final class LoopEvent extends AbstractEvent
{
    /**
     * Remaining encounter time when first {@link Event} occurs.
     */
    private final long start;

    /**
     * Delay between event executions.
     */
    private final long delay;

    /**
     * Latest time at which event can be executed.
     */
    private final long end;

    /**
     * Constructs new {@link LoopEvent}.
     *
     * @param encounter   {@link Encounter} for this {@link Event}
     * @param id          unique {@link Event} identifier
     * @param name        {@link Event} name
     * @param start       remaining encounter time when first {@link Event} occurs
     * @param delay       event repeat delay
     * @param advance     delay for announcement in advance
     * @param description {@link Event} description
     * @param sound       sound effect for this {@link Event}
     */
    public LoopEvent ( final Encounter encounter, final String id, final Supplier<String> name,
                       final long start, final long delay, final long advance,
                       final Function<Integer, String> description, final SoundEffect sound )
    {
        this ( encounter, id, name, start, delay, 0L, advance, cycle -> Icons.empty32, description, sound );
    }

    /**
     * Constructs new {@link LoopEvent}.
     *
     * @param encounter   {@link Encounter} for this {@link Event}
     * @param id          unique {@link Event} identifier
     * @param name        {@link Event} name
     * @param start       remaining encounter time when first {@link Event} occurs
     * @param delay       event repeat delay
     * @param advance     delay for announcement in advance
     * @param icon        {@link Event} icon
     * @param description {@link Event} description
     * @param sound       sound effect for this {@link Event}
     */
    public LoopEvent ( final Encounter encounter, final String id, final Supplier<String> name,
                       final long start, final long delay, final long advance,
                       final Function<Integer, Icon> icon, final Function<Integer, String> description, final SoundEffect sound )
    {
        this ( encounter, id, name, start, delay, 0L, advance, icon, description, sound );
    }

    /**
     * Constructs new {@link LoopEvent}.
     *
     * @param encounter   {@link Encounter} for this {@link Event}
     * @param id          unique {@link Event} identifier
     * @param name        {@link Event} name
     * @param start       remaining encounter time when first {@link Event} occurs
     * @param delay       event repeat delay
     * @param end         remaining encounter time after which {@link Event} doesn't occur anymore
     * @param advance     delay for announcement in advance
     * @param description {@link Event} description
     * @param sound       sound effect for this {@link Event}
     */
    public LoopEvent ( final Encounter encounter, final String id, final Supplier<String> name,
                       final long start, final long delay, final long end, final long advance,
                       final Function<Integer, String> description, final SoundEffect sound )
    {
        this ( encounter, id, name, start, delay, end, advance, cycle -> Icons.empty32, description, sound );
    }

    /**
     * Constructs new {@link LoopEvent}.
     *
     * @param encounter   {@link Encounter} for this {@link Event}
     * @param id          unique {@link Event} identifier
     * @param name        {@link Event} name
     * @param start       remaining encounter time when first {@link Event} occurs
     * @param delay       event repeat delay
     * @param end         remaining encounter time after which {@link Event} doesn't occur anymore
     * @param advance     delay for announcement in advance
     * @param icon        {@link Event} icon
     * @param description {@link Event} description
     * @param sound       sound effect for this {@link Event}
     */
    public LoopEvent ( final Encounter encounter, final String id, final Supplier<String> name,
                       final long start, final long delay, final long end, final long advance,
                       final Function<Integer, Icon> icon, final Function<Integer, String> description, final SoundEffect sound )
    {
        super ( encounter, id, name, icon, description, advance, sound );
        this.start = start;
        this.end = end;
        this.delay = delay;
    }

    @Override
    public List<Announcement> announcements ()
    {
        final List<Announcement> announcements = new ArrayList<> ( 3 );
        long time = start;
        int cycle = 0;
        while ( time >= end )
        {
            announcements.add ( new Announcement ( time, icon ( cycle ), description ( cycle ), this ) );
            time -= delay;
            cycle++;
        }
        return announcements;
    }
}