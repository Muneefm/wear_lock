package mnf.android.wearlock;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.CircularButton;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mnf.android.wearlock.fragment.LockFragment;
import mnf.android.wearlock.fragment.RingFragment;

import static mnf.android.wearlock.AppController.c;

public class MainActivity extends FragmentActivity {

    private TextView mTextView;
    CircularButton lockBtn;
    ViewPager viewPager;
    ScreenSlidePagerAdapter adapterPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //lockBtn = (CircularButton) findViewById(R.id.lock_btn);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        adapterPager = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterPager);

    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new LockFragment().newInstance("","");
            }else{
                return new RingFragment().newInstance("","");
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
