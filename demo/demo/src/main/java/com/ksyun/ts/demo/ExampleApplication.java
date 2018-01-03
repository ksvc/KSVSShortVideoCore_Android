package com.ksyun.ts.demo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.ksyun.ts.demo.kit.KSVSShortVideoKit;
import com.ksyun.ts.shortvideo.kit.IKSVSShortVideoAuth;

/**
 * Created by xiaoqiang on 2017/12/5.
 */

public class ExampleApplication extends Application {
    private static final String TAG = ExampleApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        KSVSShortVideoKit.addAuthorizeListener(ExampleApplication.this, mAuthListener);
    }

    private IKSVSShortVideoAuth.IKSVSShortVideoAuthListener mAuthListener =
            new IKSVSShortVideoAuth.IKSVSShortVideoAuthListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailed(int error, String message) {
                    Log.e(TAG, "鉴权失败,错误码：" + error + "，错误原因：" + message);
                    Toast.makeText(ExampleApplication.this, "鉴权失败,error:" + error,
                            Toast.LENGTH_SHORT).show();
                }
            };


}
