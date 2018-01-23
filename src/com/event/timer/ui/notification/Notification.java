package com.event.timer.ui.notification;

import com.alee.extended.behavior.ComponentMoveBehavior;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.window.WebPopup;
import com.alee.managers.style.StyleId;
import com.alee.utils.SwingUtils;
import com.alee.utils.SystemUtils;
import com.event.timer.data.announcement.Announcement;
import com.event.timer.data.notification.NotificationSettings;
import com.event.timer.style.icons.Icons;
import com.event.timer.style.skin.Styles;
import com.event.timer.ui.behavior.CtrlMoveBehavior;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Mikle Garin
 */

public final class Notification extends WebPopup<Notification> implements Runnable, Icons
{
    /**
     * {@link NotificationSettings} for this popup.
     */
    private final NotificationSettings settings;

    /**
     * {@link Announcement} displayed by this notification popup.
     * Only provided for non-sample notifications display, otherwise it is {@code null}.
     */
    private final Announcement announcement;

    /**
     * Time left.
     */
    private long timeLeft;

    /**
     * Announcement tracking timer.
     */
    private Timer timer;

    /**
     * UI elements.
     */
    private WebStyledLabel notificationLabel;

    /**
     * Constructs sample {@link Notification}.
     *
     * @param settings {@link NotificationSettings}
     */
    public Notification ( final NotificationSettings settings )
    {
        this ( settings, null );
    }

    /**
     * Constructs {@link Notification} for the specified {@link Announcement}.
     *
     * @param settings     {@link NotificationSettings}
     * @param announcement {@link Announcement}
     */
    public Notification ( final NotificationSettings settings, final Announcement announcement )
    {
        super ( Styles.announcementPopup );
        this.settings = settings;
        this.announcement = announcement;
        this.timeLeft = -1;
        initializeUI ();
    }

    /**
     * Initialises popup UI.
     */
    private void initializeUI ()
    {
        /**
         * Popup settings.
         */
        setAlwaysOnTop ( true );
        setCloseOnFocusLoss ( false );
        setCloseOnOuterAction ( false );

        /**
         * Notification content.
         */
        if ( announcement == null )
        {
            notificationLabel = new WebStyledLabel ( StyleId.styledlabelShadow, settings.name (), laurel32 );
        }
        else
        {
            notificationLabel = new WebStyledLabel ( Styles.announcementLabel, "", announcement.data ().icon () );
        }
        add ( notificationLabel );

        /**
         * Setup move behavior.
         */
        final ComponentMoveBehavior moveBehavior;
        if ( announcement == null )
        {
            moveBehavior = new ComponentMoveBehavior ( Notification.this );
        }
        else
        {
            moveBehavior = new CtrlMoveBehavior ( Notification.this );
        }
        moveBehavior.install ();

        /**
         * Hide behavior.
         */
        if ( announcement == null )
        {
            onDoubleClick ( e -> hidePopup () );
        }
        else
        {
            onMousePress ( e -> {
                if ( !SwingUtils.isCtrl ( e ) )
                {
                    hidePopup ();
                }
            } );
        }
    }

    @Override
    public void run ()
    {
        if ( timeLeft <= 0 )
        {
            /**
             * Hiding popup when time is out.
             */
            SwingUtils.invokeLater ( Notification.this::hidePopup );
        }
        else
        {
            /**
             * Decreasing time left.
             */
            timeLeft = timeLeft - 1000L;

            /**
             * Updating displayed text.
             */
            SwingUtils.invokeLater ( this::updateText );
        }
    }

    /**
     * Updates notification text.
     */
    private void updateText ()
    {
        /**
         * Updating label.
         */
        final String text = announcement.text ( timeLeft );
        notificationLabel.setText ( text );

        /**
         * Updating popup position.
         */
        final JWindow window = getWindow ();
        final Rectangle bounds = window.getBounds ();
        final Dimension ps = window.getPreferredSize ();
        window.setBounds ( bounds.x + bounds.width / 2 - ps.width / 2, bounds.y + bounds.height / 2 - ps.height / 2, ps.width, ps.height );
    }

    /**
     * Displays notification.
     * Performs necessary adjustments to popup position and registers listener to save changes.
     */
    public void showNotification ()
    {
        synchronized ( sync )
        {
            /**
             * Launching timer.
             */
            if ( announcement != null )
            {
                timeLeft = announcement.event ().time ().advance ();
                timer = new Timer ( "NotificationTimer @ " + announcement.id (), true );
                timer.schedule ( new TimerTask ()
                {
                    @Override
                    public void run ()
                    {
                        Notification.this.run ();
                    }
                }, 0L, 1000L );
            }

            /**
             * Updating location.
             */
            Point location = settings.location ();
            final Dimension ps = getPreferredSize ();
            if ( location == null )
            {
                final GraphicsDevice screen = SystemUtils.getDefaultScreenDevice ();
                final Rectangle bounds = screen.getDefaultConfiguration ().getBounds ();
                location = new Point ( bounds.x / 2 + bounds.width / 2, bounds.y / 2 + bounds.height / 2 );
                settings.setLocation ( location );
                Notifications.save ( settings );
            }
            location.x = location.x - ps.width / 2;
            location.y = location.y - ps.height / 2;
            setLocation ( location );

            /**
             * Displaying notification.
             */
            showPopup ( null, location );

            /**
             * Registering location capture.
             * We don't need to dispose this as new window is created for every display.
             */
            getWindow ().addComponentListener ( new ComponentAdapter ()
            {
                @Override
                public void componentMoved ( final ComponentEvent e )
                {
                    final Rectangle bounds = getWindow ().getBounds ();
                    settings.setLocation ( new Point ( bounds.x + bounds.width / 2, bounds.y + bounds.height / 2 ) );
                    Notifications.save ( settings );
                }
            } );

            /**
             * Properly stopping timer if needed on notification close.
             */
            getWindow ().addWindowListener ( new WindowAdapter ()
            {
                @Override
                public void windowClosed ( final WindowEvent e )
                {
                    if ( announcement != null )
                    {
                        timer.cancel ();
                        timeLeft = 0;
                    }
                }
            } );
        }
    }
}