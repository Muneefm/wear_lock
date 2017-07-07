package mnf.android.wearlock;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.lang.reflect.Modifier;
import java.util.List;

import mnf.android.wearlock.Activity.PreferanceActivity;
import mnf.android.wearlock.Interfaces.WearNodeApiListener;
import mnf.android.wearlock.Tools.DeviceAdmin;
import mnf.android.wearlock.Tools.WearListener;
import mnf.android.wearlock.misc.BroadCast;
import mnf.android.wearlock.misc.PreferensHandler;

/**
 * Created by muneef on 10/06/17.
 */

public class ApplicationController extends Application implements NavigationView.OnNavigationItemSelectedListener,DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

   static Context c ;
    DevicePolicyManager mDevicePolicyManager;
    private static GoogleApiClient mGoogleApiClient;
    private int count = 0;
    static WearNodeApiListener mNodeListener;
    static PreferensHandler pref;
   static Ringtone defaultRingtone;
   static Uri defaultRintoneUri;
    @Override
    public void onCreate() {
        super.onCreate();
        c = this;
        pref = new PreferensHandler(c);
        mDevicePolicyManager  = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        registerReceiver(new BroadCast(), new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        Log.e("ApplicationController","Application controller last ");
        Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(c.getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        defaultRingtone = RingtoneManager.getRingtone(c, defaultRintoneUri);

    }


    public void getWearDeviceConected(){

        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                List<Node> nodes = getConnectedNodesResult.getNodes();
                Log.e("TAG","getWearDeviceConected Result "+nodes.size());

                if(mNodeListener!=null) {
                    Log.e("TAG","mNodeListener is not null");
                    mNodeListener.onNodeConnected(nodes);
                }else{
                    Log.e("TAG","mNodeListener is null");
                }
            }
        });
    }

    public void setWearNodeListener(WearNodeApiListener mNodeListener){
        this.mNodeListener = mNodeListener;
        getWearDeviceConected();
    }

    public boolean lockDevice(){
    if(isAdminActive()){
        mDevicePolicyManager.lockNow();
        return true;
    }else{
        Log.e("lock","device does not have admin access or preference is false = "+pref.getWearLockEnable());
        return false;
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
                c.getString(R.string.admin_warning_content));
        c.startActivity(intent);
        // return false - don't update checkbox until we're really active
        return false;
   /* } else {
        mDPM.removeActiveAdmin(mDeviceAdminSample);
        enableDeviceCapabilitiesArea(false);
        mAdminActive = false;
    }*/
    }

    public void disableDeviceAdmin(Context c){
        if(isAdminActive()){
            mDevicePolicyManager.removeActiveAdmin(new ComponentName(c, DeviceAdmin.class));
        }

    }

    public void showDialogue(final Context c,String title,String content,String buttonText,boolean isCancallable ){
        new MaterialDialog.Builder(c)
                .title( title)
                .content(content)
                .positiveText(buttonText).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
               // startBrowser("https://play.google.com/store/apps/details?id=com.utorrent.client",c);
                enableDeviceAdmin(c);
            }
        }).iconRes(R.mipmap.ic_launcher)
                .positiveColor(c.getResources().getColor(R.color.white))
                .contentColor(c.getResources().getColor(R.color.blue_grey800))
                .backgroundColor(c.getResources().getColor(R.color.white))
                .titleColorRes(R.color.black)
                .cancelable(isCancallable)
                .btnSelector(R.drawable.md_btn_selector_custom, DialogAction.POSITIVE)
                .show();
    }

    public  void startRingPhone(boolean isRing){
        Log.e("lock","startRingPhone "+isRing);
if(!isRing){
    if(defaultRingtone!=null){
        if(defaultRingtone.isPlaying()){
            defaultRingtone.stop();
            notificationPref(false);
        }
    }
}else {
    try {
        /*    Log.e("lock","startRingPhone "+isRing);

            Uri path = Uri.parse("android.resource://"+getPackageName()+"/raw/sound.mp3");
            // The line below will set it as a default ring tone replace
            // RingtoneManager.TYPE_RINGTONE with RingtoneManager.TYPE_NOTIFICATION
            // to set it as a notification tone
            RingtoneManager.setActualDefaultRingtoneUri(
                    getApplicationContext(), RingtoneManager.TYPE_RINGTONE,
                    path);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), path);
            r.play();*/

        if (defaultRingtone != null) {
            if (!defaultRingtone.isPlaying()) {
                defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(c.getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
                defaultRingtone = RingtoneManager.getRingtone(c, defaultRintoneUri);
                defaultRingtone.play();
                notificationPref(true);
            } else {
                defaultRingtone.stop();
                notificationPref(false);

            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

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
     /*   if(pref.getWearLockEnable()) {
            lockDevice();
        }else{
            startRingPhone(true);
        }
*/


        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEventBuffer);
        for (DataEvent event : events) {
            final Uri uri = event.getDataItem().getUri();
            final String path = uri != null ? uri.getPath() : null;
            final DataMap map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

            if ("/wear_lock_mnf".equals(path)) {
                if(pref.getWearLockEnable()) {
                    lockDevice();
                }
            }
            else if("/ring_phone_mnf".equals(path)){
                if(pref.getPhoneRignEnable()) {
                    startRingPhone(true);
                }
            }

        }
    }

    public void notificationPref(boolean show){
        if(show) {
            Intent intent = new Intent(this, PreferanceActivity.class);
            intent.putExtra("notification","1");
// use System.currentTimeMillis() to have a unique ID for the pending intent
            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
            Notification n = new Notification.Builder(this)
                    .setContentTitle("Ring !!")
                    .setContentText("Ring ring from wear")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_notification, "Stop", pIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .build();


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, n);
        }else{
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.cancel(0);
        }
    }
}
