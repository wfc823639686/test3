package com.wfc.app.test3;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceActivity;
import com.wfc.app.test3.model.Photo;
import com.wfc.app.test3.presenter.DatingLcePresenter;
import com.wfc.app.test3.utils.DialogUtils;
import com.wfc.app.test3.view.IDatingLceView;

import java.util.ArrayList;
import java.util.List;

public class DatingLceActivity extends MvpLceActivity<View, Photo, IDatingLceView<Photo>, DatingLcePresenter> implements IDatingLceView<Photo> {

    private static final String TAG = "DatingLceActivity";

    SlidingSelectView slidingSelectView;

    List<Photo> ds1 = new ArrayList<>();

    List<Photo> ds2 = new ArrayList<>();

    int[] ds = {R.mipmap.d1, R.mipmap.d2, R.mipmap.d3, R.mipmap.d4};

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating);
        slidingSelectView = (SlidingSelectView) findViewById(R.id.ssview);
        progressDialog = DialogUtils.createProgressDialog(this, "加载中...");
        for (int i=0;i<4;i++) {
            Photo p = new Photo();
            p.setPhotoUrl(String.valueOf(ds[i]));
            p.setTitle("NO."+i);
            if(i<2) {
                ds1.add(p);
            } else {
                ds2.add(p);
            }
        }
        slidingSelectView.setPhotos(ds1);
        slidingSelectView.setCallback(()-> {
            Log.i(TAG, "onOver: ");
            slidingSelectView.setPhotos(ds2);
        });
    }

    @NonNull
    @Override
    public DatingLcePresenter createPresenter() {
        return new DatingLcePresenter();
    }


    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }


    @Override
    public void setData(Photo data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }
}
