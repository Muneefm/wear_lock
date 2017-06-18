package mnf.android.wearlock;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.UUID;

/**
 * Created by muneef on 11/06/17.
 */

public class AppController extends Application implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    static Context c;
    static GoogleApiClient mApiClient;
    final static String TAG = "AppController";
    @Override
    public void onCreate() {
        super.onCreate();
        super.onCreate();

        c =  this;

        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();
        Log.e("lock","GoogleApiClient call");

    }

    public void sendLockToDevice(){
        Log.e("lock","sendLockToDevice");

if(mApiClient.isConnected()) {
Log.e("lock","google client connected");
    PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/wear_lock_mnf");
    putDataMapReq.getDataMap().putString("action", "device_lock-"+ UUID.randomUUID().toString());
    PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();
    PendingResult<DataApi.DataItemResult> pendingResult =
            Wearable.DataApi.putDataItem(mApiClient, putDataReq);
    Log.e("lock","send data from wear");
}else{
    Log.e("lock","google client not connected");

}
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("lock","onConnected call");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG,"onConnectionSuspended call");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG,"onConnectionFailed call");

    }
}
