package com.infinitydheer.themanager.data.repository.datasource.base;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    /**
     * Method to read a {@link Long} from the {@link SharedPreferences}
     * @param context Context of the current working application
     * @param preferenceFileName Name of the file of SharedPreferences
     * @param key Key of the value required
     * @return The value referenced by the key
     */
    public long readFromPreference(Context context, String preferenceFileName,
                                   String key){
        return context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE)
                .getLong(key, -1);
    }

    /**
     * Method to read a {@link String} from the {@link SharedPreferences}
     * @param context Context of the current working application
     * @param preferenceFileName Name of the file of SharedPreferences
     * @param key1 Key of the value required
     * @return The value referenced by the key
     */
    public String readStringFromPreference(Context context, String preferenceFileName,
                                           String key1){
        return context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE)
                .getString(key1, "");
    }

    /**
     * Method to write to a {@link SharedPreferences} file, asynchronously
     * @param context Context of the current working application
     * @param preferenceFileName Name of the file of SharedPreferences
     * @param key Key of the value required
     * @param value Value to be written corresponding to the key
     */
    public void writeToPreference(Context context, String preferenceFileName,
                                  String key, long value){
        final SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceFileName,
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong(key,value);
        editor.apply();
    }

    /**
     * Method to write to a {@link SharedPreferences} file
     * @param context Context of the current working application
     * @param preferenceFileName Name of the file of SharedPreferences
     * @param key Key of the value required
     * @param value Value to be written corresponding to the key
     */
    public void writeToPreference(Context context, String preferenceFileName,
                                  String key, String value){
        final SharedPreferences sharedPreferences=context.getSharedPreferences(preferenceFileName,
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
}
