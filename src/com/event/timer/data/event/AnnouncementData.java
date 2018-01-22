package com.event.timer.data.event;

import com.event.timer.data.announcement.Announcement;
import com.event.timer.style.sound.SoundEffect;

import javax.swing.*;

/**
 * Represents {@link Announcement} data.
 * An interface can be separated in future to expand possible implementations.
 *
 * @author Mikle Garin
 */

public final class AnnouncementData
{
    /**
     * {@link Announcement} icon.
     */
    private final Icon icon;

    /**
     * {@link Announcement} styled text.
     */
    private final String text;

    /**
     * {@link Announcement} speech.
     */
    private final String speech;

    /**
     * {@link SoundEffect} for {@link Announcement}.
     */
    private final SoundEffect sound;

    /**
     * Constructs new {@link AnnouncementData}.
     *
     * @param icon  {@link Announcement} icon
     * @param text  {@link Announcement} text
     * @param sound {@link SoundEffect} for {@link Announcement}
     */
    public AnnouncementData ( final Icon icon, final String text, final String speech, final SoundEffect sound )
    {
        this.icon = icon;
        this.text = text;
        this.speech = speech;
        this.sound = sound;
    }

    /**
     * Returns {@link Icon} for {@link Announcement}.
     *
     * @return {@link Icon} for {@link Announcement}
     */
    public Icon icon ()
    {
        return icon;
    }

    /**
     * Returns text for {@link Announcement}.
     *
     * @return text for {@link Announcement}
     */
    public String text ()
    {
        return text;
    }

    /**
     * Returns speech for {@link Announcement}.
     *
     * @return speech for {@link Announcement}
     */
    public String speech ()
    {
        return speech;
    }

    /**
     * Returns {@link SoundEffect} for {@link Announcement}.
     *
     * @return {@link SoundEffect} for {@link Announcement}
     */
    public SoundEffect sound ()
    {
        return sound;
    }
}