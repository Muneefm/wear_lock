package mnf.android.wearlock;

import android.app.Application;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;

import mnf.android.wearlock.Tools.DeviceAdmin;
import mnf.android.wearlock.Tools.WearListener;
import mnf.android.wearlock.misc.BroadCast;

/**
 * Created by muneef on 10/06/17.
 */

public class ApplicationController extends Application implements NavigationView.OnNavigationItemSelectedListener,DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

   static Context c ;
    DevicePolicyManager mDevicePolicyManager;
    private GoogleApiClient mGoogleApiClient;
    private int count = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        c = this;
        mDevicePolicyManager  = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        registerReceiver(new BroadCast(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        Log.e("ApplicationController","Application controller last ");
    }


    public void lockDevice(){
    if(isAdminActive()){
        mDevicePolicyManager.lockNow();
    }else{
        Log.e("lock","device does not have admin access");
    }
    }
    public boolean isAdminActive(){
        if(c==null) {
            Log.e("lock","is admin active  context null");

        }
        mDevicePolicyManager  = (DevicePolicyManager)c.getSystemService(Context.DEVICE_POLICY_SERVICE);
            return mDevicePolicyManager.isAdminActive(new ComponentName(c, DeviceAdmin.class));

    }
    public boolean enableDeviceAdmin(Context c) {
        Log.e("TAG","enableDeviceAdmin called "+new ApplicationController().isAdminActive());
        // Launch the activity to have the user enable our admin.

        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(c, DeviceAdmin.class));
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                c.getString(R.string.test_string));
        c.startActivity(intent);
        // return false - don't update checkbox until we're really active
        return false;
   /* } else {
        mDPM.removeActiveAdmin(mDeviceAdminSample);
        enableDeviceCapabilitiesArea(false);
        mAdminActive = false;
    }*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("lock","onConnected google client");
        Wearable.DataApi.addListener(mGoogleApiClient, this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.e("lock","onDataChanged appcontroller 1 google client");
        lockDevice();
    }
}
