package mnf.android.wearlock.misc;

import agency.tango.materialintroscreen.SlideFragment;
import mnf.android.wearlock.ApplicationController;

/**
 * Created by muneef on 12/06/17.
 */

public class LastSlide extends SlideFragment {


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
