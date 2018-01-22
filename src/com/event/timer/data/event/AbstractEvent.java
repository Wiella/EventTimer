package com.event.timer.data.event;

import com.alee.managers.settings.SettingsManager;
import com.event.timer.data.encounter.Encounter;
import com.event.timer.data.notification.NotificationSettings;
import com.event.timer.style.icons.Icons;
import com.event.timer.style.sound.SoundEffect;
import com.event.timer.ui.notification.Notifications;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Abstract {@link Event} with some convenience methods.
 *
 * @author Mikle Garin
 */

public abstract class AbstractEvent implements Event
{
    /**
     * {@link Event}s settings.
     */
    private static final String EVENTS_KEY = "Events";

    /**
     * {@link Encounter} for this {@link Event}.
     */
    private final Encounter encounter;

    /**
     * Unique {@link Event} identifier.
     */
    private final String id;

    /**
     * {@link Event} name.
     */
    private final Supplier<String> name;

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
     * @param encounter   {@link Encounter} for this {@link Event}
     * @param id          unique {@link Event} identifier
     * @param name        {@link Event} name
     * @param description {@link Event} description
     * @param advance     delay for announcement in advance
     * @param sound       sound effect for this {@link Event}
     */
    public AbstractEvent ( final Encounter encounter, final String id, final Supplier<String> name,
                           final Function<Integer, String> description,
                           final long advance, final SoundEffect sound )
    {
        this ( encounter, id, name, cycle -> Icons.empty32, description, advance, sound );
    }

    /**
     * Constructs new {@link AbstractEvent}.
     *
     * @param encounter   {@link Encounter} for this {@link Event}
     * @param id          unique {@link Event} identifier
     * @param name        {@link Event} name
     * @param icon        {@link Event} icon
     * @param description {@link Event} description
     * @param advance     delay for announcement in advance
     * @param sound       sound effect for this {@link Event}
     */
    public AbstractEvent ( final Encounter encounter, final String id, final Supplier<String> name,
                           final Function<Integer, Icon> icon, final Function<Integer, String> description,
                           final long advance, final SoundEffect sound )
    {
        super ();
        this.encounter = encounter;
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.advance = advance;
        this.sound = sound;
    }

    @Override
    public Encounter encounter ()
    {
        return encounter;
    }

    @Override
    public String id ()
    {
        return id;
    }

    @Override
    public String name ()
    {
        return name.get ();
    }

    @Override
    public Icon icon ()
    {
        return icon.apply ( 0 );
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

    @Override
    public boolean enabled ()
    {
        return load ().enabled ();
    }

    @Override
    public void setEnabled ( final boolean enabled )
    {
        final EventSettings settings = load ();
        settings.setEnabled ( enabled );
        save ( settings );
    }

    @Override
    public NotificationSettings notification ()
    {
        final String notificationId = load ().notification ();
        return Notifications.get ( notificationId );
    }

    @Override
    public void setNotification ( final NotificationSettings notification )
    {
        final EventSettings settings = load ();
        settings.setNotification ( notification.id () );
        save ( settings );
    }

    /**
     * Returns loaded {@link EventSettings}.
     *
     * @return loaded {@link EventSettings}
     */
    private EventSettings load ()
    {
        final String group = encounter ().settingsGroup ();
        final List<EventSettings> all = SettingsManager.get ( group, EVENTS_KEY, new ArrayList<> ( 1 ) );
        return all.stream ().filter ( s -> id ().equals ( s.id () ) ).findFirst ().orElse ( new EventSettings ( id () ) );
    }

    /**
     * Saves {@link EventSettings}.
     *
     * @param settings {@link EventSettings} to save
     */
    private void save ( final EventSettings settings )
    {
        final String group = encounter ().settingsGroup ();
        final List<EventSettings> all = SettingsManager.get ( group, EVENTS_KEY, new ArrayList<> ( 1 ) );
        all.removeIf ( s -> id ().equals ( s.id () ) );
        all.add ( settings );
        SettingsManager.set ( group, EVENTS_KEY, all );
    }
}