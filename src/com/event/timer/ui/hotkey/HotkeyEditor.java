package com.event.timer.ui.hotkey;

import com.alee.extended.hotkey.WebHotkeyField;
import com.alee.managers.hotkey.HotkeyData;
import com.event.timer.style.font.Fonts;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Custom editor for any specific {@link Hotkey}.
 *
 * @author Mikle Garin
 */

public class HotkeyEditor extends WebHotkeyField implements Fonts
{
    /**
     * Constructs new {@link HotkeyEditor}.
     *
     * @param hotkey {@link Hotkey} to edit
     */
    public HotkeyEditor ( final Hotkey hotkey )
    {
        super ();
        setFont ( smallFont );

        /**
         * Initial hotkey.
         */
        setHotkeyData ( hotkey.get () );

        /**
         * Hotkey edit listener.
         */
        addKeyListener ( new KeyAdapter ()
        {
            private int keys = 0;
            private int lastKey = -1;

            @Override
            public void keyPressed ( final KeyEvent e )
            {
                if ( lastKey != e.getKeyCode () )
                {
                    keys++;
                }
                lastKey = e.getKeyCode ();
            }

            @Override
            public void keyReleased ( final KeyEvent e )
            {
                keys--;
                if ( keys == 0 )
                {
                    lastKey = -1;

                    /**
                     * Saving hotkey only if proper new one is available.
                     */
                    final HotkeyData hotkeyData = getHotkeyData ();
                    if ( hotkeyData != null && hotkeyData.getKeyCode () != null && !Hotkeys.exists ( hotkey.id (), hotkeyData ) )
                    {
                        Hotkeys.change ( hotkey, hotkeyData );
                    }

                    /**
                     * Updating field.
                     */
                    setHotkeyData ( hotkey.get () );
                }
            }
        } );
    }
}