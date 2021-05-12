package digital.pedda.sternenwarte;

import android.view.View;

public class Utils {
    public static String getResourceName(View view) {
        if (view.getId() == View.NO_ID) return null;
        else return view.getResources().getResourceEntryName(view.getId());
    }
}
