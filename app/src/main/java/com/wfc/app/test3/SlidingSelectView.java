package com.wfc.app.test3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wfc.app.test3.model.Photo;
import com.wfc.app.test3.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;


public class SlidingSelectView extends ViewGroup {

    private List<Photo> photos = new ArrayList<>();

    private static final String TAG = "SlidingSelectView";

    int photoL, photoT, photoR, photoB, photoLayoutWidth, photoLayoutHeight, photoSize;

    int width, height;

    ViewGroup photoLayout;

    ImageView photoIv;

    TextView titleTv;

    private float startY;

    private float downY;

    private int position;

    LayoutInflater layoutInflater;

    private Callback callback;

    public SlidingSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setOnTouchListener(photoTouchListener);
        photoLayoutWidth = DensityUtils.dip2px(getContext(), 280);
        photoSize = DensityUtils.dip2px(getContext(), 280);
        photoLayoutHeight = DensityUtils.dip2px(getContext(), 320);
        Log.e(TAG, "get w1 h1->" + photoLayoutWidth + " " + photoLayoutHeight);
        initPhotoLayout();
    }

    void setPhotos(List<Photo> photos) {
        this.photos = photos;
        position = 0;
        nextPhoto();
    }

    void initSize() {
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        Log.e(TAG, "get w h->" + width + " " + height);
        photoL = (width - photoLayoutWidth) / 2;
        photoR = width - photoL;
        photoT = (height - photoLayoutHeight) /2;
        photoB = height - photoT;
        Log.e(TAG, "get photo size->" + photoL + " " + photoR
                + " " + photoT + " " + photoB);
    }

    void initPhotoLayout() {
        photoLayout = (ViewGroup) layoutInflater.inflate(R.layout.layout_photo, null);
        LayoutParams lp = new LayoutParams(photoLayoutWidth, photoLayoutHeight);
        photoLayout.setLayoutParams(lp);
        addView(photoLayout);
        setOnTouchListener(photoTouchListener);
        photoIv = (ImageView) photoLayout.findViewById(R.id.iv_photo);
        photoIv.setImageResource(R.mipmap.d1);
        titleTv = (TextView) photoLayout.findViewById(R.id.tv_name);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //测量并保存layout的宽高(使用getDefaultSize时，wrap_content和match_perent都是填充屏幕)
        //稍后会重新写这个方法，能达到wrap_content的效果
        setMeasuredDimension( getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        if(width==0 || height==0) {
            initSize();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e(TAG, "l t r b->" + l + " " + t + " " + r + " " + b);
        if(changed) {
            Log.i(TAG, "onLayout: changed");
            photoLayout.layout(photoL, photoT, photoR, photoB);
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
                photoLayout.setY(photoLayout.getY() + (y - startY));
                startY = y;
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
        float destY;
        if(v==1) {
            destY = -(photoLayoutHeight/2);
        } else {
            destY = height + (photoLayoutHeight/2);
        }
        ObjectAnimator anim = ObjectAnimator
                .ofFloat(photoLayout, "", photoLayout.getY(), destY)
                .setDuration(200);
        anim.start();
        anim.addUpdateListener(animation -> {
            float cVal = (Float) animation.getAnimatedValue();
            photoLayout.setY(cVal);
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                photoLayout.setVisibility(View.GONE);
                //让其回到原点
                photoLayout.setY(photoT);
                photoLayout.setVisibility(VISIBLE);
                nextPhoto();
                initAnim();
            }
        });
    }

    void initAnim() {
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(photoLayout, "", 0.6f, 1)
                .setDuration(300);
        anim.start();
        anim.addUpdateListener(animation -> {
            float cVal = (Float) animation.getAnimatedValue();
            photoLayout.setAlpha(cVal);
            photoLayout.setScaleX(cVal);
            photoLayout.setScaleY(cVal);
        });
    }

    void nextPhoto() {
        if(photos!=null
                && photos.size()>0) {
            if(position < photos.size()) {
                Glide.with(getContext())
                        .load(Integer.parseInt(photos.get(position).getPhotoUrl()))
                        .placeholder(R.mipmap.ic_launcher)
                        .into(photoIv);

                position ++;
            } else {
                //数组越界
                if(callback!=null)
                    callback.onOver();
            }
        }
    }

    void back() {
        ObjectAnimator anim = ObjectAnimator//
                .ofFloat(photoLayout, "", photoLayout.getY(), photoT)
                .setDuration(200);
        anim.start();
        anim.addUpdateListener(animation -> {
            float cVal = (Float) animation.getAnimatedValue();
            photoLayout.setY(cVal);
        });
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    interface Callback {
        void onOver();
    }

}
