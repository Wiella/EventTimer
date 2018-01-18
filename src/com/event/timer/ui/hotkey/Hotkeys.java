package com.event.timer.ui.hotkey;

import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.settings.SettingsManager;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that manages hotkeys.
 *
 * @author Mikle Garin
 */

public final class Hotkeys
{
    /**
     * Settings group name.
     * Can be used to flush previous hotkey settings.
     */
    private static final String group = "Hotkeys.v1";

    /**
     * Application hotkeys.
     */
    public static final Hotkey SETTINGS = new Hotkey ( "Settings", new HotkeyData ( true, false, true, KeyEvent.VK_S ) );
    public static final Hotkey START = new Hotkey ( "Start", new HotkeyData ( true, false, true, KeyEvent.VK_D ) );
    public static final Hotkey DECREASE_TIME = new Hotkey ( "AddTime", new HotkeyData ( true, false, true, KeyEvent.VK_A ) );
    public static final Hotkey EXIT = new Hotkey ( "Exit", new HotkeyData ( true, false, true, KeyEvent.VK_X ) );

    /**
     * Registered hotkeys.
     */
    private static Map<String, HotkeyData> hotkeys = new ConcurrentHashMap<> ( 5 );

    /**
     * Registered actions.
     */
    private static Map<String, HotKeyListener> actions = new ConcurrentHashMap<> ( 5 );

    /**
     * Hotkeys provider.
     */
    private static final Provider provider = Provider.getCurrentProvider ( true );

    /**
     * Returns whether or not specified hotkey is already used.
     *
     * @param id         hotkey identifier to exclude
     * @param hotkeyData {@link HotkeyData}
     * @return {@code true} if specified hotkey is already used, {@code false} otherwise
     */
    public static boolean exists ( final String id, final HotkeyData hotkeyData )
    {
        for ( final Map.Entry<String, HotkeyData> entry : hotkeys.entrySet () )
        {
            if ( !entry.getKey ().equals ( id ) && entry.getValue ().equals ( hotkeyData ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns current {@link HotkeyData} for the {@link Hotkey}.
     *
     * @param hotkey {@link Hotkey}
     * @return current {@link HotkeyData} for the {@link Hotkey}
     */
    public static HotkeyData get ( final Hotkey hotkey )
    {
        if ( hotkeys.containsKey ( hotkey.id () ) )
        {
            return hotkeys.get ( hotkey.id () );
        }
        else
        {
            return loadHotkey ( hotkey );
        }
    }

    /**
     * Registers brand new hotkey.
     *
     * @param hotkey {@link Hotkey}
     * @param action {@link HotKeyListener}
     */
    public static void register ( final Hotkey hotkey, final HotKeyListener action )
    {
        if ( !hotkeys.containsKey ( hotkey.id () ) )
        {
            final HotkeyData actualHotkey = loadHotkey ( hotkey );
            hotkeys.put ( hotkey.id (), actualHotkey );
            actions.put ( hotkey.id (), action );
            provider.register ( actualHotkey.getKeyStroke (), action );
        }
        else
        {
            throw new RuntimeException ( "Hotkey already exists: " + hotkey.id () );
        }
    }

    /**
     * Pauses listening for all hotkeys.
     */
    public static void pauseAll ()
    {
        if ( !hotkeys.isEmpty () )
        {
            provider.reset ();
        }
    }

    /**
     * Changes previously added hotkey.
     *
     * @param hotkey {@link Hotkey}
     * @param data   {@link HotkeyData}
     */
    public static void change ( final Hotkey hotkey, final HotkeyData data )
    {
        if ( hotkeys.containsKey ( hotkey.id () ) )
        {
            hotkeys.put ( hotkey.id (), data );
            saveHotkey ( hotkey, data );
        }
        else
        {
            throw new RuntimeException ( "Hotkey was not registered before: " + hotkey.id () );
        }
    }

    /**
     * Resumes listening for all hotkeys.
     */
    public static void resumeAll ()
    {
        if ( !hotkeys.isEmpty () )
        {
            for ( final Map.Entry<String, HotkeyData> entry : hotkeys.entrySet () )
            {
                final KeyStroke keyStroke = entry.getValue ().getKeyStroke ();
                final HotKeyListener action = actions.get ( entry.getKey () );
                provider.register ( keyStroke, action );
            }
        }
    }

    /**
     * Shuts down all hotkeys listeners.
     * Note that it won't be possible to use this manager anymore after this call.
     */
    public static void shutdown ()
    {
        if ( !hotkeys.isEmpty () )
        {
            provider.reset ();
        }
        provider.stop ();
    }

    /**
     * Returns {@link HotkeyData} loaded from the specified settings key.
     *
     * @param hotkey {@link Hotkey}
     * @return {@link HotkeyData} loaded from the specified settings key
     */
    private static HotkeyData loadHotkey ( final Hotkey hotkey )
    {
        final String defaultText = hotkey.hotkey ().getKeyStroke ().toString ();
        final String loadedText = SettingsManager.get ( group, hotkey.id (), defaultText );
        final KeyStroke keyStroke = KeyStroke.getKeyStroke ( loadedText );
        return new HotkeyData ( keyStroke );
    }

    /**
     * Saves specified {@link HotkeyData} under settings key.
     *
     * @param hotkey {@link Hotkey}
     * @param data   {@link HotkeyData} to save
     */
    private static void saveHotkey ( final Hotkey hotkey, final HotkeyData data )
    {
        final String textToSave = data.getKeyStroke ().toString ();
        SettingsManager.set ( group, hotkey.id (), textToSave );
    }
}