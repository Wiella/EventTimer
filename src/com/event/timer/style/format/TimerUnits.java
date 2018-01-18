package com.event.timer.style.format;

import com.alee.utils.parsing.AbstractUnits;

/**
 * {@link AbstractUnits} implementation that would only use minutes and seconds unlike {@link com.alee.utils.parsing.DurationUnits}.
 *
 * @author Mikle Garin
 */

public final class TimerUnits extends AbstractUnits
{
    /**
     * Static {@link TimerUnits} instance.
     */
    private static TimerUnits instance;

    /**
     * Returns single {@link TimerUnits} instance.
     *
     * @return single {@link TimerUnits} instance
     */
    public static TimerUnits get ()
    {
        if ( instance == null )
        {
            instance = new TimerUnits ();
        }
        return instance;
    }

    /**
     * Constructs new {@link TimerUnits}.
     */
    private TimerUnits ()
    {
        super (
                new AbstractUnits.Unit ( 60000L, 60L, "m" ),
                new AbstractUnits.Unit ( 1000L, 60L, "s" )
        );
    }
}