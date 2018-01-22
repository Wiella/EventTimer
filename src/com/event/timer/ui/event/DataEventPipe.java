package com.event.timer.ui.event;

import com.alee.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mikle Garin
 */

public final class DataEventPipe
{
    /**
     * {@link DataListener}s cache.
     */
    private static final Map<Class, List<DataListener>> listeners = new ConcurrentHashMap<> ( 5 );

    /**
     * Adds {@link DataListener} for the specified data type.
     *
     * @param clazz    data class type
     * @param listener {@link DataListener}
     * @param <T>      data type
     */
    public static <T> void listenTo ( final Class<T> clazz, final DataListener<T> listener )
    {
        listeners.computeIfPresent ( clazz, ( type, listeners ) -> {
            final List<DataListener> existing = listeners != null ? listeners : new ArrayList<> ( 1 );
            existing.add ( listener );
            return existing;
        } );
    }

    public static <T> void fireAdded ( final T data )
    {
        final List<DataListener> listeners = DataEventPipe.listeners.get ( data.getClass () );
        if ( CollectionUtils.notEmpty ( listeners ) )
        {
            for ( final DataListener listener : listeners )
            {
                listener.added ( data );
            }
        }
    }

    public static <T> void fireChanged ( final T oldData, final T newData )
    {
        final List<DataListener> listeners = DataEventPipe.listeners.get ( ( oldData != null ? oldData : newData ).getClass () );
        if ( CollectionUtils.notEmpty ( listeners ) )
        {
            for ( final DataListener listener : listeners )
            {
                listener.changed ( oldData, newData );
            }
        }
    }

    public static <T> void fireRemoved ( final T data )
    {
        final List<DataListener> listeners = DataEventPipe.listeners.get ( data.getClass () );
        if ( CollectionUtils.notEmpty ( listeners ) )
        {
            for ( final DataListener listener : listeners )
            {
                listener.removed ( data );
            }
        }
    }
}