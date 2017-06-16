package mnf.android.wearlock.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.wearable.Node;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mnf.android.wearlock.ApplicationController;
import mnf.android.wearlock.Interfaces.WearNodeApiListener;
import mnf.android.wearlock.R;

public class PreferanceActivity extends AppCompatActivity {
    @BindView(R.id.device_name)
    TextView deviceName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferance);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        new ApplicationController().setWearNodeListener(new WearNodeApiListener() {
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
