package com.wfc.app.test3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by wangfengchen on 2016/10/22.
 */

public class AnimTestActivity extends Activity {

    private static final String TAG = "AnimTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_test);
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(1000);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>()
        {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                                   PointF endValue)
            {
                Log.e(TAG, fraction * 3 + "");
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF();
                point.x = 0;
                point.y = 200 * fraction * 3;
                return point;
            }
        });
        valueAnimator.start();
        valueAnimator.addUpdateListener(animation -> {
            PointF point = (PointF) animation.getAnimatedValue();
            iv.setX(point.x);
            iv.setY(point.y);
//            float cVal = animation.getAnimatedFraction();
//            iv.setAlpha(1 - cVal);
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                iv.setVisibility(View.GONE);
            }
        });
    }
}
