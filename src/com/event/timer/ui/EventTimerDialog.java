package com.event.timer.ui;

import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.window.WebDialog;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.processors.WindowSettings;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.parsing.DurationUnits;
import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.encounter.Encounter;
import com.event.timer.data.encounter.Encounters;
import com.event.timer.style.color.Colors;
import com.event.timer.style.font.Fonts;
import com.event.timer.style.format.TimerUnits;
import com.event.timer.style.icons.Icons;
import com.event.timer.style.skin.Styles;
import com.event.timer.style.sound.SoundEffect;
import com.event.timer.style.sound.SpeechSoundEffect;
import com.event.timer.ui.behavior.CtrlMoveBehavior;
import com.event.timer.ui.components.BackgroundPanel;
import com.event.timer.ui.components.BackgroundSeparator;
import com.event.timer.ui.hotkey.Hotkeys;
import com.event.timer.ui.notification.Notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main timer dialog.
 *
 * @author Mikle Garin
 */

public final class EventTimerDialog extends WebDialog<EventTimerDialog> implements Runnable, Icons, Colors, Fonts
{
    /**
     * UI elements.
     */
    private BackgroundPanel titlePanel;
    private BackgroundSeparator titleSeparator;
    private WebLabel titleIcon;
    private WebStyledLabel titleLabel;
    private WebButton settings;
    private SettingsDialog settingsDialog;
    private BackgroundPanel contentPanel;
    private WebStyledLabel small1;
    private WebStyledLabel small2;
    private WebStyledLabel small3;
    private WebStyledLabel small4;
    private WebStyledLabel large;

    /**
     * Currently running {@link Encounter}.
     */
    private Encounter encounter;

    /**
     * {@link Announcement}s for the {@link #encounter}.
     */
    private List<Announcement> announcements;

    /**
     * Countdown timer.
     */
    private Timer timer;

    /**
     * Elapsed fight time.
     */
    private long elapsed = 0L;

    /**
     * Index of last {@link Announcement}.
     */
    private int lastAnnounced = -1;

    /**
     * Constructs new {@link EventTimerDialog}.
     */
    public EventTimerDialog ()
    {
        super ( Styles.dialog, ( Frame ) null, "Event Timer" );

        /**
         * Dialog settings.
         */
        setIconImages ( WebLookAndFeel.getImages () );
        setDefaultCloseOperation ( WindowConstants.DISPOSE_ON_CLOSE );
        setFocusableWindowState ( false );
        setAlwaysOnTop ( true );
        setResizable ( false );
        setModal ( false );

        /**
         * Initial encounter.
         */
        this.encounter = Encounters.all ().get ( 0 );

        /**
         * Initializing UI.
         */
        initializeUI ();

        /**
         * Initializing actions.
         */
        initializeActions ();

        /**
         * Adjusting dialog position.
         */
        registerSettings ( "EventTimerDialog", new WindowSettings () );
        pack ();
    }

    /**
     * Initializes UI.
     */
    private void initializeUI ()
    {
        setLayout ( new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, 0 ) );

        final CtrlMoveBehavior moveBehavior = new CtrlMoveBehavior ( getContentPane () );
        moveBehavior.install ();

        /**
         * Title.
         */
        titlePanel = new BackgroundPanel ( Styles.dialogTitle.at ( this ), "DisplayTitleBackground", "DisplayContentBackground" );
        titlePanel.setLayout ( new BorderLayout ( 10, 0 ) );

        titleIcon = new WebLabel ();
        titlePanel.add ( titleIcon, BorderLayout.WEST );

        titleLabel = new WebStyledLabel ( StyleId.styledlabelShadow, WebStyledLabel.CENTER );
        titleLabel.setFont ( titleFont );
        titlePanel.add ( titleLabel, BorderLayout.CENTER );

        settings = new WebButton ( StyleId.buttonUndecorated, settings32, settingsHover32 );
        settings.setCursor ( Cursor.getPredefinedCursor ( Cursor.HAND_CURSOR ) );
        settings.addActionListener ( e -> showSettings () );
        titlePanel.add ( settings, BorderLayout.EAST );

        /**
         * Title separator.
         */
        titleSeparator = new BackgroundSeparator ( Styles.customizedSeparator,
                WebSeparator.HORIZONTAL, "DisplayTitleBackground", "DisplayContentBackground" );

        /**
         * Content area.
         */
        contentPanel = new BackgroundPanel ( Styles.dialogContentArea.at ( this ), "DisplayContentBackground", "DisplayTitleBackground" );
        contentPanel.setLayout ( new VerticalFlowLayout ( 10, 10, true, false ) );

        small1 = new WebStyledLabel ( Styles.announcementLabel, WebStyledLabel.CENTER );
        small2 = new WebStyledLabel ( Styles.announcementLabel, WebStyledLabel.CENTER );
        large = new WebStyledLabel ( Styles.announcementLabel, WebStyledLabel.CENTER );
        small3 = new WebStyledLabel ( Styles.announcementLabel, WebStyledLabel.CENTER );
        small4 = new WebStyledLabel ( Styles.announcementLabel, WebStyledLabel.CENTER );

        contentPanel.add ( small1, small2, large, small3, small4 );

        /**
         * Loading initial view.
         */
        updateToStartingScreen ();

        /**
         * Adding content elements.
         */
        add ( titlePanel );
        add ( titleSeparator );
        add ( contentPanel );
    }

    /**
     * Initializes actions.
     */
    private void initializeActions ()
    {
        /**
         * Settings hotkey.
         */
        Hotkeys.register ( Hotkeys.SETTINGS, h -> showSettings () );

        /**
         * Timer start hotkey.
         */
        Hotkeys.register ( Hotkeys.START, h -> startOrStop () );

        /**
         * Decrease time.
         */
        Hotkeys.register ( Hotkeys.DECREASE_TIME, h -> changeElapsed ( "1s" ) );

        /**
         * Exit hotkey.
         */
        Hotkeys.register ( Hotkeys.EXIT, h -> EventTimerDialog.this.dispose () );

        /**
         * Dialog listener.
         */
        addWindowListener ( new WindowAdapter ()
        {
            @Override
            public void windowClosed ( final WindowEvent e )
            {
                shutdown ();
            }
        } );
    }

    /**
     * Displays settings dialog.
     */
    private synchronized void showSettings ()
    {
        if ( !isTimerRunning () )
        {
            /**
             * Disabling all elements to make it obvious timer can't be used while settings are open.
             */
            if ( settingsDialog == null || !settingsDialog.isVisible () )
            {
                SwingUtils.setEnabledRecursively ( EventTimerDialog.this, false, true );
            }

            /**
             * Making sure dialog exists.
             */
            if ( settingsDialog == null )
            {
                settingsDialog = new SettingsDialog ( EventTimerDialog.this )
                {
                    @Override
                    protected void settingsChanged ()
                    {
                        /**
                         * Enabling all elements back to allow timer usage.
                         */
                        SwingUtils.setEnabledRecursively ( EventTimerDialog.this, true, true );

                        /**
                         * Updating starting screen to reflect changes.
                         */
                        updateToStartingScreen ();
                    }
                };
            }

            /**
             * Displaying settings dialog when needed.
             */
            if ( !settingsDialog.isVisible () )
            {
                settingsDialog.setVisible ( true );
            }
        }
    }

    /**
     * Returns whether or not timer is running.
     *
     * @return {@code true} if timer is running, {@code false} otherwise
     */
    private synchronized boolean isTimerRunning ()
    {
        return timer != null;
    }

    /**
     * Starts or stops countdown.
     * todo Might have to proceed to subsequent encounter (like another phase) instead of stopping later
     */
    private synchronized void startOrStop ()
    {
        if ( !isTimerRunning () )
        {
            start ();
        }
        else
        {
            stop ();
        }
    }

    /**
     * Starts countdown.
     */
    private synchronized void start ()
    {
        if ( !isTimerRunning () )
        {
            /**
             * Preparing UI.
             */
            SwingUtils.invokeLater ( () -> settings.setEnabled ( false ) );

            /**
             * Collecting {@link Announcement}s.
             * We have to filter out disabled ones to hide them in the timer.
             */
            this.announcements = CollectionUtils.filter ( encounter.announcements (), Announcement::enabled );

            /**
             * Marking start time.
             */
            elapsed = 0L;
            lastAnnounced = -1;

            /**
             * Launching timer.
             */
            timer = new Timer ( "EventTimer", true );
            timer.schedule ( new TimerTask ()
            {
                @Override
                public void run ()
                {
                    EventTimerDialog.this.run ();
                }
            }, 0L, 1000L );
        }
    }

    /**
     * Adding time to the elapsed time.
     * This should not cause any issues, but might clog the events queue a bit.
     *
     * @param period time period to add to elapsed time
     */
    private synchronized void changeElapsed ( final String period )
    {
        if ( isTimerRunning () )
        {
            this.elapsed += DurationUnits.get ().fromString ( period );
        }
    }

    @Override
    public synchronized void run ()
    {
        if ( isTimerRunning () )
        {
            if ( this.elapsed <= encounter.duration () )
            {
                /**
                 * Saving elapsed time locally.
                 * We will also pass it to all sub-calls to avoid issues.
                 * This is necessary to ensure that exactly this elapsed time is used for UI updates.
                 */
                final long elapsed = this.elapsed;

                /**
                 * Checking next announcement.
                 */
                final SoundEffect playSoundEffect;
                final int nextIndex;
                final Announcement activatedAnnounce;
                final Announcement nextAnnounce;
                final int currentIndex = lastAnnounced == -1 ? 0 : lastAnnounced + 1;
                if ( currentIndex < announcements.size () )
                {
                    final Announcement currentAnnounce = announcements.get ( currentIndex );
                    if ( encounter.duration () - elapsed - currentAnnounce.event ().time ().advance () <= currentAnnounce.time () )
                    {
                        /**
                         * New event reached into advance time.
                         */
                        lastAnnounced++;
                        activatedAnnounce = currentAnnounce;
                        nextIndex = lastAnnounced == -1 ? 0 : lastAnnounced + 1;
                        nextAnnounce = nextIndex < announcements.size () ? announcements.get ( nextIndex ) : null;
                    }
                    else
                    {
                        /**
                         * Still waiting for next event.
                         */
                        activatedAnnounce = null;
                        nextIndex = currentIndex;
                        nextAnnounce = currentAnnounce;
                    }
                }
                else
                {
                    /**
                     * There is no next event.
                     */
                    activatedAnnounce = null;
                    nextIndex = currentIndex;
                    nextAnnounce = null;
                }

                /**
                 * Updating view.
                 */
                SwingUtils.invokeLater ( () -> {
                    titleIcon.setIcon ( nextIndex > 0 ? announcements.get ( nextIndex - 1 ).data ().icon () : announcement32 );
                    titleLabel.setText ( timer ( elapsed ) );
                    update ( small1, nextIndex > 2 ? announcements.get ( nextIndex - 3 ) : null, elapsed );
                    update ( small2, nextIndex > 1 ? announcements.get ( nextIndex - 2 ) : null, elapsed );
                    update ( large, nextIndex > 0 ? announcements.get ( nextIndex - 1 ) : null, elapsed );
                    update ( small3, nextAnnounce, elapsed );
                    update ( small4, nextIndex < announcements.size () - 1 ? announcements.get ( nextIndex + 1 ) : null, elapsed );
                } );

                /**
                 * Announcement becoming active.
                 */
                if ( activatedAnnounce != null )
                {
                    /**
                     * Displaying notification.
                     */
                    SwingUtils.invokeLater ( () -> Notifications.show ( activatedAnnounce ) );

                    /**
                     * Sound notification.
                     */
                    if ( SettingsManager.get ( "VerbalAnnouncements", true ) )
                    {
                        new SpeechSoundEffect ( activatedAnnounce.data ().speech () ).play ();
                    }
                    else
                    {
                        activatedAnnounce.data ().sound ().play ();
                    }
                }

                /**
                 * Adjusting elapsed time at a fixed rate.
                 * This is needed to ensure we are working with precise values.
                 */
                this.elapsed += 1000L;
            }
            else
            {
                /**
                 * Halting when time is out.
                 */
                stop ();
            }
        }
    }

    /**
     * Stops timer.
     */
    private synchronized void stop ()
    {
        /**
         * Disposing timer.
         */
        if ( isTimerRunning () )
        {
            /**
             * Stopping timer.
             */
            timer.cancel ();
            timer.purge ();
            timer = null;

            /**
             * Resetting variables.
             */
            elapsed = 0L;
            lastAnnounced = -1;

            /**
             * Resetting encounter and announcements.
             */
            this.announcements = null;

            /**
             * Updating view.
             */
            SwingUtils.invokeLater ( this::updateToStartingScreen );

            /**
             * Preparing UI.
             */
            SwingUtils.invokeLater ( () -> settings.setEnabled ( true ) );
        }
    }

    /**
     * Shuts timer down.
     */
    public synchronized void shutdown ()
    {
        /**
         * Stopping timer.
         */
        stop ();

        /**
         * Shutting down hotkeys.
         */
        Hotkeys.shutdown ();
    }

    /**
     * Returns current boss timer.
     *
     * @param elapsed encounter time elapsed
     * @return current boss timer
     */
    private String timer ( final long elapsed )
    {
        final long timer = encounter.duration () - elapsed;
        return "{" + TimerUnits.get ().toString ( timer ) + ":b;c(" + greenGW2 + ")}";
    }

    /**
     * Updates label view for the specified {@link Announcement}.
     *
     * @param label        {@link WebStyledLabel} to update
     * @param announcement {@link Announcement}
     * @param elapsed      encounter time elapsed
     */
    private void update ( final WebStyledLabel label, final Announcement announcement, final long elapsed )
    {
        label.setEnabled ( announcement == null || encounter.duration () - announcement.time () > elapsed );
        label.setIcon ( announcement != null ? announcement.data ().icon () : empty32 );
        label.setText ( announcement != null ? announcement.text ( encounter.duration () - elapsed - announcement.time () ) : " " );
    }

    /**
     * Updates view to starting screen.
     */
    protected void updateToStartingScreen ()
    {
        titleIcon.setIcon ( boss32 );
        titleLabel.setText ( encounter.name () );

        small1.setEnabled ( true );
        small1.setIcon ( empty32 );
        small1.setText ( "Press {Ctrl:c(" + blue + ")} to {Drag:c(" + blue + ")} timer" );

        small2.setEnabled ( true );
        small2.setIcon ( empty32 );
        small2.setText ( "Press {" + Hotkeys.SETTINGS.get ().toString () + ":c(" + blue + ")} for settings" );

        large.setEnabled ( true );
        large.setIcon ( empty32 );
        large.setText ( "{" + Hotkeys.START.get ().toString () + ":c(" + green + ")} to start" );

        small3.setEnabled ( true );
        small3.setIcon ( empty32 );
        small3.setText ( "{" + Hotkeys.START.get ().toString () + ":c(" + green + ")} to stop" );

        small4.setEnabled ( true );
        small4.setIcon ( empty32 );
        small4.setText ( "{" + Hotkeys.EXIT.get ().toString () + ":c(" + red + ")} to exit" );
    }

    /**
     * Updating background element settings.
     */
    protected void updateBackgroundSettings ()
    {
        titlePanel.updateStates ();
        titleSeparator.updateStates ();
        contentPanel.updateStates ();
    }
}