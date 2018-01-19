package com.event.timer.data.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * {@link Event} settings.
 *
 * @author Mikle Garin
 */

@XStreamAlias ( "Event" )
public final class EventSettings implements Serializable
{
    /**
     * {@link Event} identifier.
     */
    @XStreamAsAttribute
    private String id;

    /**
     * Whether or not {@link Event} is enabled.
     */
    @XStreamAsAttribute
    private Boolean enabled;

    /**
     * Constructs new {@link EventSettings} for {@link Event} with the specified identifier.
     *
     * @param id {@link Event} identifier
     */
    public EventSettings ( final String id )
    {
        this.id = id;
    }

    /**
     * Returns {@link Event} identifier.
     *
     * @return {@link Event} identifier
     */
    public String id ()
    {
        return id;
    }

    /**
     * Returns whether or not this {@link Event} is enabled.
     *
     * @return {@code true} if this {@link Event} is enabled
     */
    public boolean enabled ()
    {
        return enabled == null || enabled;
    }

    /**
     * Sets whether or not this {@link Event} is enabled.
     *
     * @param enabled whether or not this {@link Event} is enabled
     */
    public void setEnabled ( final boolean enabled )
    {
        this.enabled = enabled;
    }
}