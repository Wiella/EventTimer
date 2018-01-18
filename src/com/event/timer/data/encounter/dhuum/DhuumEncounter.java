package com.event.timer.data.encounter.dhuum;

import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.FormLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.encounter.AbstractEncounter;
import com.event.timer.data.event.Event;
import com.event.timer.data.event.LoopEvent;
import com.event.timer.data.event.SingleEvent;
import com.event.timer.style.color.Colors;
import com.event.timer.style.font.Fonts;
import com.event.timer.style.format.TimerUnits;
import com.event.timer.style.icons.Icons;
import com.event.timer.style.skin.Styles;
import com.event.timer.style.sound.SoundEffects;

import javax.swing.*;
import java.util.List;

/**
 * Dhuum encounter timings.
 *
 * @author Mikle Garin
 */

public final class DhuumEncounter extends AbstractEncounter implements Icons, Colors, Fonts, SoundEffects
{
    /**
     * todo Sub-encounter for last phase?
     *
     * Last phase: 1m 45s
     * Seals warning: 1m 15s (~5s going up, ~25s catching balls)
     * DPS phases: 20s
     * Seals: 7~8s
     * Enrage: 0s
     */

    /**
     * Settings group name.
     * Can be used to flush previous settings of this encounter.
     */
    private static final String group = "DhuumEncounter.v1";

    /**
     * Constructs new {@link DhuumEncounter} encounter.
     */
    public DhuumEncounter ()
    {
        super (
                duration ( "10m" ),
                "{Dhuum:c(" + Colors.red + ")}"
        );
    }

    @Override
    public List<Event> events ()
    {
        return CollectionUtils.asList (
                /**
                 * Greens.
                 */
                new LoopEvent (
                        startsAt ( "9m 30s" ),
                        happensEvery ( "30s" ),
                        endsAt ( "30s" ),
                        notifyIn ( "10s" ),
                        this::marker,
                        cycle -> "{" + player ( cycle ) + ":c(" + green + ")}",
                        tickSound
                ),

                /**
                 * Boss spawn.
                 */
                new SingleEvent (
                        occursAt ( "8m" ),
                        notifyIn ( "5s" ),
                        boss32,
                        "{Boss spawns:c(" + red + ")}",
                        springSound
                ),

                /**
                 * Teleports.
                 */
                new LoopEvent (
                        startsAt ( "7m 10s" ),
                        happensEvery ( "80s" ),
                        notifyIn ( "5s" ),
                        cycle -> waypoint32,
                        cycle -> "{Teleport:c(" + blue + ")}",
                        springSound
                ),

                /**
                 * Middles.
                 */
                new LoopEvent (
                        startsAt ( "6m 25s" ),
                        happensEvery ( "80s" ),
                        notifyIn ( "13s" ),
                        cycle -> boss32,
                        cycle -> "{Middle:c(" + red + ")}",
                        springSound
                ),

                /**
                 * Enrage.
                 */
                new SingleEvent (
                        occursAt ( "0s" ),
                        notifyIn ( "10s" ),
                        enrage32,
                        "{Enrage:c(" + red + ")}",
                        echoedDingSound
                )
        );
    }

    @Override
    public JComponent settings ()
    {
        final WebPanel settings = new WebPanel ( StyleId.panelTransparent, new FormLayout ( false, true, 5, 5 ) );

        /**
         * Players for greens.
         */

        final WebLabel greensTitle = new WebLabel ( StyleId.labelShadow, "Greens:" );
        greensTitle.setFont ( smallFont );
        settings.add ( greensTitle );

        final WebTextField green1 = new WebTextField ();
        green1.setFont ( smallFont );
        green1.setHorizontalAlignment ( WebTextField.CENTER );
        green1.registerSettings ( group, "green1", "Green 1" );
        green1.onChange ( ( c, e ) -> green1.saveSettings () );

        final WebTextField green2 = new WebTextField ();
        green2.setFont ( smallFont );
        green2.setHorizontalAlignment ( WebTextField.CENTER );
        green2.registerSettings ( group, "green2", "Green 2" );
        green2.onChange ( ( c, e ) -> green2.saveSettings () );

        final WebTextField green3 = new WebTextField ();
        green3.setFont ( smallFont );
        green3.setHorizontalAlignment ( WebTextField.CENTER );
        green3.registerSettings ( group, "green3", "Green 3" );
        green3.onChange ( ( c, e ) -> green3.saveSettings () );

        settings.add ( new GroupPanel ( GroupingType.fillAll, 5, true, green1, green2, green3 ) );

        /**
         * Feed customization.
         */

        final WebLabel feedTitle = new WebLabel ( StyleId.labelShadow, "Announcements feed", WebLabel.CENTER );
        feedTitle.setFont ( smallFont );
        settings.add ( feedTitle, FormLayout.LINE );

        final WebPanel feed = new WebPanel ( StyleId.panelTransparent, new VerticalFlowLayout ( 0, 5, true, false ) );
        feed.setPreferredWidth ( 0 );

        final Runnable updateFeed = () -> {
            feed.removeAll ();
            for ( final Announcement announcement : announcements () )
            {
                final WebStyledLabel announcementView = new WebStyledLabel ( Styles.announcementLabel );
                announcementView.setHorizontalAlignment ( WebStyledLabel.CENTER );
                announcementView.setFont ( smallFont );
                announcementView.setIcon ( announcement.icon () );
                announcementView.setText ( TimerUnits.get ().toString ( announcement.time () ) + ": " + announcement.text () );
                feed.add ( announcementView );
            }
            feed.revalidate ();
            feed.repaint ();
        };
        updateFeed.run ();

        final WebScrollPane feedScroll = new WebScrollPane ( StyleId.scrollpaneTransparentHovering, feed );
        feedScroll.setPreferredHeight ( 300 );
        feedScroll.getVerticalScrollBar ().setUnitIncrement ( 25 );
        feedScroll.getVerticalScrollBar ().setBlockIncrement ( 50 );
        settings.add ( feedScroll, FormLayout.LINE );

        /**
         * View update actions.
         */

        green1.onChange ( ( c, e ) -> SwingUtils.invokeLater ( updateFeed ) );
        green2.onChange ( ( c, e ) -> SwingUtils.invokeLater ( updateFeed ) );
        green3.onChange ( ( c, e ) -> SwingUtils.invokeLater ( updateFeed ) );

        return settings;
    }

    /**
     * Returns player name for the specified event cycle.
     *
     * @param cycle event cycle
     * @return player name for the specified event cycle
     */
    private String player ( final int cycle )
    {
        final int number = 1 + ( cycle % 3 );
        final String player = SettingsManager.get ( group, "green" + number, "Green " + number );
        return TextUtils.notEmpty ( player ) ? player : "Green " + number;
    }

    /**
     * Returns marker for the specified event cycle.
     *
     * @param cycle event cycle
     * @return marker for the specified event cycle
     */
    private Icon marker ( final int cycle )
    {
        final int number = 1 + ( cycle % 7 );
        switch ( number )
        {
            case 1:
                return arrowMarker;

            case 2:
                return circleMarker;

            case 3:
                return heartMarker;

            case 4:
                return squareMarker;

            case 5:
                return starMarker;

            case 6:
                return spiralMarker;

            case 7:
                return triangleMarker;

            default:
                return empty32;
        }
    }
}