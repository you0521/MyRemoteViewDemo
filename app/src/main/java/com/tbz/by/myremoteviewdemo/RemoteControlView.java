package com.tbz.by.myremoteviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by leeby on 2017/7/19.
 */

public class RemoteControlView extends View {
    private float scale = this.getResources().getDisplayMetrics().density;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private float posX;
    private float posY;

    private int minRadius = 0;

    private Bitmap bitmap;
    private RemoteViewBg remoteViewBg;
    private Rect src;
    private Matrix matrix;
    private Bitmap bitmap1;
    private int nBitmapWidth;
    private int nBitmapHeight;
    private int RockerCircleX;
    private int RockerCircleY;
    private float tempRad = 0;
    private Rect dst;
    private Bitmap bitmapInner;
    private Rect srcInner;
    private RemoteViewBg innerBg;
    private Rect dstInner;
    private boolean isStart = false;
    private Timer timer;
    private TimerTask timerTask;
    private int currentCmd;
    private boolean isSend;
    private Bitmap bitmap2;
    private Rect srcNoActive;
    private Rect dstNoActive;



    public RemoteControlView(Context context) {
        super(context);
    }

    public RemoteControlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        //画笔抗锯齿
        mPaint.setAntiAlias(true);
        //四个方向图片
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.control_rocker_arrow);
        //中间可滑动的操纵杆
        bitmapInner = BitmapFactory.decodeResource(getResources(), R.mipmap.control_rocker_paws);
        //滑动操纵杆的效果，外围出现的箭头
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.control_rocker_bg);
        //圆环
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.control_rocker_not_active);

        //  获取图片高度和宽度
        nBitmapWidth = bitmap1.getWidth();
        nBitmapHeight = bitmap1.getHeight();
        //这个构造方法仅仅是传入图片，后面会有画的方法。
        //画的是四个方向的图片
        remoteViewBg = new RemoteViewBg(bitmap);
        //创建几个矩阵
        src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        srcNoActive = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
        srcInner = new Rect(0, 0, bitmapInner.getWidth(), bitmapInner.getHeight());
        //也是传入图片，为了后期进行绘制
        innerBg = new RemoteViewBg(bitmapInner);
        matrix = new Matrix();


    }


    public RemoteControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        posX = mWidth / 2;
        posY = mHeight / 2;
        minRadius = mWidth / 6;
        //矩形，中间四个方向箭头的矩阵
        dst = new Rect((int) mWidth / 2 - bitmap.getWidth() / 2, (int) mHeight / 2 - bitmap.getHeight() / 2, (int) mWidth / 2 + bitmap.getWidth() / 2, (int) mHeight / 2 + bitmap.getHeight() / 2);
        //四个箭头外围圆环的矩阵
        dstNoActive = new Rect((int) mWidth / 2 - bitmap2.getWidth() / 2, (int) mHeight / 2 - bitmap2.getHeight() / 2, (int) mWidth / 2 + bitmap2.getWidth() / 2, (int) mHeight / 2 + bitmap2.getHeight() / 2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        matrix.reset();
        //画四个方向图片，其中src与dst都是个矩形，第一个是代表要画图片中的哪个部分，但二个是要将图片画在画布上的哪个位置
        remoteViewBg.draw(canvas, mPaint, src, dst);
        //同上，画中间操纵杆
        dstInner = new Rect((int) posX - minRadius, (int) posY - minRadius, (int) posX + minRadius, (int) posY + minRadius);
        innerBg.draw(canvas, mPaint, srcInner, dstInner);


        matrix.reset();
        //平移，向右，向下
        matrix.setTranslate(mWidth / 2 - bitmap1.getWidth() / 2, mHeight / 2 - bitmap1.getHeight() / 2);

        //判断是否拖动，如果拖动将外围箭头旋转
        if (tempRad != 0) {
            matrix.preRotate(tempRad + 90, (float) bitmap1.getWidth() / 2, (float) bitmap1.getHeight() / 2);  //要旋转的角度
        } else {
            matrix.preRotate(tempRad);
        }
        //判断是否显示外围的箭头
        if (isStart) {
            canvas.drawBitmap(bitmap1, matrix, null);
        } else {
            canvas.drawBitmap(bitmap2, srcNoActive, dstNoActive, null);
        }



        matrix.reset();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isStart = true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // 在圆形范围外触摸
            if (Math.sqrt(Math.pow((mWidth / 2 - (int) event.getX()), 2) + Math.pow((mWidth / 2 - (int) event.getY()), 2)) >= (mWidth / 2 - 50 * scale)) {
                tempRad = getRad(mWidth / 2, mHeight / 2, event.getX(), event.getY());
                getXY(mWidth / 2, mHeight / 2, bitmap.getWidth() / 2 - minRadius, tempRad);
                tempRad = getAngle(event.getX(), event.getY());

                Log.e("gogogo", "外: "+tempRad);
            } else {//圆形范围内触摸


                posX = event.getX();
                posY = event.getY();
                tempRad = getAngle(event.getX(), event.getY());
                Log.e("gogogo", "内: "+tempRad);
            }

            onItemClickListener.onTouch(tempRad);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isStart = false;
            posX = mWidth / 2;
            posY = mHeight / 2;
            tempRad = 0;

        }


        invalidate();
        return true;

    }


    private float getAngle(float xTouch, float yTouch) {
        RockerCircleX = mWidth / 2;
        RockerCircleY = mHeight / 2;
        return (float) (getRad(RockerCircleX, RockerCircleY, xTouch, yTouch) * 180f / Math.PI);

    }

    public void getXY(float x, float y, float R, double rad) {
        //获取圆周运动的X坐标
//        posX = (float) (R * Math.cos(rad)) + x;

        posX = (float) (R * Math.cos(rad)) + x;
        //获取圆周运动的Y坐标
//        posY = (float) (R * Math.sin(rad)) + y;
        posY = (float) (R * Math.sin(rad)) + y;


    }

    /***
     * 得到两点之间的弧度
     */
    public float getRad(float px1, float py1, float px2, float py2) {
        float x = px2 - px1;

        float y = py1 - py2;
        //斜边的长
        float z = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        float cosAngle = x / z;
        float rad = (float) Math.acos(cosAngle);

        if (py2 < py1) {
            rad = -rad;
        }
        return rad;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    //点击回调监听器
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onTouch(float item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}

