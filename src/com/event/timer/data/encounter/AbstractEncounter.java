package com.event.timer.data.encounter;

import com.alee.utils.parsing.DurationUnits;
import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.announcement.AnnouncementComparator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract {@link Encounter} with some convenience methods.
 *
 * @author Mikle Garin
 */

public abstract class AbstractEncounter implements Encounter
{
    /**
     * Unique {@link Encounter} identifier.
     */
    private final String id;

    /**
     * {@link Encounter} duration.
     */
    private final long duration;

    /**
     * {@link Encounter} name.
     */
    private final String name;

    /**
     * Constructs new {@link AbstractEncounter}.
     *
     * @param id       unique {@link Encounter} identifier
     * @param duration {@link Encounter} duration
     * @param name     {@link Encounter} name
     */
    public AbstractEncounter ( final String id, final long duration, final String name )
    {
        super ();
        this.id = id;
        this.duration = duration;
        this.name = name;
    }

    @Override
    public String id ()
    {
        return id;
    }

    @Override
    public long duration ()
    {
        return duration;
    }

    @Override
    public String name ()
    {
        return name;
    }

    @Override
    public List<Announcement> announcements ()
    {
        return events ().stream ()
                .flatMap ( event -> event.announcements ().stream () )
                .sorted ( new AnnouncementComparator () )
                .collect ( Collectors.toList () );
    }

    /**
     * Returns provided time in milliseconds.
     *
     * @param time time text representation
     * @return provided time in milliseconds
     */
    protected static long occursAt ( final String time )
    {
        return duration ( time );
    }

    /**
     * Returns provided time in milliseconds.
     *
     * @param time time text representation
     * @return provided time in milliseconds
     */
    protected static long startsAt ( final String time )
    {
        return duration ( time );
    }

    /**
     * Returns provided time in milliseconds.
     *
     * @param time time text representation
     * @return provided time in milliseconds
     */
    protected static long endsAt ( final String time )
    {
        return duration ( time );
    }

    /**
     * Returns provided time in milliseconds.
     *
     * @param time time text representation
     * @return provided time in milliseconds
     */
    protected static long happensEvery ( final String time )
    {
        return duration ( time );
    }

    /**
     * Returns provided time in milliseconds.
     *
     * @param time time text representation
     * @return provided time in milliseconds
     */
    protected static long notifyIn ( final String time )
    {
        return duration ( time );
    }

    /**
     * Returns provided time in milliseconds.
     *
     * @param time time text representation
     * @return provided time in milliseconds
     */
    protected static long duration ( final String time )
    {
        return DurationUnits.get ().fromString ( time );
    }
}