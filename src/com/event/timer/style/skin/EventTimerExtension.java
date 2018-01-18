package com.event.timer.style.skin;

import com.alee.managers.style.XmlSkinExtension;

/**
 * Custom {@link com.alee.managers.style.SkinExtension} for {@link com.event.timer.EventTimer}.
 *
 * @author Mikle Garin
 */

public final class EventTimerExtension extends XmlSkinExtension
{
    public EventTimerExtension ()
    {
        super ( EventTimerExtension.class, "resources/extension.xml" );
    }
}