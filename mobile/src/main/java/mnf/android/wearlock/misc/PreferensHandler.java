package mnf.android.wearlock.misc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by muneef on 17/06/17.
 */

public class PreferensHandler {

    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context c;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "settings_pref";
    final String wear_lock_enable = "enable_lock";
    final String bluetooth_lock_enable = "blue_lock";
    final String phone_ring_enable = "ring_phone";


    @SuppressLint("CommitPrefEdits")
    public PreferensHandler(Context context) {
        this.c = context;
        pref = c.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setWearLockEnable(Boolean var){
        editor.putBoolean(wear_lock_enable, var);
        editor.commit();
    }

    public Boolean getWearLockEnable(){
        return pref.getBoolean(wear_lock_enable, true);
    }

    public void setBluetoothLock(Boolean var){
        editor.putBoolean(bluetooth_lock_enable, var);
        editor.commit();
    }

    public Boolean getBluetoothLock(){
        return pref.getBoolean(bluetooth_lock_enable, true);
    }


    public void setPhoneRignEnable(Boolean var){
        editor.putBoolean(phone_ring_enable, var);
        editor.commit();
    }

    public Boolean getPhoneRignEnable(){
        return pref.getBoolean(phone_ring_enable, true);
    }


}
