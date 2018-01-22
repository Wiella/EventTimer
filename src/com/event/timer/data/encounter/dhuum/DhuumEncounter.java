package com.event.timer.data.encounter.dhuum;

import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.FormLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.collection.ImmutableList;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.UnselectableButtonGroup;
import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.encounter.AbstractEncounter;
import com.event.timer.data.event.Event;
import com.event.timer.data.event.LoopEvent;
import com.event.timer.data.event.SingleEvent;
import com.event.timer.data.notification.NotificationSettings;
import com.event.timer.style.color.Colors;
import com.event.timer.style.format.TimerUnits;
import com.event.timer.style.icons.Icons;
import com.event.timer.style.skin.Styles;
import com.event.timer.style.sound.SoundEffects;
import com.event.timer.ui.notification.Notifications;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dhuum encounter timings.
 *
 * @author Mikle Garin
 */

public final class DhuumEncounter extends AbstractEncounter implements Icons, Colors, SoundEffects
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
    private static final String group = "DhuumEncounter.v2";

    /**
     * Default green player markers.
     */
    private static final ImmutableList<Icon> markers = new ImmutableList<> (
            arrowMarker,
            circleMarker,
            heartMarker,
            squareMarker,
            starMarker,
            spiralMarker,
            triangleMarker
    );

    /**
     * Cached {@link List} of {@link Event}s.
     */
    private transient List<Event> events;

    /**
     * Settings variables.
     */
    private transient Runnable updateEventsFeed;
    private transient Runnable updateAnnouncementsFeed;

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
                             * First player greens.
                             */
                            new LoopEvent (
                                    DhuumEncounter.this,
                                    "green1", () -> "First green ( " + player ( 1 ) + " )",
                                    startsAt ( "9m 30s" ),
                                    happensEvery ( "1m 30s" ),
                                    notifyIn ( "10s" ),
                                    cycle -> marker ( 1, cycle ),
                                    cycle -> "{" + player ( 1 ) + ":c(" + green + ")}",
                                    tickSound
                            ),

                            /**
                             * Second player greens.
                             */
                            new LoopEvent (
                                    DhuumEncounter.this,
                                    "green2", () -> "Second green ( " + player ( 2 ) + " )",
                                    startsAt ( "9m" ),
                                    happensEvery ( "1m 30s" ),
                                    notifyIn ( "10s" ),
                                    cycle -> marker ( 2, cycle ),
                                    cycle -> "{" + player ( 2 ) + ":c(" + green + ")}",
                                    tickSound
                            ),

                            /**
                             * Third player greens.
                             */
                            new LoopEvent (
                                    DhuumEncounter.this,
                                    "green3", () -> "Third green ( " + player ( 3 ) + " )",
                                    startsAt ( "8m 30s" ),
                                    happensEvery ( "1m 30s" ),
                                    notifyIn ( "10s" ),
                                    cycle -> marker ( 3, cycle ),
                                    cycle -> "{" + player ( 3 ) + ":c(" + green + ")}",
                                    tickSound
                            ),

                            /**
                             * Boss spawn.
                             */
                            new SingleEvent (
                                    DhuumEncounter.this,
                                    "boss", () -> "Boss spawn",
                                    occursAt ( "7m 50s" ),
                                    notifyIn ( "10s" ),
                                    boss32,
                                    "{Boss spawns:c(" + red + ")}",
                                    springSound
                            ),

                            /**
                             * Teleports.
                             */
                            new LoopEvent (
                                    DhuumEncounter.this,
                                    "teleport", () -> "Teleport AoE",
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
                                    DhuumEncounter.this,
                                    "middle", () -> "Middle AoE",
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
                                    DhuumEncounter.this,
                                    "enrage", () -> "Boss enrage",
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
        createGreenPlayersSettings ( settings );
        createFeedSettings ( settings );
        return settings;
    }

    /**
     * Creates settings UI for players doing greens.
     *
     * @param settings settings panel
     */
    private void createGreenPlayersSettings ( final WebPanel settings )
    {
        final WebLabel greensTitle = new WebLabel ( StyleId.labelShadow, "Greens:" );
        settings.add ( greensTitle );

        final WebTextField green1 = new WebTextField ();
        green1.setHorizontalAlignment ( WebTextField.CENTER );
        green1.registerSettings ( settingsGroup (), "green1", "Green 1" );
        green1.onChange ( ( c, e ) -> SwingUtils.invokeLater ( () -> {
            green1.saveSettings ();
            updateEventsFeed.run ();
            updateAnnouncementsFeed.run ();
        } ) );

        final WebTextField green2 = new WebTextField ();
        green2.setHorizontalAlignment ( WebTextField.CENTER );
        green2.registerSettings ( settingsGroup (), "green2", "Green 2" );
        green2.onChange ( ( c, e ) -> SwingUtils.invokeLater ( () -> {
            green2.saveSettings ();
            updateEventsFeed.run ();
            updateAnnouncementsFeed.run ();
        } ) );

        final WebTextField green3 = new WebTextField ();
        green3.setHorizontalAlignment ( WebTextField.CENTER );
        green3.registerSettings ( settingsGroup (), "green3", "Green 3" );
        green3.onChange ( ( c, e ) -> SwingUtils.invokeLater ( () -> {
            green3.saveSettings ();
            updateEventsFeed.run ();
            updateAnnouncementsFeed.run ();
        } ) );

        settings.add ( new GroupPanel ( GroupingType.fillAll, 5, true, green1, green2, green3 ) );
    }

    /**
     * Creates settings UI for events and announcements feed customization.
     *
     * @param settings settings panel
     */
    private void createFeedSettings ( final WebPanel settings )
    {
        final UnselectableButtonGroup group = new UnselectableButtonGroup ( false );

        final CardLayout layout = new CardLayout ( 0, 0 );
        final WebPanel feedContent = new WebPanel ( StyleId.panelTransparent, layout );
        feedContent.add ( createEventsFeedSettings (), "events" );
        feedContent.add ( createAnnouncementsFeedSettings (), "announcements" );

        final WebStyledLabel feedTitle = new WebStyledLabel ( StyleId.styledlabelShadow, "Customize feed:" );

        final WebToggleButton eventsToggle = new WebToggleButton ( Styles.greenTabToggleButton, "Events", true );
        eventsToggle.addActionListener ( e -> layout.show ( feedContent, "events" ) );
        group.add ( eventsToggle );

        final WebToggleButton announcementsToggle = new WebToggleButton ( Styles.greenTabToggleButton, "Announcements" );
        announcementsToggle.addActionListener ( e -> layout.show ( feedContent, "announcements" ) );
        group.add ( announcementsToggle );

        settings.add ( new GroupPanel ( GroupingType.fillFirst, 5, true, feedTitle, eventsToggle, announcementsToggle ), FormLayout.LINE );
        settings.add ( feedContent, FormLayout.LINE );
    }

    /**
     * Returns settings UI for events feed customization.
     */
    private JComponent createEventsFeedSettings ()
    {
        final WebPanel feed = new WebPanel ( StyleId.panelTransparent, new VerticalFlowLayout ( 0, 5, true, false ) );
        feed.setPreferredWidth ( 0 );

        updateEventsFeed = () -> {
            feed.removeAll ();
            for ( final Event event : events () )
            {
                final WebStyledLabel eventView = new WebStyledLabel ( Styles.eventLabel, event.icon (), WebStyledLabel.CENTER );
                eventView.setEnabled ( event.enabled () );
                eventView.setIcon ( event.icon () );
                eventView.setText ( event.name () );
                eventView.onMousePress ( MouseButton.left, e -> {
                    eventView.setEnabled ( !eventView.isEnabled () );
                    event.setEnabled ( eventView.isEnabled () );
                    updateAnnouncementsFeed.run ();
                } );

                final WebButton notification = new WebButton ( StyleId.buttonUndecorated, flag32 );
                notification.addActionListener ( e -> {
                    final WebPopupMenu allNotifications = new WebPopupMenu ();
                    for ( final NotificationSettings settings : Notifications.list () )
                    {
                        final WebMenuItem notificationItem = new WebMenuItem ( settings.name () );
                        notificationItem.addActionListener ( ae -> event.setNotification ( settings ) );
                        allNotifications.add ( notificationItem );
                    }
                    allNotifications.showBelowMiddle ( notification );
                } );

                feed.add ( new GroupPanel ( GroupingType.fillFirst, eventView, notification ) );
            }
            feed.revalidate ();
            feed.repaint ();
        };
        updateEventsFeed.run ();

        final WebScrollPane feedScroll = new WebScrollPane ( StyleId.scrollpaneTransparentHovering, feed );
        feedScroll.setPreferredHeight ( 300 );
        feedScroll.getVerticalScrollBar ().setUnitIncrement ( 25 );
        feedScroll.getVerticalScrollBar ().setBlockIncrement ( 50 );
        return feedScroll;
    }

    /**
     * Returns settings UI for announcements feed customization.
     */
    private JComponent createAnnouncementsFeedSettings ()
    {
        final WebPanel feed = new WebPanel ( StyleId.panelTransparent, new VerticalFlowLayout ( 0, 5, true, false ) );
        feed.setPreferredWidth ( 0 );

        updateAnnouncementsFeed = () -> {
            feed.removeAll ();
            for ( final Announcement announcement : announcements () )
            {
                final WebStyledLabel announcementView = new WebStyledLabel ( Styles.announcementLabel, WebStyledLabel.CENTER );
                announcementView.setEnabled ( announcement.enabled () );
                announcementView.setIcon ( announcement.icon () );
                announcementView.setText ( TimerUnits.get ().toString ( announcement.time () ) + ": " + announcement.text () );
                announcementView.onMousePress ( MouseButton.left, e -> {
                    announcementView.setEnabled ( !announcementView.isEnabled () );
                    announcement.setEnabled ( announcementView.isEnabled () );
                    updateEventsFeed.run ();
                } );
                feed.add ( announcementView );
            }
            feed.revalidate ();
            feed.repaint ();
        };
        updateAnnouncementsFeed.run ();

        final WebScrollPane feedScroll = new WebScrollPane ( StyleId.scrollpaneTransparentHovering, feed );
        feedScroll.setPreferredHeight ( 300 );
        feedScroll.getVerticalScrollBar ().setUnitIncrement ( 25 );
        feedScroll.getVerticalScrollBar ().setBlockIncrement ( 50 );
        return feedScroll;
    }

    /**
     * Returns marker for the specified green player and event cycle.
     *
     * @param number green player number (between 1 and 3)
     * @param cycle  green event cycle
     * @return marker for the specified green player and event cycle
     */
    private Icon marker ( final int number, final int cycle )
    {
        final int index = ( ( number - 1 ) + ( 3 * cycle ) ) % markers.size ();
        return markers.get ( index );
    }

    /**
     * Returns player name for the specified green player and event cycle.
     *
     * @param number green player number (between 1 and 3)
     * @return player name for the specified green player and event cycle
     */
    private String player ( final int number )
    {
        final String player = SettingsManager.get ( settingsGroup (), "green" + number, "Green " + number );
        return TextUtils.notEmpty ( player ) ? player : "Green " + number;
    }
}