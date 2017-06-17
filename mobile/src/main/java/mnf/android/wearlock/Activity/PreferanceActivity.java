package mnf.android.wearlock.Activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.wearable.Node;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mnf.android.wearlock.ApplicationController;
import mnf.android.wearlock.Interfaces.WearNodeApiListener;
import mnf.android.wearlock.R;
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

    @BindView(R.id.demo_text)
    TextView demoTv;


    @BindView(R.id.switch_lock)
    SwitchCompat switchLockWear;
    @BindView(R.id.switchCompat)
    SwitchCompat switchBluetoothLock;
    @BindView(R.id.switch_ring_phone)
    SwitchCompat switchRingPhone;
   /* @SwitchCompat(R.id.demo_text)
    TextView demoTv;*/

    PreferensHandler pref;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferance);
        ButterKnife.bind(this);
        c = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref  = new PreferensHandler(c);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Jaldi-Regular.ttf");
        enableLockTv.setTypeface(face);
        enableBlueLockTv.setTypeface(face);
        enableRingLockTv.setTypeface(face);
        demoTv.setTypeface(face);
        deviceName.setTypeface(face);

        //tvMovie.setTypeface(face);
        switchLockWear.setChecked(pref.getWearLockEnable());
        switchBluetoothLock.setChecked(pref.getBluetoothLock());
        switchRingPhone.setChecked(pref.getPhoneRignEnable());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        switchLockWear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && !new ApplicationController().isAdminActive()){
                    // call dialogue
                    new ApplicationController().showDialogue(c,getString(R.string.admin_warning),getString(R.string.admin_warning_content),"Enable admin",false);
                }else{
                    pref.setWearLockEnable(isChecked);
                }
            }
        });

        switchBluetoothLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.setBluetoothLock(isChecked);
            }
        });

        switchRingPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pref.setPhoneRignEnable(isChecked);
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
