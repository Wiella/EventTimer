package com.event.timer.data.notification;

import com.alee.utils.TextUtils;
import com.alee.utils.xml.PointConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.awt.*;
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

    @XStreamConverter ( PointConverter.class )
    @XStreamAsAttribute
    private Point location;

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

    public Point location ()
    {
        return location;
    }

    public void setLocation ( final Point location )
    {
        this.location = location;
    }
}