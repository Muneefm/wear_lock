package mnf.android.wearlock.misc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import agency.tango.materialintroscreen.SlideFragment;
import mnf.android.wearlock.ApplicationController;
import mnf.android.wearlock.R;

/**
 * Created by muneef on 12/06/17.
 */

public class LastSlide extends SlideFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.custom_slider, container, false);
        // = (CheckBox) view.findViewById(R.id.checkBox);
        Log.e("TAG","onCreateView custom fragment");
        return view;
    }


    @Override
    public int backgroundColor() {
        return R.color.purple500;
    }


    @Override
    public int buttonsColor() {
        return R.color.purple800;
    }

    @Override
    public boolean canMoveFurther() {
        if(new ApplicationController().isAdminActive()) {
            return true;
        }else return false;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "Please provide admin privilege to continue ";
    }

}
