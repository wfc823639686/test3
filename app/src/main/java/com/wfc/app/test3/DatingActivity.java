package com.wfc.app.test3;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

public class DatingActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating);
//        ViewGroup photoLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_photo, null);
//        SlidingSelectView ssView = (SlidingSelectView) findViewById(R.id.ssview);
//        ssView.addView(photoLayout);
//        ssView.nextPhoto();
    }
}
