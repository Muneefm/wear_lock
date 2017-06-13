package mnf.android.wearlock.Tools;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import mnf.android.wearlock.Interfaces.DeviceAdminCallback;

/**
 * Created by muneef on 11/06/17.
 */

public class DeviceAdmin extends DeviceAdminReceiver {


     public static  DeviceAdminCallback mCallback;
    public  void setDeviceAdminCallback(DeviceAdminCallback mAdminCallback){
        Log.e("DeviceAdmin","setDeviceAdminCallback setting");

        this.mCallback = mAdminCallback;

    }
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.e("DeviceAdmin","AdminReciever enabled");
        if(mCallback!=null) {
            mCallback.onAdminEnabled();
        }


    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Log.e("DeviceAdmin","AdminReciever enabled");
    }

}
