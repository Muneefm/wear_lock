package mnf.android.wearlock.Tools;
import android.net.Uri;
import android.util.Log;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import java.util.List;
import mnf.android.wearlock.ApplicationController;
import mnf.android.wearlock.misc.PreferensHandler;

/**
 * Created by muneef on 10/06/17.
 */

public class WearListener extends WearableListenerService {

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        super.onDataChanged(dataEventBuffer);
        Log.e("lock","Phone on data changed");
        Log.e("lock","Phone on data changed");
        PreferensHandler pref = new PreferensHandler(getApplicationContext());
        if(pref!=null) {
            final List<DataEvent> events = FreezableUtils.freezeIterable(dataEventBuffer);
            for (DataEvent event : events) {
                final Uri uri = event.getDataItem().getUri();
                final String path = uri != null ? uri.getPath() : null;
                final DataMap map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();

                if ("/wear_lock_mnf".equals(path)) {
                    if (pref.getWearLockEnable()) {
                        new ApplicationController().lockDevice();
                    }
                } else if ("/ring_phone_mnf".equals(path)) {
                    if (pref.getPhoneRignEnable()) {
                        new ApplicationController().startRingPhone(true);
                    }
                }
            }
        }else{
            Log.e("TAG","Preference is null");
        }
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
