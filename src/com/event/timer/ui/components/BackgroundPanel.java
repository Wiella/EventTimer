package com.event.timer.ui.components;

import com.alee.laf.panel.WebPanel;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.Stateful;

import java.util.ArrayList;
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
     * Settings key of the other {@link BackgroundPanel} this one is attached to.
     * Used to create distinct styles for some specific cases.
     */
    private final String attachmentKey;

    /**
     * Constructs new {@link BackgroundPanel}.
     *
     * @param id         {@link StyleId}
     * @param settingKey background setting key
     */
    public BackgroundPanel ( final StyleId id, final String settingKey, final String attachmentKey )
    {
        super ( id );
        this.settingsKey = settingKey;
        this.attachmentKey = attachmentKey;
        updateStates ();
    }

    @Override
    public List<String> getStates ()
    {
        final List<String> states = new ArrayList<> ( 2 );
        if ( settingsKey != null && !SettingsManager.get ( settingsKey, true ) )
        {
            states.add ( "transparent" );
        }
        if ( attachmentKey != null && !SettingsManager.get ( attachmentKey, true ) )
        {
            states.add ( "unattached" );
        }
        return states;
    }

    /**
     * Updates panel states.
     */
    public void updateStates ()
    {
        DecorationUtils.fireStatesChanged ( this );
    }
}