package com.event.timer.ui.settings;

import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;

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
        //                return !SettingsManager.get ( settingsKey, true ) ? CollectionUtils.asList ( "transparent" ) : null;
        return null;
    }

    /**
     * Updates panel states.
     */
    public void updateStates ()
    {
        DecorationUtils.fireStatesChanged ( this );
    }
}