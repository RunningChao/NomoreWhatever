
package com.yahoo.android.nomorewhatever.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.yahoo.android.nomorewhatever.model.Place;

import java.util.List;
import java.util.Random;

public class LuckyPanView extends SurfaceView implements SurfaceHolder.Callback, Runnable, View.OnTouchListener {

    public interface onClickLuckyPanListener {
        void onClickLuckyPan(int luckyId);
    }

    private SurfaceHolder mHolder;
    /**
     * 与SurfaceHolder绑定的Canvas
     */
    private Canvas mCanvas;
    /**
     * 用于绘制的线程
     */
    private Thread t;
    /**
     * 线程的控制开关
     */
    private boolean isRunning;

    private boolean mIsClickOnTargetItem = false;

    /**
     * 盘块的个数
     */
    private int mItemCount = 6;

    private String[] mStrs = new String[mItemCount];    //抽獎的文字
    private int[] mImgs = new int[mItemCount];          //抽獎的文字對應的图片
//    private int[] mColors = new int[mItemCount];        //抽獎的文字對應的顏色

//    private int[] COLOR_ENUM = new int[] {
//            0xFFFFC300, 0xFFF17E01, 0xFFFFFF00,
//            0x00000001, 0xFFFFC300, 0xFFF17E01
//    };

    /**
     * 每个盘块的颜色
     */
    private int[] mColors = new int[] {
            0xFFFFC300, 0xFFF17E01, 0xFFFFC300,
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01
    };
    public void setPlacesonBoard(List<Place> places){
        Log.d("Debug", String.valueOf(places.size()));
        for(int i =0;i<places.size();i++){
            mStrs[i]=places.get(i).getName();
            mImgs[i]=places.get(i).getImageResourceId(getContext());
            //mColors[i]=COLOR_ENUM[i];
        }
        if(places.size()<mItemCount){
            for(int i =places.size();i<mItemCount;i++){
                Random r = new Random();
                int index = r.nextInt(places.size()) + 0;
                mStrs[i]=places.get(index).getName();
                mImgs[i]=places.get(index).getImageResourceId(getContext());
                //mColors[i]=COLOR_ENUM[index];
            }
        }

    }



    /**
     * 与文字对应图片的bitmap数组
     */
    private Bitmap[] mImgsBitmap;

    /**
     * TODO: refactor mStrs, mColors, mImgs and mItemCount
     */

    /**
     * 绘制盘块的范围
     */
    private RectF mRange = new RectF();
    /**
     * 圆的直径
     */
    private int mRadius;
    /**
     * 绘制盘快的画笔
     */
    private Paint mArcPaint;

    /**
     * 绘制文字的画笔
     */
    private Paint mTextPaint;

    /**
     * 滚动的速度
     */
    private double mSpeed;
    private volatile float mStartAngle = 0;
    /**
     * 是否点击了停止
     */
    private boolean isShouldEnd;

    private boolean isStart = false;

    private boolean mHasAnswer = false;

    /**
     * 控件的中心位置
     */
    private int mCenter;
    /**
     * 控件的padding，这里我们认为4个padding的值一致，以paddingleft为标准
     */
    private int mPadding;

    /**
     * 背景图的bitmap
     */
    // private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(),
    // R.drawable.bg2);

    private int mLuckyTargetIndex;

    private onClickLuckyPanListener mListener;

    /**
     * 文字的大小
     */
    private float mTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

    public LuckyPanView(Context context)
    {
        this(context, null);
    }

    public LuckyPanView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHolder = getHolder();
        mHolder.addCallback(this);
        setOnTouchListener(this);

        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

    }

    /**
     * 设置控件为正方形
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        // 获取圆形的直径
        mRadius = width - getPaddingLeft() - getPaddingRight();
        // padding值
        mPadding = getPaddingLeft();
        // 中心点
        mCenter = width / 2;
        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 初始化绘制圆弧的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        // 初始化绘制文字的画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(0xFFffffff);
        mTextPaint.setTextSize(mTextSize);
        // 圆弧的绘制范围
        mRange = new RectF(getPaddingLeft(), getPaddingLeft(), mRadius
                + getPaddingLeft(), mRadius + getPaddingLeft());

        // 初始化图片
        mImgsBitmap = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(),
                    mImgs[i]);
        }

        // 开启线程
        isRunning = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 通知关闭线程
        isRunning = false;
    }

    @Override
    public void run() {
        // 不断的进行draw
        while (isRunning) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void draw() {
        try {
            // 获得canvas
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                // 绘制背景图
                drawBg();

                /**
                 * 绘制每个块块，每个块块上的文本，每个块块上的图片
                 */
                float tmpAngle = mStartAngle;
                float sweepAngle = (float) (360 / mItemCount);
                for (int i = 0; i < mItemCount; i++) {
                    // 绘制快快
                    if (mIsClickOnTargetItem && i == mLuckyTargetIndex && mHasAnswer) {
                        /**
                         * TODO: 客製化點下去的style
                         */
                        mArcPaint.setColor(mColors[i]);
                        mArcPaint.setAlpha(150);
                    } else {
                        mArcPaint.setColor(mColors[i]);
                    }
                    mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true,
                            mArcPaint);
                    // 绘制文本
                    drawText(tmpAngle, sweepAngle, mStrs[i]);
                    // 绘制Icon
                    drawIcon(tmpAngle, i);

                    tmpAngle += sweepAngle;
                }

                // 如果mSpeed不等于0，则相当于在滚动
                mStartAngle += mSpeed;

                // 点击停止时，设置mSpeed为递减，为0值转盘停止
                if (isShouldEnd) {
                    mSpeed -= 1;
                }
                if (mSpeed <= 0) {
                    mSpeed = 0;
                    isShouldEnd = false;
                }
                // 根据当前旋转的mStartAngle计算当前滚动到的区域
                // calInExactArea(mStartAngle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }

    }

    /**
     * 根据当前旋转的mStartAngle计算当前滚动到的区域 绘制背景，不重要，完全为了美观
     */
    private void drawBg() {
        mCanvas.drawColor(0xFFFFFFFF);
        /*
         * mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2, mPadding / 2, getMeasuredWidth() - mPadding / 2,
         * getMeasuredWidth() - mPadding / 2), null);
         */
    }

    /**
     * 绘制文本
     *
     * @param startAngle
     * @param sweepAngle
     * @param string
     */
    private void drawText(float startAngle, float sweepAngle, String string) {
        Path path = new Path();
        path.addArc(mRange, startAngle, sweepAngle);
        float textWidth = mTextPaint.measureText(string);
        // 利用水平偏移让文字居中
        float hOffset = (float) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);// 水平偏移
        float vOffset = mRadius / 2 / 6;// 垂直偏移
        mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
    }

    /**
     * 绘制图片
     *
     * @param startAngle
     * @param i
     */
    private void drawIcon(float startAngle, int i) {
        // 设置图片的宽度为直径的1/8
        int imgWidth = mRadius / 6;

        float angle = (float) ((30 + startAngle) * (Math.PI / 180));

        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));

        // 确定绘制图片的位置
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth
                / 2, y + imgWidth / 2);

        if (mImgsBitmap[i] != null) {
            mCanvas.drawBitmap(mImgsBitmap[i], null, rect, null);
        }

    }

    /**
     * 点击开始旋转
     *
     * @param luckyIndex
     */
    public void luckyStart(int luckyIndex) {
        isStart = true;
        mHasAnswer = false;
        mLuckyTargetIndex = luckyIndex;

        // 每项角度大小
        float angle = (float) (360 / mItemCount);
        // 中奖角度范围（因为指针向上，所以水平第一项旋转到指针指向，需要旋转210-270；）
        float from = 270 - (luckyIndex + 1) * angle;
        float to = from + angle;
        // 停下来时旋转的距离
        float targetFrom = 4 * 360 + from;
        /**
         * <pre>
         *  (v1 + 0) * (v1+1) / 2 = target ;
         *  v1*v1 + v1 - 2target = 0 ;
         *  v1=-1+(1*1 + 8 *1 * target)/2;
         * </pre>
         */
        float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetFrom) - 1) / 2;
        float targetTo = 4 * 360 + to;
        float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;

        mSpeed = (float) (v1 + 0.5 * (v2 - v1));
        isShouldEnd = false;
    }

    public void luckyEnd() {
        mStartAngle = 0;
        isShouldEnd = true;
        isStart = false;
        mHasAnswer = true;
    }

    public boolean isStart() {
        return isStart;
    }

    public boolean isShouldEnd() {
        return isShouldEnd;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mHasAnswer) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    float clickX = event.getX();
                    float clickY = event.getY();
                    float width = getWidth();
                    float height = getHeight();

                    float transY = clickX - width / 2;
                    float transX = height / 2 - clickY;
                    double theta = 90 - (180 / mItemCount);
                    if ((-Math.sin(theta) * transX + Math.cos(theta) * transY) >= 0
                            && (-Math.sin(theta) * transX + -Math.cos(theta) * transY) >= 0) {
                        mIsClickOnTargetItem = true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if (mIsClickOnTargetItem == true) {
                        if (mListener != null) {
                            mListener.onClickLuckyPan(mLuckyTargetIndex);
                        }
                        mIsClickOnTargetItem = false;
                    }
                    break;
            }
            return true;
        }

        return false;
    }

    public onClickLuckyPanListener getListener() {
        return mListener;
    }

    public void setListener(onClickLuckyPanListener mListener) {
        this.mListener = mListener;
    }
}
