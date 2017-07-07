package mnf.android.wearlock.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.wearable.Node;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mnf.android.wearlock.ApplicationController;
import mnf.android.wearlock.Interfaces.DeviceAdminCallback;
import mnf.android.wearlock.Interfaces.WearNodeApiListener;
import mnf.android.wearlock.LaunchActivity;
import mnf.android.wearlock.MainActivity;
import mnf.android.wearlock.R;
import mnf.android.wearlock.Tools.DeviceAdmin;
import mnf.android.wearlock.misc.PreferensHandler;

public class PreferanceActivity extends AppCompatActivity {
    @BindView(R.id.device_name)
    TextView deviceName;

    @BindView(R.id.lock_text)
    TextView enableLockTv;

    @BindView(R.id.lock_blue_text)
    TextView enableBlueLockTv;

    @BindView(R.id.alert_text)
    TextView enableRingLockTv;

   /* @BindView(R.id.demo_text)
    TextView demoTv;

*/
    @BindView(R.id.switch_lock)
    SwitchCompat switchLockWear;
    @BindView(R.id.switchCompat)
    SwitchCompat switchBluetoothLock;
    @BindView(R.id.switch_ring_phone)
    SwitchCompat switchRingPhone;

    @BindView(R.id.admin_enable_button)
    Button adminEnableBtn;
    @BindView(R.id.admin_disable_button)
    Button adminDisableBtn;
    @BindView(R.id.how_to_text)
            TextView tvHowTo;
   /* @SwitchCompat(R.id.demo_text)
    TextView demoTv;*/

    PreferensHandler pref;
    Context c;
    //private FirebaseAnalytics mFirebaseAnalytics;

    ApplicationController mAppController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferance);
        ButterKnife.bind(this);
        c = this;
        pref  = new PreferensHandler(c);
        pref.setFirstTimeUser(false);
        mAppController = new ApplicationController();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Jaldi-Regular.ttf");
        tvHowTo.setTypeface(face);
        enableLockTv.setTypeface(face);
        enableBlueLockTv.setTypeface(face);
        enableRingLockTv.setTypeface(face);
      //  demoTv.setTypeface(face);
        deviceName.setTypeface(face);
        //AdView adView = new AdView(this);
       // adView.setAdSize(AdSize.BANNER);
       // adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //tvMovie.setTypeface(face);
        switchLockWear.setChecked(pref.getWearLockEnable());
        switchBluetoothLock.setChecked(pref.getBluetoothLock());
        switchRingPhone.setChecked(pref.getPhoneRignEnable());

        Intent intent = getIntent();
        if(intent.hasExtra("notification")){
         String value = intent.getStringExtra("notification");
            new ApplicationController().startRingPhone(false);
            Log.e("TAG","inside android getExtra");
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(PreferanceActivity.this,LaunchActivity.class);
                launchIntent.putExtra("show_intro","1");
                startActivity(launchIntent);
                finish();
            }
        });
        switchLockWear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("TAG","lock wear switch callback");

                if(isChecked && !new ApplicationController().isAdminActive()){
                    // call dialogue
                    switchLockWear.setChecked(false);
                    new ApplicationController().showDialogue(c,getString(R.string.admin_warning),getString(R.string.admin_warning_content),"Enable admin",false);
                }else{
                    pref.setWearLockEnable(isChecked);
                }
            }
        });

        switchBluetoothLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("TAG","lock wear switch callback");

                if(isChecked && !new ApplicationController().isAdminActive()){
                    // call dialogue
                    switchBluetoothLock.setChecked(false);
                    new ApplicationController().showDialogue(c,getString(R.string.admin_warning),getString(R.string.admin_warning_content),"Enable admin",false);
                }else{
                    pref.setBluetoothLock(isChecked);
                }
            }
        });

        switchRingPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.setPhoneRignEnable(isChecked);
            }
        });

        if(mAppController.isAdminActive()){
            adminState(true);
        }else{
           adminState(false);
        }

        new DeviceAdmin().setDeviceAdminCallback(new DeviceAdminCallback() {
            @Override
            public void onAdminEnabled() {
                adminState(true);
                switchLockWear.setChecked(true);

            }

            @Override
            public void onAdminDisabled() {
                adminState(false);
                switchBluetoothLock.setChecked(false);
                switchLockWear.setChecked(false);


            }
        });
    }

    private void adminState(boolean isOn){
        if (isOn) {
            adminEnableBtn.setVisibility(View.GONE);
            adminDisableBtn.setVisibility(View.VISIBLE);
        }
        else{
            adminEnableBtn.setVisibility(View.VISIBLE);
            adminDisableBtn.setVisibility(View.GONE);
        }
        adminDisableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAppController!=null){
                        mAppController.disableDeviceAdmin(c);
                }
            }
        });
        adminEnableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppController.enableDeviceAdmin(c);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ApplicationController appInstance = new ApplicationController();
        if(c==null)
        c = getApplicationContext();
        if(!appInstance.isAdminActive()) {
            appInstance.showDialogue(c, getString(R.string.admin_warning), getString(R.string.admin_warning_content), "Enable admin", false);
        }
        appInstance.setWearNodeListener(new WearNodeApiListener() {
            @Override
            public void onNodeConnected(List<Node> nodeList) {
                Log.e("TAG","onNodeConnected node size = "+nodeList);
                if(nodeList.size()>0) {
                    String displayName = nodeList.get(0).getDisplayName();
                    if (deviceName != null)
                        deviceName.setText(displayName);
                }else{
                    deviceName.setText(R.string.no_device_connected);

                }

            }
        });
    }
}
