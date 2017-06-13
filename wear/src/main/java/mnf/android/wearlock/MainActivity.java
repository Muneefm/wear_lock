package mnf.android.wearlock;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.CircularButton;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static mnf.android.wearlock.AppController.c;

public class MainActivity extends Activity {

    private TextView mTextView;
    CircularButton lockBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lockBtn = (CircularButton) findViewById(R.id.lock_btn);
        lockBtn.setImageDrawable(c.getResources().getDrawable(R.mipmap.ic_launcher));
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MainActivity","lock button clicked");
                new AppController().sendLockToDevice();
            }
        });
    }
}
