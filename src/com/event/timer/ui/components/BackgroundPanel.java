package com.event.timer.ui.components;

import com.alee.laf.panel.WebPanel;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CollectionUtils;

import java.util.List;

/**
 * Panel that can become transparent based on provided setting.
 *
 * @author Mikle Garin
 */

public final class BackgroundPanel extends WebPanel implements Stateful
{
    /**
     * Background setting key.
     * Note that this parameter is not received in the first {@link #getStates()} calls due to them being made in super constructors.
     * Although that is not important as the style (and states) are further updated when component becomes visible.
     */
    private final String settingsKey;

    /**
     * Constructs new {@link BackgroundPanel}.
     *
     * @param id         {@link StyleId}
     * @param settingKey background setting key
     */
    public BackgroundPanel ( final StyleId id, final String settingKey )
    {
        super ( id );
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
     * Updates panel states.
     */
    public void updateStates ()
    {
        DecorationUtils.fireStatesChanged ( this );
    }
}