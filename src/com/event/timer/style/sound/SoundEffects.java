package com.event.timer.style.sound;

import com.alee.utils.CollectionUtils;

import java.util.List;

/**
 * Collection of {@link SoundEffect}s.
 *
 * @author Mikle Garin
 * @see <a href="https://notificationsounds.com">Sound effects</a>
 */

public interface SoundEffects
{
    /**
     * Common sound effects.
     */
    public static final SoundEffect springSound = new SpringSoundEffect ();
    public static final SoundEffect tickSound = new TickSoundEffect ();
    public static final SoundEffect decaySound = new DecaySoundEffect ();
    public static final SoundEffect popSound = new PopSoundEffect ();
    public static final SoundEffect knobSound = new KnobSoundEffect ();
    public static final SoundEffect echoedDingSound = new EchoedDingSoundEffect ();
    public static final SoundEffect glassyClickSound = new GlassyClickSoundEffect ();
    public static final SoundEffect bellSound = new BellSoundEffect ();
    public static final SoundEffect plastikSound = new PlastikSoundEffect ();
    public static final SoundEffect christmasBellsSound = new ChristmasBellsSoundEffect ();
    public static final SoundEffect breakForthSound = new BreakForthSoundEffect ();
    public static final SoundEffect gracefulSound = new GracefulSoundEffect ();

    /**
     * List of all sound effects.
     */
    public static final List<SoundEffect> allSoundEffects = CollectionUtils.asList (
            springSound,
            tickSound,
            decaySound,
            popSound,
            knobSound,
            echoedDingSound,
            glassyClickSound,
            bellSound,
            plastikSound,
            christmasBellsSound,
            breakForthSound,
            gracefulSound
    );
}