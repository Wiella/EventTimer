package com.event.timer.ui.components;

import com.alee.laf.separator.WebSeparator;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CollectionUtils;

import java.util.List;

/**
 * Separator that can become transparent based on provided setting.
 *
 * @author Mikle Garin
 */

public final class BackgroundSeparator extends WebSeparator implements Stateful
{
    /**
     * Background setting key.
     * Note that this parameter is not received in the first {@link #getStates()} calls due to them being made in super constructors.
     * Although that is not important as the style (and states) are further updated when component becomes visible.
     */
    private final String settingsKey;

    /**
     * Constructs new {@link BackgroundSeparator}.
     *
     * @param id          {@link StyleId}
     * @param orientation separator orientation
     * @param settingKey  background setting key
     */
    public BackgroundSeparator ( final StyleId id, final int orientation, final String settingKey )
    {
        super ( id, orientation );
        this.settingsKey = settingKey;
        updateStates ();
    }

    @Override
    public List<String> getStates ()
    {
        final boolean transparent = settingsKey != null && !SettingsManager.get ( settingsKey, true );
        return transparent ? CollectionUtils.asList ( "transparent" ) : null;
    }

    /**
     * Updates separator states.
     */
    public void updateStates ()
    {
        DecorationUtils.fireStatesChanged ( this );
    }
}