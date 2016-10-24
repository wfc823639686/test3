package com.wfc.app.test3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by wangfengchen on 2016/10/24.
 */

public class MoveTestActivity extends Activity implements View.OnClickListener {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_test);
        iv = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        iv.setX(iv.getX());
    }
}
