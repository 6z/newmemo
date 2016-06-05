package a6z.com.newmemo.Utils;

import android.text.Html;
import android.text.Spanned;
import android.util.Base64;

/**
 * Created by liuzhiguo on 16/5/22.
 */
public class StringUtil {
    public static Spanned getUnderlineString(String text) {
        return Html.fromHtml("<u>" + text + "</u>");
    }


    public static byte[] toBytes(String str) {
        byte[] bytes = Base64.decode(str, Base64.DEFAULT);
        return bytes;
    }
}
