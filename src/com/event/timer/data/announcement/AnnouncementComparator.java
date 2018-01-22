package com.event.timer.data.announcement;

import java.util.Comparator;

/**
 * {@link Comparator} for {@link Announcement}s by their time.
 * {@link Announcement}s that happen earlier in the encounter are placed first.
 *
 * @author Mikle Garin
 */

public final class AnnouncementComparator implements Comparator<Announcement>
{
    @Override
    public int compare ( final Announcement a1, final Announcement a2 )
    {
        final long t1 = a1.time () + a1.event ().time ().advance ();
        final long t2 = a2.time () + a2.event ().time ().advance ();
        return Long.compare ( t2, t1 );
    }
}