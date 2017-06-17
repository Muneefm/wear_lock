package mnf.android.wearlock;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;
import agency.tango.materialintroscreen.animations.ViewTranslationWrapper;
import mnf.android.wearlock.Activity.PreferanceActivity;
import mnf.android.wearlock.Interfaces.DeviceAdminCallback;
import mnf.android.wearlock.Tools.DeviceAdmin;
import mnf.android.wearlock.misc.LastSlide;

public class LaunchActivity extends MaterialIntroActivity {

    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;
        new DeviceAdmin().setDeviceAdminCallback(new DeviceAdminCallback() {
            @Override
            public void onAdminEnabled() {

                Intent mainAct = new Intent(LaunchActivity.this,PreferanceActivity.class);
                startActivity(mainAct);
            }

            @Override
            public void onAdminDisabled() {

            }
        });

        enableLastSlideAlphaExitTransition(true);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
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
                            Intent mainAct = new Intent(LaunchActivity.this,PreferanceActivity.class);
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


        //addSlide(new LastSlide());


    }

    @Override
    public ViewTranslationWrapper getNextButtonTranslationWrapper() {
        return super.getNextButtonTranslationWrapper();
    }
    @Override
    public void onFinish() {
        if(new ApplicationController().isAdminActive()) {
            Intent intent = new Intent(LaunchActivity.this,PreferanceActivity.class);
            startActivity(intent);
        }else{
           // showMessage("Please provide admin privilege ");
            new ApplicationController().enableDeviceAdmin(c);

        }
    }

}
