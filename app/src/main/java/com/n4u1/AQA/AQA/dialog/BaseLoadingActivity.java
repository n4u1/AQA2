package com.n4u1.AQA.AQA.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

public class BaseLoadingActivity extends Activity {
    ProgressDialog pbLoading = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


//        다이얼로그가 생성되어있지 않으면 생성
        if (pbLoading == null) createLoading();

    }


// 다이얼로그 생성메서드


    public void createLoading() {
        pbLoading = new ProgressDialog(this);
        pbLoading.setMessage("Loading...");
        pbLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pbLoading.setCancelable(true);

    }


// 다이얼로그 보여주기
    public void startLoading() {
        if (pbLoading == null) return;
        if (!pbLoading.isShowing()) pbLoading.show();
    }


// 다이얼로그 숨기기
    public void endLoading() {

        if (pbLoading == null) return;

        pbLoading.dismiss();

    }


// 액티비티가 파기되면 null 처리

    @Override

    protected void onDestroy() {

        pbLoading = null;


        super.onDestroy();


    }
}
