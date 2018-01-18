package com.event.timer.style.icons;

import com.alee.utils.CollectionUtils;
import com.alee.utils.ImageUtils;

import javax.swing.*;
import java.util.List;

/**
 * Collection of {@link Icon}s.
 *
 * @author Mikle Garin
 */

public interface Icons
{
    /**
     * Empty icons.
     */
    public static final Icon empty32 = ImageUtils.createEmptyIcon ( 1, 32 );

    /**
     * UI icons.
     */
    public static final Icon settings32 = createPNG ( "settings.png", 32 );
    public static final Icon settingsHover32 = createPNG ( "settings-hover.png", 32 );

    /**
     * Marker icons.
     */
    public static final Icon arrowMarker = createPNG ( "arrow-marker.png", 32 );
    public static final Icon circleMarker = createPNG ( "circle-marker.png", 32 );
    public static final Icon heartMarker = createPNG ( "heart-marker.png", 32 );
    public static final Icon squareMarker = createPNG ( "square-marker.png", 32 );
    public static final Icon starMarker = createPNG ( "star-marker.png", 32 );
    public static final Icon spiralMarker = createPNG ( "spiral-marker.png", 32 );
    public static final Icon triangleMarker = createPNG ( "triangle-marker.png", 32 );
    public static final Icon crossMarker = createPNG ( "cross-marker.png", 32 );

    /**
     * Other icons.
     */
    public static final Icon world32 = createPNG ( "world.png", 32 );
    public static final Icon announcement32 = createPNG ( "announcement.png", 32 );
    public static final Icon notes32 = createPNG ( "notes.png", 32 );
    public static final Icon sword32 = createPNG ( "sword.png", 32 );
    public static final Icon swords32 = createPNG ( "swords.png", 32 );
    public static final Icon checkpoint32 = createPNG ( "checkpoint.png", 32 );
    public static final Icon flag32 = createPNG ( "flag.png", 32 );
    public static final Icon boss32 = createPNG ( "boss.png", 32 );
    public static final Icon downed32 = createPNG ( "downed.png", 32 );
    public static final Icon downstate32 = createPNG ( "downstate.png", 32 );
    public static final Icon waypoint32 = createPNG ( "waypoint.png", 32 );
    public static final Icon dragon32 = createPNG ( "dragon.png", 32 );
    public static final Icon enrage32 = createPNG ( "enrage.png", 32 );
    public static final Icon laurel32 = createPNG ( "laurel.png", 32 );
    public static final Icon water32 = createPNG ( "water.png", 32 );
    public static final Icon swirl32 = createPNG ( "swirl.png", 32 );
    public static final Icon up32 = createPNG ( "up.png", 32 );
    public static final Icon down32 = createPNG ( "down.png", 32 );


    /**
     * List of all colors.
     */
    public static final List<Icon> allIcons = CollectionUtils.asList (
            arrowMarker,
            circleMarker,
            heartMarker,
            squareMarker,
            starMarker,
            spiralMarker,
            triangleMarker,
            crossMarker,
            world32,
            announcement32,
            notes32,
            sword32,
            swords32,
            checkpoint32,
            flag32,
            boss32,
            downed32,
            downstate32,
            waypoint32,
            dragon32,
            enrage32,
            laurel32,
            water32,
            swirl32,
            up32,
            down32
    );


    /**
     * Returns scaled {@link Icon}.
     *
     * @param path {@link Icon} path relative to this interface
     * @param size size to scale {@link Icon} to
     * @return scaled {@link Icon}
     */
    public static Icon createPNG ( final String path, final int size )
    {
        return ImageUtils.createPreviewIcon ( new ImageIcon ( Icons.class.getResource ( "resources/" + path ) ), size );
    }
}