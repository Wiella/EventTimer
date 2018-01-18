package com.event.timer.style.sound;

import javazoom.jl.player.Player;

import java.io.InputStream;

/**
 * Abstract {@link SoundEffect} implementation for loading and playback.
 *
 * @author Mikle Garin
 */

public abstract class AbstractSoundEffect implements SoundEffect, Runnable
{
    /**
     * Class near which sound effect is located.
     */
    private final Class clazz;

    /**
     * Path to sound effect file relative to {@link #clazz}.
     */
    private final String resource;

    /**
     * Constructs new {@link AbstractSoundEffect}.
     *
     * @param clazz    class near which sound effect is located
     * @param resource path to sound effect file relative to class
     */
    public AbstractSoundEffect ( final Class clazz, final String resource )
    {
        super ();
        this.clazz = clazz;
        this.resource = resource;
    }

    @Override
    public final void play ()
    {
        final Thread playback = new Thread ( this );
        playback.setDaemon ( true );
        playback.start ();
    }

    @Override
    public void run ()
    {
        try
        {
            final InputStream fis = clazz.getResourceAsStream ( resource );
            final Player player = new Player ( fis );
            player.play ();
        }
        catch ( final Exception e )
        {
            e.printStackTrace ();
        }
    }
}