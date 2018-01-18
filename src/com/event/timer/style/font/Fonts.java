package com.event.timer.style.font;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Collection of {@link Font}s.
 *
 * @author Mikle Garin
 */

public interface Fonts
{
    /**
     * Common fonts.
     */
    public static final Font titleFont = createFont ( 35f );
    public static final Font smallFont = createFont ( 20f );
    public static final Font largeFont = createFont ( 30f );

    /**
     * Returns custom display {@link Font}.
     *
     * @param size {@link Font} size
     * @return custom display {@link Font}
     */
    public static Font createFont ( final float size )
    {
        try
        {
            final InputStream fontStream = Fonts.class.getResourceAsStream ( "resources/gw2.ttf" );
            final Font font = Font.createFont ( Font.TRUETYPE_FONT, fontStream );
            return font.deriveFont ( size );
        }
        catch ( FontFormatException | IOException e )
        {
            /**
             * Something went wrong.
             */
            e.printStackTrace ();

            /**
             * Exiting application.
             */
            System.exit ( 0 );
            return null;
        }
    }
}