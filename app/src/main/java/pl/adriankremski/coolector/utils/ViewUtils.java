package pl.adriankremski.coolector.utils;

import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {

    public static void setViewsEnabledInHierarchy(View view, boolean enabled) {
        if (view instanceof ViewGroup) {
            ViewGroup viewHierarchy = (ViewGroup) view;
            for (int i = 0; i < viewHierarchy.getChildCount(); ++i) {
                setViewsEnabledInHierarchy(viewHierarchy.getChildAt(i), enabled);
            }
        } else {
            view.setEnabled(enabled);
            view.setClickable(enabled);
        }
    }
}
