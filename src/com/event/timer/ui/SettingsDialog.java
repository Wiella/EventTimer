package com.event.timer.ui;

import com.alee.extended.behavior.ComponentMoveBehavior;
import com.alee.extended.label.WebStyledLabel;
import com.alee.extended.layout.FormLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.tab.DocumentData;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.text.WebTextField;
import com.alee.laf.window.WebDialog;
import com.alee.managers.settings.processors.WindowSettings;
import com.alee.managers.style.StyleId;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.swing.UnselectableButtonGroup;
import com.event.timer.data.encounter.Encounter;
import com.event.timer.data.encounter.Encounters;
import com.event.timer.data.notification.NotificationSettings;
import com.event.timer.style.color.Colors;
import com.event.timer.style.font.Fonts;
import com.event.timer.style.icons.Icons;
import com.event.timer.style.skin.Styles;
import com.event.timer.ui.hotkey.HotkeyEditor;
import com.event.timer.ui.hotkey.Hotkeys;
import com.event.timer.ui.notification.Notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Mikle Garin
 */

public abstract class SettingsDialog extends WebDialog<SettingsDialog> implements Icons, Colors, Fonts
{
    /**
     * {@link EventTimerDialog}.
     */
    private EventTimerDialog eventTimer;

    /**
     * Constructs new {@link SettingsDialog}.
     *
     * @param eventTimer {@link EventTimerDialog}
     */
    public SettingsDialog ( final EventTimerDialog eventTimer )
    {
        super ( Styles.dialog, eventTimer, "Event Timer Settings" );
        this.eventTimer = eventTimer;

        /**
         * Dialog settings.
         */
        setIconImages ( WebLookAndFeel.getImages () );
        setDefaultCloseOperation ( WindowConstants.DISPOSE_ON_CLOSE );
        setFocusableWindowState ( true );
        setAlwaysOnTop ( true );
        setResizable ( false );
        setModal ( false );

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
        registerSettings ( "SettingsDialog", new WindowSettings () );
        pack ();
    }

    /**
     * Initializes UI.
     */
    private void initializeUI ()
    {
        setLayout ( new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, 0 ) );

        final ComponentMoveBehavior moveBehavior = new ComponentMoveBehavior ( getContentPane () );
        moveBehavior.install ();

        /**
         * Title.
         */
        final WebPanel title = new WebPanel ( Styles.dialogTitle.at ( this ), new BorderLayout ( 10, 0 ) );

        final WebLabel titleIcon = new WebLabel ( settingsHover32 );
        title.add ( titleIcon, BorderLayout.WEST );

        final WebStyledLabel titleLabel = new WebStyledLabel ( StyleId.styledlabelShadow, "Settings" );
        titleLabel.setHorizontalAlignment ( WebStyledLabel.CENTER );
        titleLabel.setFont ( titleFont );
        title.add ( titleLabel, BorderLayout.CENTER );

        final WebButton exitIcon = new WebButton ( StyleId.buttonUndecorated, check32, checkHover32 );
        exitIcon.setCursor ( Cursor.getPredefinedCursor ( Cursor.HAND_CURSOR ) );
        exitIcon.addActionListener ( e -> SettingsDialog.this.dispose () );
        title.add ( exitIcon, BorderLayout.EAST );

        /**
         * Title separator.
         */
        final WebSeparator separator = new WebSeparator ( Styles.customizedSeparator, WebSeparator.HORIZONTAL );

        /**
         * Content area.
         */
        final WebPanel content = new WebPanel ( Styles.dialogContentArea.at ( this ), new VerticalFlowLayout ( 10, 10, true, false ) );
        initializeSettingsUI ( content );

        /**
         * Adding content elements.
         */
        add ( title );
        add ( separator );
        add ( content );
    }

    /**
     * Initializes settings UI.
     *
     * @param content settings content panel
     */
    private void initializeSettingsUI ( final WebPanel content )
    {
        final UnselectableButtonGroup group = new UnselectableButtonGroup ( false );

        final WebPanel tabTitles = new WebPanel ( StyleId.panelTransparent, new BorderLayout () );
        final CardLayout tabsContentLayout = new CardLayout ( 0, 0 );
        final WebPanel tabsContent = new WebPanel ( StyleId.panelTransparent, tabsContentLayout );

        final List<DocumentData> settings = new ArrayList<> ();
        settings.add ( createBasicSettings () );
        settings.addAll ( createEncounterSettings () );

        final WebPanel pane = new WebPanel ( StyleId.panelTransparent, new GridLayout ( 1, settings.size (), 5, 0 ) );
        for ( final DocumentData section : settings )
        {
            final Component sectionContent = section.getComponent ();
            tabsContent.add ( sectionContent, section.getId () );

            final Icon icon = section.getIcon ();
            final String title = section.getTitle ();
            final WebToggleButton sectionButton = new WebToggleButton ( Styles.customizedToggleButton, title, icon );
            sectionButton.setSelected ( settings.indexOf ( section ) == 0 );
            sectionButton.addActionListener ( e -> tabsContentLayout.show ( tabsContent, section.getId () ) );
            pane.add ( sectionButton );
            group.add ( sectionButton );
        }
        tabTitles.add ( pane );

        content.add ( tabTitles );
        content.add ( tabsContent );
    }

    /**
     * Returns {@link DocumentData} for the basic settings panel.
     *
     * @return {@link DocumentData} for the basic settings panel
     */
    private DocumentData createBasicSettings ()
    {
        final WebPanel content = new WebPanel ( StyleId.panelTransparent, new VerticalFlowLayout ( 0, 5 ) );

        /**
         * Hotkey settings.
         */

        final WebPanel hotkeysPanel = new WebPanel ( StyleId.panelTransparent, new FormLayout ( false, true, 5, 5 ) );
        content.add ( hotkeysPanel );

        final WebLabel settingsHotkeyLabel = new WebLabel ( StyleId.labelShadow, "Settings dialog:" );
        final HotkeyEditor settingsHotkey = new HotkeyEditor ( Hotkeys.SETTINGS );
        hotkeysPanel.add ( settingsHotkeyLabel, settingsHotkey );

        final WebLabel startHotkeyLabel = new WebLabel ( StyleId.labelShadow, "Start timer:" );
        final HotkeyEditor startHotkey = new HotkeyEditor ( Hotkeys.START );
        hotkeysPanel.add ( startHotkeyLabel, startHotkey );

        final WebLabel decreaseTimeHotkeyLabel = new WebLabel ( StyleId.labelShadow, "Decrease time:" );
        final HotkeyEditor adjustTimeHotkey = new HotkeyEditor ( Hotkeys.DECREASE_TIME );
        hotkeysPanel.add ( decreaseTimeHotkeyLabel, adjustTimeHotkey );

        final WebLabel exitHotkeyLabel = new WebLabel ( StyleId.labelShadow, "Exit application:" );
        final HotkeyEditor exitHotkey = new HotkeyEditor ( Hotkeys.EXIT );
        hotkeysPanel.add ( exitHotkeyLabel, exitHotkey );

        /**
         * Separator.
         */

        content.add ( createPartsSeparator () );

        /**
         * Current event popup settings.
         */

        final WebCheckBox notificationsBackground = new WebCheckBox ( Styles.customizedCheckBox, "Notifications background" );
        notificationsBackground.registerSettings ( "DisplayNotificationsBackground", true );
        content.add ( notificationsBackground );

        final WebPanel notifications = new WebPanel ( StyleId.panelTransparent, new VerticalFlowLayout ( 0, 5, true, false ) );
        content.add ( notifications );

        final Function<NotificationSettings, JComponent> createNotificationSettings = settings -> {
            final WebTextField nameField = new WebTextField ( settings.name (), 1 );
            final WebButton locationButton = new WebButton ( flag32 );
            final WebButton deleteButton = new WebButton ( downstate32 );
            final GroupPanel np = new GroupPanel ( GroupingType.fillFirst, 5, true, nameField, locationButton, deleteButton );

            nameField.onChange ( ( c, e ) -> SwingUtils.invokeLater ( () -> {
                final String text = nameField.getText ();
                final String name = TextUtils.notEmpty ( text ) ? text : NotificationSettings.DEFAULT_NAME;
                settings.setName ( name );
                Notifications.save ( settings );
            } ) );

            locationButton.addActionListener ( e -> Notifications.position (  settings ) );

            deleteButton.addActionListener ( e -> {
                Notifications.delete ( settings );
                notifications.remove ( np );
                notifications.revalidate ();
                notifications.repaint ();
                SwingUtils.invokeLater ( SettingsDialog.this::pack );
            } );

            return np;
        };
        final Runnable updateNotifications = () -> {
            notifications.removeAll ();
            for ( final NotificationSettings settings : Notifications.list () )
            {
                notifications.add ( createNotificationSettings.apply ( settings ) );
            }
            notifications.revalidate ();
            notifications.repaint ();
            SwingUtils.invokeLater ( SettingsDialog.this::pack );
        };
        updateNotifications.run ();

        final WebButton addNotification = new WebButton ( StyleId.buttonIcon, "Add notification area", checkpoint32 );
        addNotification.addActionListener ( e -> {
            final NotificationSettings settings = Notifications.create ();
            notifications.add ( createNotificationSettings.apply ( settings ) );
            notifications.revalidate ();
            notifications.repaint ();
            SwingUtils.invokeLater ( SettingsDialog.this::pack );
        } );
        content.add ( addNotification );

        /**
         * Separator.
         */

        content.add ( createPartsSeparator () );

        /**
         * Background settings.
         */

        final WebCheckBox titleBackground = new WebCheckBox ( Styles.customizedCheckBox, "Title background" );
        titleBackground.registerSettings ( "DisplayTitleBackground", true );
        titleBackground.addActionListener ( e -> eventTimer.updateBackgroundSettings () );
        content.add ( titleBackground );

        final WebCheckBox contentBackground = new WebCheckBox ( Styles.customizedCheckBox, "Content background" );
        contentBackground.registerSettings ( "DisplayContentBackground", true );
        contentBackground.addActionListener ( e -> eventTimer.updateBackgroundSettings () );
        content.add ( contentBackground );

        return new DocumentData<> ( "basic", "Basic settings", content );
    }

    /**
     * Returns settings parts separator.
     *
     * @return settings parts separator
     */
    private WebSeparator createPartsSeparator ()
    {
        final WebSeparator separator = new WebSeparator ( Styles.customizedSeparator, WebSeparator.HORIZONTAL );
        separator.setPadding ( 5, 0, 5, 0 );
        return separator;
    }

    /**
     * Returns {@link List} of {@link DocumentData} containing encounter settings.
     *
     * @return {@link List} of {@link DocumentData} containing encounter settings
     */
    private List<DocumentData> createEncounterSettings ()
    {
        final List<DocumentData> settings = new ArrayList<> ();
        for ( final Encounter encounter : Encounters.all () )
        {
            final String sectionName = encounter.name () + " encounter";
            final JComponent sectionContent = encounter.settings ();
            settings.add ( new DocumentData<> ( encounter.name (), sectionName, sectionContent ) );
        }
        return settings;
    }

    /**
     * Initializes actions.
     */
    private void initializeActions ()
    {
        /**
         * Dialog listeners.
         */
        addComponentListener ( new ComponentAdapter ()
        {
            @Override
            public void componentShown ( final ComponentEvent e )
            {
                /**
                 * Pause all hotkeys while configuring.
                 */
                Hotkeys.pauseAll ();
            }
        } );
        addWindowListener ( new WindowAdapter ()
        {
            @Override
            public void windowClosed ( final WindowEvent e )
            {
                /**
                 * Resume hotkeys after dialog is closed.
                 */
                Hotkeys.resumeAll ();

                /**
                 * Settings changed.
                 */
                settingsChanged ();
            }
        } );
    }

    /**
     * Informs about settings changes.
     */
    protected abstract void settingsChanged ();
}