package we.video.wevideo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import we.video.wevideo.cons.Constants;

/**
 * Json缓存
 *
 * @author Berfy
 */
public class TempSharedData {
    static SharedPreferences mSharedPreferences;
    static Editor mEditor;

    private static TempSharedData mUserSharedData;

    public static TempSharedData getInstance(Context context) {
        if (mUserSharedData == null) {
            synchronized (TempSharedData.class) {
                if (mUserSharedData == null) {
                    mUserSharedData = new TempSharedData();
                    mSharedPreferences = context.getSharedPreferences(
                            Constants.XML_TEMP, Context.MODE_PRIVATE);
                    mEditor = mSharedPreferences.edit();
                }
            }
        }
        return mUserSharedData;
    }

    /**
     * ]
     * String类型
     *
     * @param key
     * @param value
     */
    public void put(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * Bool类型
     *
     * @param key
     * @param value
     */
    public void put(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    /**
     * Int类型
     *
     * @param key
     * @param value
     */
    public void put(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    /**
     * String类型
     *
     * @param key
     */
    public String get(String key) {
        return mSharedPreferences.getString(key, "");
    }

    /**
     * Bool类型
     *
     * @param key
     */
    public boolean get(String key, boolean defaultBool) {
        return mSharedPreferences.getBoolean(key, defaultBool);
    }

    /**
     * Int类型
     *
     * @param key
     */
    public int get(String key, int defaultInt) {
        return mSharedPreferences.getInt(key, defaultInt);
    }

    public void clearJsonTemp() {
        mEditor.clear().commit();
    }

}
