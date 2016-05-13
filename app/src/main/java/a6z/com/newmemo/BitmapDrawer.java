package a6z.com.newmemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 绘制位图辅助类.
 */
public class BitmapDrawer {
    private static Bitmap bitmap;

    public static Bitmap getFilledRect(int width, int height, int color) {
        if (bitmap == null) {
            bitmap = makeBitmap(width, height, color);
        }
        return bitmap;
    }

    private static Bitmap makeBitmap(int width, int height, int color) {
        // 新建一个bitmap，相当于传入进来的bitmap的copy
        Bitmap backgroundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 建立画布
        Canvas canvas = new Canvas(backgroundBitmap);
        // 建立画笔
        Paint paint = new Paint();
        // 颜色
        paint.setColor(color);
        //填充
        paint.setStyle(Paint.Style.FILL);
        // 无锯齿
        paint.setAntiAlias(true);
        // 矩形
        RectF rect = new RectF(0, 0, width, height);
        // 画矩形
        canvas.drawRect(rect, paint);

        return backgroundBitmap;
    }
}
