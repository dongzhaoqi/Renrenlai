package com.siti.renrenlai.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class ImageHelper {
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    
    
    /**  
     * 图片缩放  
     * @param originalBitmap 原始的Bitmap  
     * @param newWidth 自定义宽度  
     * @param newHeight 自定义高度
     * @return 缩放后的Bitmap  
     */  
    public static Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){  
        int width = originalBitmap.getWidth();  
        int height = originalBitmap.getHeight();  
        //定义欲转换成的宽、高  
//          int newWidth = 200;  
//          int newHeight = 200;  
        //计算宽、高缩放率  
        float scanleWidth = (float)newWidth/width;  
        float scanleHeight = (float)newHeight/height;  
        //创建操作图片用的matrix对象 Matrix  
        Matrix matrix = new Matrix();  
        // 缩放图片动作  
        matrix.postScale(scanleWidth,scanleHeight);  
        //旋转图片 动作  
        //matrix.postRotate(45);  
        // 创建新的图片Bitmap  
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);  
        return resizedBitmap;  
    }  
}