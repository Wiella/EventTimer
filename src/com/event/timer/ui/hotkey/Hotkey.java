package com.event.timer.ui.hotkey;

import com.alee.managers.hotkey.HotkeyData;

/**
 * Single {@link Hotkey} used by application.
 *
 * @author Mikle Garin
 */

public final class Hotkey
{
    /**
     * Unique {@link Hotkey} identifier.
     */
    private final String id;

    /**
     * Default {@link HotkeyData}.
     */
    private final HotkeyData hotkey;

    /**
     * Constructs new {@link Hotkey}.
     *
     * @param id     unique {@link Hotkey} identifier
     * @param hotkey default {@link HotkeyData}
     */
    public Hotkey ( final String id, final HotkeyData hotkey )
    {
        this.id = id;
        this.hotkey = hotkey;
    }

    /**
     * Returns unique {@link Hotkey} identifier.
     *
     * @return unique {@link Hotkey} identifier
     */
    public String id ()
    {
        return id;
    }

    /**
     * Returns default {@link HotkeyData} for this {@link Hotkey}.
     *
     * @return default {@link HotkeyData} for this {@link Hotkey}
     */
    public HotkeyData hotkey ()
    {
        return hotkey;
    }

    /**
     * Returns current {@link HotkeyData} for this {@link Hotkey}.
     *
     * @return current {@link HotkeyData} for this {@link Hotkey}
     */
    public HotkeyData get ()
    {
        return Hotkeys.get ( this );
    }
}