package a6z.com.newmemo.Utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by liuzhiguo on 16/5/22.
 */
public class StringUtil {
    public static Spanned getUnderlineString(String text) {
        return Html.fromHtml("<u>" + text + "</u>");
    }
}
