package com.sensorsdata.analytics.android.sdk;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SensorsDataContentProvider extends ContentProvider {
    private final static int APP_START = 1;
    private final static int APP_END_STATE = 2;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor mEditor;
    private final String prefsName = "com.sensorsdata.analytics.android.sdk.SensorsDataAPI";
    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private ContentResolver mContentResolver;
    @Override
    public boolean onCreate() {
        String packName = getContext().getPackageName();
        uriMatcher.addURI(packName + ".SensorsDataContentProvider", DbAdapter.Table.APPSTARTED.getName(), APP_START);
        uriMatcher.addURI(packName + ".SensorsDataContentProvider", DbAdapter.Table.APPENDSTATE.getName(), APP_END_STATE);
        sharedPreferences = getContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        mEditor = sharedPreferences.edit();
        mContentResolver = getContext().getContentResolver();
        return false;
    }



    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int code = uriMatcher.match(uri);
        switch (code) {
            case APP_START:
                boolean appStart = contentValues.getAsBoolean(DbAdapter.APP_STARTED);
                mEditor.putBoolean(DbAdapter.APP_STARTED, appStart);
                mContentResolver.notifyChange(uri, null);
                break;
            case APP_END_STATE:
                boolean appEnd = contentValues.getAsBoolean(DbAdapter.APP_END_STATE);
                mEditor.putBoolean(DbAdapter.APP_END_STATE, appEnd);
                break;
        }
        mEditor.commit();
        return uri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int code = uriMatcher.match(uri);
        MatrixCursor matrixCursor = null;
        switch (code) {
            case APP_START:
                int appStart = sharedPreferences.getBoolean(DbAdapter.APP_STARTED, true) ? 1 : 0;
                matrixCursor = new MatrixCursor(new String[]{DbAdapter.APP_STARTED});
                matrixCursor.addRow(new Object[]{appStart});
                break;
            case APP_END_STATE:
                int appEnd = sharedPreferences.getBoolean(DbAdapter.APP_END_STATE, true) ? 1 : 0;
                matrixCursor = new MatrixCursor(new String[]{DbAdapter.APP_END_STATE});
                matrixCursor.addRow(new Object[]{appEnd});
                break;
        }
        return matrixCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
