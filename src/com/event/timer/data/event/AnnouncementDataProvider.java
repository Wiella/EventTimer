package com.event.timer.data.event;

/**
 * Special provider for {@link AnnouncementData}.
 *
 * @author Mikle Garin
 */

public interface AnnouncementDataProvider
{
    /**
     * Returns {@link AnnouncementData} for the specified {@link Event} cycle.
     *
     * @param cycle {@link Event} cycle
     * @return {@link AnnouncementData} for the specified {@link Event} cycle
     */
    public AnnouncementData get ( int cycle );
}