package a6z.com.newmemo.Utils;

import android.text.Layout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by liuzhiguo on 16/5/24.
 */
public class LayoutUtil {
    public static void makeAutoHeight(TextView view) {
        int height_in_pixels = view.getLineCount() * view.getLineHeight(); //approx height text
        Layout layout = view.getLayout();
        int desired = layout.getLineTop(view.getLineCount());
        int padding = view.getCompoundPaddingTop() + view.getCompoundPaddingBottom();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = desired + padding;
        view.setLayoutParams(layoutParams);

        //view.getLayoutParams().height = desired + view.getCompoundPaddingTop() + view.getCompoundPaddingBottom();

    }
}

