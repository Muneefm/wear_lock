package mnf.android.wearlock.Tools;

import android.util.Log;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by muneef on 10/06/17.
 */

public class WearListener extends WearableListenerService {

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        super.onDataChanged(dataEventBuffer);

        Log.e("lock","Phone on data changed");
        Log.e("lock","Phone on data changed");


    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.e("lock","Phone on data changed");
        super.onMessageReceived(messageEvent);
    }

    @Override
    public void onCreate() {
        Log.e("lock"," WearListener Phone on create");
        super.onCreate();
    }
}
