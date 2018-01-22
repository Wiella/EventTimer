package com.event.timer.ui.event;

import java.util.EventListener;

/**
 * @author Mikle Garin
 */

public interface DataListener<T> extends EventListener
{
    public void added ( T data );

    public void changed ( T oldData, T newData );

    public void removed ( T data );
}