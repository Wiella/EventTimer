package com.event.timer.data.encounter.dhuum;

import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.FormLayout;
import com.alee.extended.layout.LineLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.swing.MouseButton;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Cached {@link List} of {@link Event}s.
     */
    private transient List<Event> events;

    /**
     * Constructs new {@link DhuumEncounter} encounter.
     */
    public DhuumEncounter ()
    {
        super ( "dhuum", duration ( "10m" ), "{Dhuum:c(" + Colors.red + ")}" );
    }

    @Override
    public List<Event> events ()
    {
        if ( events == null )
        {
            synchronized ( DhuumEncounter.this )
            {
                if ( events == null )
                {
                    events = new ImmutableList<> (
                            /**
                             * Greens.
                             */
                            new LoopEvent (
                                    "green",
                                    DhuumEncounter.this,
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
                                    "boss",
                                    DhuumEncounter.this,
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
                                    "teleport",
                                    DhuumEncounter.this,
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
                                    "middle",
                                    DhuumEncounter.this,
                                    startsAt ( "6m 25s" ),
                                    happensEvery ( "80s" ),
                                    notifyIn ( "13s" ),
                                    cycle -> downed32,
                                    cycle -> "{Middle:c(" + red + ")}",
                                    springSound
                            ),

                            /**
                             * Enrage.
                             */
                            new SingleEvent (
                                    "enrage",
                                    DhuumEncounter.this,
                                    occursAt ( "0s" ),
                                    notifyIn ( "10s" ),
                                    enrage32,
                                    "{Enrage:c(" + red + ")}",
                                    echoedDingSound
                            )
                    );
                }
            }
        }
        return events;
    }

    @Override
    public String settingsGroup ()
    {
        return group;
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
        green1.registerSettings ( settingsGroup (), "green1", "Green 1" );
        green1.onChange ( ( c, e ) -> green1.saveSettings () );

        final WebTextField green2 = new WebTextField ();
        green2.setFont ( smallFont );
        green2.setHorizontalAlignment ( WebTextField.CENTER );
        green2.registerSettings ( settingsGroup (), "green2", "Green 2" );
        green2.onChange ( ( c, e ) -> green2.saveSettings () );

        final WebTextField green3 = new WebTextField ();
        green3.setFont ( smallFont );
        green3.setHorizontalAlignment ( WebTextField.CENTER );
        green3.registerSettings ( settingsGroup (), "green3", "Green 3" );
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

        final Map<Announcement, WebStyledLabel> announcements = new HashMap<> ( 20 );
        final Runnable updateFeed = () -> {
            feed.removeAll ();
            announcements.clear ();
            for ( final Announcement announcement : announcements () )
            {
                final WebStyledLabel announcementView = new WebStyledLabel ( Styles.announcementLabel );
                announcementView.setHorizontalAlignment ( WebStyledLabel.CENTER );
                announcementView.setFont ( smallFont );
                announcementView.setEnabled ( announcement.enabled () );
                announcementView.setIcon ( announcement.icon () );
                announcementView.setText ( TimerUnits.get ().toString ( announcement.time () ) + ": " + announcement.text () );
                announcementView.onMousePress ( MouseButton.left, e -> {
                    announcementView.setEnabled ( !announcementView.isEnabled () );
                    announcement.setEnabled ( announcementView.isEnabled () );
                } );
                feed.add ( announcementView );
                announcements.put ( announcement, announcementView );
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

        final WebPanel eventControls = new WebPanel ( StyleId.panelTransparent, new LineLayout ( LineLayout.HORIZONTAL, 5 ) );
        eventControls.setPadding ( 10, 0, 0, 0 );

        final WebStyledLabel byEventsLabel = new WebStyledLabel ( StyleId.styledlabelShadow, "By events:" );
        byEventsLabel.setFont ( smallFont );
        eventControls.add ( byEventsLabel, LineLayout.MIDDLE );

        for ( final Event event : events () )
        {
            // todo Initial enabled state
            final WebLabel eventView = new WebLabel ( event.icon () );
            eventView.onMousePress ( MouseButton.left, e -> {
                eventView.setEnabled ( !eventView.isEnabled () );
                event.announcements ().forEach ( a -> a.setEnabled ( eventView.isEnabled () ) );
                updateFeed.run ();
            } );
            eventControls.add ( eventView, LineLayout.MIDDLE );
        }

        settings.add ( eventControls, FormLayout.LINE );

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
        final String player = SettingsManager.get ( settingsGroup (), "green" + number, "Green " + number );
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