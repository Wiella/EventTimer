package com.event.timer.ui.settings;

import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * @author Mikle Garin
 */

@XStreamAlias ( "Notification" )
public final class NotificationSettings implements Serializable
{
    public static final String DEFAULT_NAME = "Notification area";

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String name;

    public NotificationSettings ()
    {
        super ();
        this.id = TextUtils.generateId ( "NF" );
        this.name = DEFAULT_NAME;
    }

    public String id ()
    {
        return id;
    }

    public String name ()
    {
        return name;
    }

    public void setName ( final String name )
    {
        this.name = name;
    }
}