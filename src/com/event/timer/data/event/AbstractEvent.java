package com.event.timer.data.event;

import com.event.timer.data.encounter.Encounter;
import com.event.timer.style.icons.Icons;
import com.event.timer.style.sound.SoundEffect;

import javax.swing.*;
import java.util.function.Function;

/**
 * Abstract {@link Event} with some convenience methods.
 *
 * @author Mikle Garin
 */

public abstract class AbstractEvent implements Event
{
    /**
     * Unique {@link Event} identifier.
     */
    private final String id;

    /**
     * {@link Encounter} for this {@link Event}.
     */
    private final Encounter encounter;

    /**
     * {@link Event} icon.
     */
    private final Function<Integer, Icon> icon;

    /**
     * {@link Event} description.
     */
    private final Function<Integer, String> description;

    /**
     * Delay for {@link Event} announcement in advance.
     */
    private final long advance;

    /**
     * Sound effect for this {@link Event}.
     */
    private final SoundEffect sound;

    /**
     * Constructs new {@link AbstractEvent}.
     *
     * @param id          unique {@link Event} identifier
     * @param encounter   {@link Encounter} for this {@link Event}
     * @param description {@link Event} description
     * @param advance     delay for announcement in advance
     * @param sound       sound effect for this {@link Event}
     */
    public AbstractEvent ( final String id, final Encounter encounter,
                           final Function<Integer, String> description,
                           final long advance, final SoundEffect sound )
    {
        this ( id, encounter, cycle -> Icons.empty32, description, advance, sound );
    }

    /**
     * Constructs new {@link AbstractEvent}.
     *
     * @param id          unique {@link Event} identifier
     * @param encounter   {@link Encounter} for this {@link Event}
     * @param icon        {@link Event} icon
     * @param description {@link Event} description
     * @param advance     delay for announcement in advance
     * @param sound       sound effect for this {@link Event}
     */
    public AbstractEvent ( final String id, final Encounter encounter,
                           final Function<Integer, Icon> icon, final Function<Integer, String> description,
                           final long advance, final SoundEffect sound )
    {
        super ();
        this.id = id;
        this.encounter = encounter;
        this.icon = icon;
        this.description = description;
        this.advance = advance;
        this.sound = sound;
    }

    @Override
    public String id ()
    {
        return id;
    }

    @Override
    public Icon icon ()
    {
        return icon.apply ( 0 );
    }

    @Override
    public Encounter encounter ()
    {
        return encounter;
    }

    /**
     * Returns icon for specific {@link Event} cycle.
     *
     * @param cycle {@link Event} cycle
     * @return icon for specific {@link Event} cycle
     */
    public Icon icon ( final int cycle )
    {
        return icon.apply ( cycle );
    }

    /**
     * Returns description for specific {@link Event} cycle.
     *
     * @param cycle {@link Event} cycle
     * @return description for specific {@link Event} cycle
     */
    public String description ( final int cycle )
    {
        return description.apply ( cycle );
    }

    @Override
    public long advance ()
    {
        return advance;
    }

    @Override
    public SoundEffect sound ()
    {
        return sound;
    }
}