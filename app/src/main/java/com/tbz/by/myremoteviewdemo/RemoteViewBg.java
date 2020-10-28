package com.tbz.by.myremoteviewdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by leeby on 2017/6/27.
 */

public class RemoteViewBg {
    private Bitmap bitmapBg;
    public RemoteViewBg(Bitmap bitmap) {
        bitmapBg = bitmap;
    }

    //游戏背景的绘图函数
    public void draw(Canvas canvas, Paint paint, Rect src0 , Rect dst0 ) {
//        bitmap：画什么图片
//        src：要画图片中的哪个部分
//        dst：要把图片画到画布中的哪个位置
//        paint：画笔
        canvas.drawBitmap(bitmapBg, src0, dst0, paint);
    }
}
