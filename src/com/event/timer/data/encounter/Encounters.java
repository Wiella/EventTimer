package com.event.timer.data.encounter;

import com.alee.utils.CollectionUtils;
import com.event.timer.data.encounter.dhuum.DhuumEncounter;

import java.util.List;

/**
 * @author Mikle Garin
 */

public final class Encounters
{
    private static List<Encounter> encounters;

    public static List<Encounter> all ()
    {
        if ( encounters == null )
        {
            synchronized ( Encounters.class )
            {
                if ( encounters == null )
                {
                    encounters = CollectionUtils.asList ( new DhuumEncounter () );
                }
            }
        }
        return encounters;
    }
}