package com.sensorsdata.analytics.android.sdk;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DbAdapter {
    private static final String TAG = "SA.DbAdapter";
    private Context mContext;
    private ContentResolver contentResolver;
    private Uri mAppStart, mAppEndState;

    public enum Table {
        APPSTARTED("app_started"),
        APPENDSTATE("app_end");

        Table(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private String name;
    }

    public static final String APP_STARTED = "$app_started";
    public static final String APP_START_TIME = "$app_start_time";
    public static final String APP_END_STATE = "$app_end";
    public static final String APP_PAUSED_TIME = "$app_paused_time";


    public DbAdapter(Context context, String packageName) {
        mContext = context;
        contentResolver = mContext.getContentResolver();
        mAppStart = Uri.parse("content://" + packageName + ".SensorsDataContentProvider/" + Table.APPSTARTED.getName());
        mAppEndState = Uri.parse("content://" + packageName + ".SensorsDataContentProvider/" + Table.APPENDSTATE.getName());
    }


    /**
     * add the ActivityStart state to the SharedPreferences
     * @param appStart the ActivityState
     */
    public void commitAppStart(boolean appStart){
        ContentValues contentValues = new ContentValues();
        contentValues.put(APP_STARTED, appStart);
        contentResolver.insert(mAppStart, contentValues);
    }

    /**
     * return the state of Activity start
     * @return Activity count
     */
    public boolean getAppStart(){
        boolean state = true;
        Cursor cursor = contentResolver.query(mAppStart, new String[]{APP_STARTED},null,null,null);
        if(cursor != null && cursor.getCount() > 0){
            while(cursor.moveToNext()){
                state = cursor.getInt(0) > 0;
            }
        }

        if(cursor != null){
            cursor.close();
        }
        return state;
    }

    /**
     * add the Activity End to the SharedPreferences
     * @param appEndState the Activity end state
     */
    public void commitAppEndEventState(boolean appEndState){
        ContentValues contentValues = new ContentValues();
        contentValues.put(APP_END_STATE, appEndState);
        contentResolver.insert(mAppEndState, contentValues);
    }

    /**
     * return the state of $AppEnd
     * @return Activity End state
     */
    public boolean getAppEndEventState(){
        boolean state = true;
        Cursor cursor = contentResolver.query(mAppEndState, new String[]{APP_END_STATE},null,null,null);
        if(cursor != null && cursor.getCount() > 0){
            while(cursor.moveToNext()){
                state = cursor.getInt(0) > 0;
            }
        }

        if(cursor != null){
            cursor.close();
        }
        return state;
    }

    public Uri getmAppStart() {
        return mAppStart;
    }
}
