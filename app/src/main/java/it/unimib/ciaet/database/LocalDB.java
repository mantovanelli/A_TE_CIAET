package it.unimib.ciaet.database;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalDB {
    private final String PREFS_NAME_APP = "ciaet.app";

    private SharedPreferences sharedPrefAppSetting;
    private Context context;

    private final String USER_FIRST_NAME = "firstName";
    private final String USER_LAST_NAME = "lastName";

    private final String USER_NODE_ID = "userNodeId";
    private final String USER_EMAIL = "user_email";
    private final String USER_IMAGE = "user_image";

    public LocalDB(Context context) {
        this.context = context;
        sharedPrefAppSetting = context.getSharedPreferences(PREFS_NAME_APP, Context.MODE_PRIVATE);
    }

    public void saveLocalUserData(String userNodeID, String firstName, String lastName, String userEmail, String userImage) {
        SharedPreferences.Editor editor = sharedPrefAppSetting.edit();
        editor.putString(USER_FIRST_NAME, firstName);
        editor.putString(USER_LAST_NAME, lastName);
        editor.putString(USER_NODE_ID, userNodeID);
        editor.putString(USER_EMAIL, userEmail);
        editor.putString(USER_IMAGE, userImage);
        editor.apply();
    }

    public String getUSER_FIRST_NAME() {
        return sharedPrefAppSetting.getString(USER_FIRST_NAME, "");
    }

    public String getUserEmail() {
        return sharedPrefAppSetting.getString(USER_EMAIL, "");
    }

    public String getUSER_LAST_NAME() {
        return sharedPrefAppSetting.getString(USER_LAST_NAME, "");
    }

    public String getUserNodeID() {
        return sharedPrefAppSetting.getString(USER_NODE_ID, "");
    }

    public String getUSER_IMAGE(){
        return sharedPrefAppSetting.getString(USER_IMAGE, "");

    }

    public void clearSharedPreference() {
        SharedPreferences.Editor editor = sharedPrefAppSetting.edit();
        editor.clear();
        editor.apply();
    }
}
