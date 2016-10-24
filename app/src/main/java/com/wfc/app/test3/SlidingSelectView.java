package com.wfc.app.test3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;

import com.bumptech.glide.Glide;
import com.wfc.app.test3.utils.DensityUtils;


public class SlidingSelectView extends ViewGroup {

    int[] photos = {
        R.mipmap.d1, R.mipmap.d2, R.mipmap.d3, R.mipmap.d4
    };

    private static final String TAG = "SlidingSelectView";

    int photoL, photoT, photoR, photoB, photoSize;

    int width, height;

    ImageView photoIv;

    private float startY;

    private float downY;

    private int position;
//    Scroller mScroller;


    public SlidingSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        mScroller = new Scroller(context);
    }

    void initPhotoIv() {
        photoIv = new ImageView(getContext());
//        photoIv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        LayoutParams lp = new LayoutParams(
                DensityUtils.dip2px(getContext(), photoSize),
                DensityUtils.dip2px(getContext(), photoSize));
        photoIv.setLayoutParams(lp);
        addView(photoIv);
        setOnTouchListener(photoTouchListener);
        nextPhoto();
    }

    void initSize() {
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        Log.e(TAG, "get w h->" + width + " " + height);
//        photoSize = width - DensityUtils.dip2px(getContext(), 30 * 2);
        photoSize = DensityUtils.dip2px(getContext(), 240);
        photoL = (width - photoSize) / 2;
        photoR = width - photoL;
        photoT = (height - photoSize) /2;
        photoB = height - photoT;
        Log.e(TAG, "get photo size->" + photoL + " " + photoR
                + " " + photoT + " " + photoB);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(width==0 || height==0) {
            initSize();
            initPhotoIv();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed) {
            Log.e(TAG, "l t r b->" + l + " " + t + " " + r + " " + b);
            photoIv.layout(photoL, photoT, photoR, photoB);
        }
    }

    private OnTouchListener photoTouchListener = (v, event) -> {
        Log.e(TAG, "getAction " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "y " + startY);
                startY = event.getY();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                Log.e(TAG, "y " + y);
//                float offY = y - startY;
//                photoT = photoT + (int) offY;
//                photoB = photoT + photoSize;
                photoIv.setY(photoIv.getY() + (y - startY));
                startY = y;
//                photoIv.layout(photoL, photoT, photoR, photoB);
//                scrollBy(0,- (int) offY);
                break;
            case MotionEvent.ACTION_UP:
                float offY1 = event.getY() - downY;
                if(Math.abs(offY1) > DensityUtils.dip2px(getContext(), 150)) {
                    //如果滑动超过20dp
                    if(offY1 > 0) {
                        //不感兴趣
                        Log.e(TAG, "不感兴趣");
                        sliding(0);
                    } else {
                        Log.e(TAG, "感兴趣");
                        sliding(1);
                    }
                } else {
                    //开启滑动,让其回到原点
//                    mScroller.startScroll(getScrollX(),
//                            getScrollY(),
//                            -getScrollX() ,-getScrollY());
//                    photoIv.setY(photoT);
                    back();
                }
                break;
        }
        return true;
    };

    /**
     *
     * @param v 1上
     */
    void sliding(int v) {
//        ValueAnimator valueAnimator = new ValueAnimator();
//        valueAnimator.setDuration(200);
//        valueAnimator.setObjectValues(new PointF(0, 0));
//        valueAnimator.setInterpolator(new LinearInterpolator());
//        valueAnimator.setEvaluator(new TypeEvaluator<PointF>()
//        {
//            // fraction = t / duration
//            @Override
//            public PointF evaluate(float fraction, PointF startValue,
//                                   PointF endValue)
//            {
//                Log.e(TAG, fraction * 3 + "");
//                // x方向200px/s ，则y方向0.5 * 10 * t
//                PointF point = new PointF();
//                float fy = fraction;
//                if(v==0) {
//                    fy = 1 - fraction;
//                }
////                point.x = photoL;
//                point.y = 200 * fy * 3;
//                return point;
//            }
//        });
//        valueAnimator.start();
//        valueAnimator.addUpdateListener(animation -> {
//            PointF point = (PointF) animation.getAnimatedValue();
////            photoIv.setX(point.x);
//            photoIv.setY(point.y);
////            float cVal = animation.getAnimatedFraction();
////            photoIv.setAlpha(1 - cVal);
//        });
        float destY;
        if(v==1) {
            destY = -(photoSize/2);
        } else {
            destY = height + (photoSize/2);
        }
        ObjectAnimator anim = ObjectAnimator
                .ofFloat(photoIv, "", photoIv.getY(), destY)
                .setDuration(200);
        anim.start();
        anim.addUpdateListener(animation -> {
            float cVal = (Float) animation.getAnimatedValue();
            photoIv.setY(cVal);
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                photoIv.setVisibility(View.GONE);
                //让其回到原点
//                photoT = (height - photoSize) /2;
//                photoB = height - photoT;
//                Log.e(TAG, "onAnimationEnd->" + photoL + " " + photoR
//                        + " " + photoT + " " + photoB);
                photoIv.setY(photoT);
//                mScroller.startScroll(getScrollX(),
//                        getScrollY(),
//                        -getScrollX() ,-getScrollY());
//                requestLayout();
                photoIv.setVisibility(VISIBLE);
                nextPhoto();
                initAnim();
//                photoIv.setAlpha(1.0f);
            }
        });
    }

    void initAnim() {
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(photoIv, "", 0.6f, 1)
                .setDuration(300);
        anim.start();
        anim.addUpdateListener(animation -> {
            float cVal = (Float) animation.getAnimatedValue();
            photoIv.setAlpha(cVal);
            photoIv.setScaleX(cVal);
            photoIv.setScaleY(cVal);
        });
    }

    void nextPhoto() {
        Glide.with(getContext())
                .load(photos[position++])
                .placeholder(R.mipmap.ic_launcher)
                .into(photoIv);
    }

    void back() {
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(photoIv, "", photoIv.getY(), photoT)
                .setDuration(200);
        anim.start();
        anim.addUpdateListener(animation -> {
            float cVal = (Float) animation.getAnimatedValue();
            photoIv.setY(cVal);
        });
    }

//    public void computeScroll() {
//        super.computeScroll();
//        if(mScroller.computeScrollOffset()){
//            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
//        }
//        invalidate();//必须要调用
//    }
}
