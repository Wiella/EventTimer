package com.event.timer.data.announcement;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * {@link Announcement} settings.
 *
 * @author Mikle Garin
 */

@XStreamAlias ( "Announcement" )
public final class AnnouncementSettings implements Serializable
{
    /**
     * {@link Announcement} identifier.
     */
    @XStreamAsAttribute
    private String id;

    /**
     * Whether or not {@link Announcement} is enabled.
     */
    @XStreamAsAttribute
    private Boolean enabled;

    /**
     * Constructs new {@link AnnouncementSettings} for {@link Announcement} with the specified identifier.
     *
     * @param id {@link Announcement} identifier
     */
    public AnnouncementSettings ( final String id )
    {
        this.id = id;
    }

    /**
     * Returns {@link Announcement} identifier.
     *
     * @return {@link Announcement} identifier
     */
    public String id ()
    {
        return id;
    }

    /**
     * Returns whether or not this {@link Announcement} is enabled.
     *
     * @return {@code true} if this {@link Announcement} is enabled
     */
    public boolean enabled ()
    {
        return enabled == null || enabled;
    }

    /**
     * Sets whether or not this {@link Announcement} is enabled.
     *
     * @param enabled whether or not this {@link Announcement} is enabled
     */
    public void setEnabled ( final boolean enabled )
    {
        this.enabled = enabled;
    }
}