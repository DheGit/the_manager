package com.infinitydheer.themanager.data.repository.datasource.general;

import android.content.Context;

import com.infinitydheer.themanager.data.repository.datasource.base.PreferenceManager;
import com.infinitydheer.themanager.domain.constants.S;

/**
 * Helper class to perform CRUD operations on values shared by the app
 */
public class GeneralDataStore {
    private Context mContext;
    private PreferenceManager mPreferenceManager;

    public GeneralDataStore(Context context){
        this.mPreferenceManager =new PreferenceManager();
        this.mContext=context;
    }

    /**
     * Method to get the main password(PIN) set by the user
     * @return The PIN
     */
    public String getMainPassword(){
        return this.mPreferenceManager.readStringFromPreference(mContext, S.Constants.PREF_USER,S.Constants.PREF_USER_MAINP);
    }

    /**
     * Method to set the main password(PIN) for entry into the app
     * @param newPassword The new PIN. Pass in empty {@link String} here to disable password
     */
    public void setMainPassword(String newPassword){
        this.mPreferenceManager.writeToPreference(mContext,S.Constants.PREF_USER,S.Constants.PREF_USER_MAINP,newPassword);
    }
}
