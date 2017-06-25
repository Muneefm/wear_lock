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
import mnf.android.wearlock.misc.PreferensHandler;

public class LaunchActivity extends MaterialIntroActivity {

    Context c;
    PreferensHandler pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;
        pref = new PreferensHandler(c);
        if(!pref.getFirstTimeUser()){
            if(!getIntent().hasExtra("show_intro")) {
                Intent mainAct = new Intent(LaunchActivity.this, PreferanceActivity.class);
                startActivity(mainAct);
                finish();
            }
        }

        new DeviceAdmin().setDeviceAdminCallback(new DeviceAdminCallback() {
            @Override
            public void onAdminEnabled() {

                Intent mainAct = new Intent(LaunchActivity.this,PreferanceActivity.class);
                startActivity(mainAct);
                finish();
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
                        .description(getString(R.string.launch_string_one))
                        .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.teal400)
                .buttonsColor(R.color.teal500)
                .image(R.mipmap.intro_lock)
                .title("Lock your device Remotely !")
                .description(getString(R.string.launch_desc_two))
                .build());


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.curious_blue)
                        .buttonsColor(R.color.blue800)
                        .image(R.mipmap.icon)
                        .title("Please provide admin Permission.")
                        .description(getString(R.string.launch_desc_three))
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
