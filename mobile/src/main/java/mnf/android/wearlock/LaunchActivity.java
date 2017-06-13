package mnf.android.wearlock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import mnf.android.wearlock.Interfaces.DeviceAdminCallback;
import mnf.android.wearlock.Tools.DeviceAdmin;

public class LaunchActivity extends MaterialIntroActivity {

    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;
        new DeviceAdmin().setDeviceAdminCallback(new DeviceAdminCallback() {
            @Override
            public void onAdminEnabled() {

                Intent mainAct = new Intent(LaunchActivity.this,MainActivity.class);
                startActivity(mainAct);
            }
        });


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.first_slide_background)
                        .buttonsColor(R.color.first_slide_buttons)
                        .image(R.mipmap.intro_phone)
                        .title("What's the catch ? ")
                        //.description("An easy way to lock your phone from wear ")
                        .description("\nLet me explain it this way, Imagine a situation \n where your phone is in  someone else's hand,\n may be your friend, but you are worried \n if he/she is gonna  see something they aren't suppose to.\n  ")
                        .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.teal400)
                .buttonsColor(R.color.teal500)
                .image(R.mipmap.intro_lock)
                .title("Lock your device Remotely !")
                .description("\nOpen \"Wear lock\" app in your Wear device \n and press Lock button. that's it. \n your phone is locked Remotely")
                .build());


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.purple500)
                        .buttonsColor(R.color.purple800)

                        .image(R.mipmap.main_icon)
                        .title("We provide best solution ")
                        .description("\nBut according to android policy \nThe app need Admin privilege to lock the device. ")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!new ApplicationController().isAdminActive()) {
                            new ApplicationController().enableDeviceAdmin(c);
                        }else{
                            Intent mainAct = new Intent(LaunchActivity.this,MainActivity.class);
                            startActivity(mainAct);
                        }
                    }
                }, "Give Admin Privilege"));

   /*     addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.pink800)
                .buttonsColor(R.color.grey500)
                .title("That's it")
                .description("Would you join us?")
                .build());
*/

    }

}
