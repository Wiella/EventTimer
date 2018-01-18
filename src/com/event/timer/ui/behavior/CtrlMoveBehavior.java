package com.event.timer.ui.behavior;

import com.alee.extended.behavior.ComponentMoveBehavior;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * {@link ComponentMoveBehavior} extension.
 *
 * @author Mikle Garin
 */

public final class CtrlMoveBehavior extends ComponentMoveBehavior
{
    /**
     * Constructs new {@link CtrlMoveBehavior}.
     *
     * @param gripper window component that will act as gripper
     */
    public CtrlMoveBehavior ( final Component gripper )
    {
        super ( gripper );
    }

    @Override
    public void mousePressed ( final MouseEvent e )
    {
        if ( SwingUtils.isCtrl ( e ) )
        {
            super.mousePressed ( e );
        }
    }
}