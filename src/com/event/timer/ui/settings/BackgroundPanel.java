package com.event.timer.ui.settings;

import com.alee.laf.panel.WebPanel;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.CollectionUtils;

import java.util.List;

/**
 * @author Mikle Garin
 */

public final class BackgroundPanel extends WebPanel implements Stateful
{
    /**
     * Background setting key.
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