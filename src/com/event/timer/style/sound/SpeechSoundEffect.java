package com.event.timer.style.sound;

import com.alee.managers.settings.SettingsManager;
import com.darkprograms.speech.synthesiser.SynthesiserV2;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mikle Garin
 */

public class SpeechSoundEffect implements SoundEffect, Runnable
{
    /**
     * Text to read.
     */
    private String text;

    /**
     * Constructs new {@link SpeechSoundEffect}.
     *
     * @param text text to read
     */
    public SpeechSoundEffect ( final String text )
    {
        super ();
        this.text = text;
    }

    @Override
    public void play ()
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
            final SynthesiserV2 synthesizer = new SynthesiserV2 ( "AIzaSyAE1Q2bNKOTAVY4ArBuVxMjkFwgyPf8FRA" );
            synthesizer.setLanguage ( SettingsManager.get ( "VerbalSpeaker", "en" ) );
            synthesizer.setSpeed ( 0.9d );

            final InputStream mp3data = synthesizer.getMP3Data ( text );

            final AdvancedPlayer player = new AdvancedPlayer ( mp3data );
            player.play ();
        }
        catch ( IOException | JavaLayerException e )
        {
            //Print the exception ( we want to know , not hide below our finger , like many developers do...)
            e.printStackTrace ();
        }
    }
}