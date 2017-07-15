package com.techmali.smartteam.database;

import android.content.ContentValues;

/**
 * Created by nrana on 25-Mar-16.
 */
public interface DatabaseOperation {
    public long insert(Object object);
    public int update(Object object);
    public int delete(Object object);
    ContentValues getContentValues(Object object);
}
