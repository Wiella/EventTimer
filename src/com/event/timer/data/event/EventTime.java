package com.event.timer.data.event;

import com.event.timer.data.announcement.Announcement;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information about single {@link Event} timings.
 * An interface can be separated in future to expand possible implementations.
 *
 * @author Mikle Garin
 */

public final class EventTime
{
    /**
     * Remaining encounter time when first {@link Event} occurs.
     */
    private long start;

    /**
     * Delay between {@link Event} occurrences.
     */
    private long delay;

    /**
     * Latest time at which {@link Event} can occur.
     */
    private long end;

    /**
     * Delay for {@link Event} announcement in advance.
     */
    private long advance;

    /**
     * Constructs one-time {@link Event}.
     *
     * @param time    remaining encounter time when {@link Event} occurs
     * @param advance delay for {@link Event} announcement in advance
     */
    public EventTime ( final long time, final long advance )
    {
        this ( time, 0, time, advance );
    }

    /**
     * Constructs repeatable {@link Event}.
     *
     * @param start   remaining encounter time when first {@link Event} occurs
     * @param delay   delay between {@link Event} occurrences
     * @param advance delay for {@link Event} announcement in advance
     */
    public EventTime ( final long start, final long delay, final long advance )
    {
        this ( start, delay, 0L, advance );
    }

    /**
     * Constructs repeatable {@link Event}.
     *
     * @param start   remaining encounter time when first {@link Event} occurs
     * @param delay   delay between {@link Event} occurrences
     * @param end     latest time at which {@link Event} can occur
     * @param advance delay for {@link Event} announcement in advance
     */
    public EventTime ( final long start, final long delay, final long end, final long advance )
    {
        super ();
        this.start = start;
        this.delay = delay;
        this.end = end;
        this.advance = advance;
    }

    /**
     * Returns delay used for displaying {@link Announcement}s in advance.
     *
     * @return delay used for displaying {@link Announcement}s in advance
     */
    public long advance ()
    {
        return advance;
    }

    /**
     * Returns {@link List} of times for each {@link Event} cycle.
     *
     * @return {@link List} of times for each {@link Event} cycle
     */
    public List<Long> times ()
    {
        final List<Long> times = new ArrayList<> ( 3 );
        if ( delay > 0 )
        {
            long time = start;
            while ( time >= end )
            {
                times.add ( time );
                time -= delay;
            }
        }
        else
        {
            times.add ( start );
        }
        return times;
    }
}