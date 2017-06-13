package mnf.android.wearlock.misc;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import mnf.android.wearlock.ApplicationController;

/**
 * Created by muneef on 12/06/17.
 */

public class BroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("TAG","onReceive BroadCast");


        String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            Log.e("TAG","onReceive blue action");

            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            switch(state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.e("TAG","onReceive blue off");
                    new ApplicationController().lockDevice();
                    break;
            }

        }


    }
}
