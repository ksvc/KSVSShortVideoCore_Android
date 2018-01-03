package com.ksyun.ts.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ksyun.ts.demo.kit.KSVSShortVideoKit;
import com.ksyun.ts.shortvideo.common.util.KLog;
import com.ksyun.ts.shortvideo.kit.IKSVSShortVideoListener;
import com.ksyun.ts.shortvideo.kit.IKSVSShortVideoUpload;

import java.util.List;

/**
 * @Author: [xiaoqiang]
 * @Description: [UploadActivity]
 * @CreateDate: [2017/12/22]
 * @UpdateDate: [2017/12/22]
 * @UpdateUser: [xiaoqiang]
 * @UpdateRemark: []
 */

public class UploadActivity extends Activity {
    private static final String TAG = UploadActivity.class.getName();

    private TextView mVideoPath;
    private String mVideoLocal;
    private Button mUpload;
    private TextView mUploadProgress;
    private TextView mUploadCom;
    private Button mPlayer;


    public final static void startActivity(Context context, String path) {
        Intent intent = new Intent(context, UploadActivity.class);
        intent.putExtra(MainActivity.VIDEOPATH, path);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        mVideoPath = findViewById(R.id.tv_path);
        mVideoLocal = getIntent().getStringExtra(MainActivity.VIDEOPATH);

        mVideoPath.setText(mVideoLocal);
        mUpload = findViewById(R.id.btn_upload);
        mUploadProgress = findViewById(R.id.tv_upload_p);
        mUploadCom = findViewById(R.id.tv_upload_com);
        mPlayer = findViewById(R.id.btn_player);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUpload.setVisibility(View.GONE);
                KSVSShortVideoKit.uploadFile(UploadActivity.this, Build.SERIAL, mVideoLocal, mUploadVideoListener);
            }
        });
        mPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerActivity.startActivity(UploadActivity.this);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KSVSShortVideoKit.releaseUpload();
    }

    private IKSVSShortVideoListener mUploadVideoListener = new IKSVSShortVideoListener() {
        @Override
        public void onInfo(String type, Bundle data) {
            if (type.equals(IKSVSShortVideoListener.KSVS_LISTENER_TYPE_UPLOAD)) {
                int status = data.getInt(IKSVSShortVideoListener.KSVS_LISTENER_BUNDLE_STATUS_INT);
                if (status == IKSVSShortVideoUpload.UPLOAD_INFO_COMPLETE) {
                    String path = data.getString(IKSVSShortVideoUpload.UPLOAD_INFO_FILE_PATH);
                    KLog.d(TAG, "获取到的path是：" + path);
                    mUploadProgress.setText("视频地址:" + path);
                    mPlayer.setVisibility(View.VISIBLE);


                    StringBuilder str = new StringBuilder();
                    List<String> urls = Util.saveLocal(UploadActivity.this, path);
                    str.append("保存到本地的所有已上传视频有：\n");
                    for (int i = 0; i < urls.size(); i++) {
                        str.append(i + ". " + urls.get(i) + "\n");
                    }
                    mUploadCom.setText(str.toString());
                }
            }
        }

        @Override
        public void onError(String type, int error, Bundle data) {
            if (type.equals(IKSVSShortVideoListener.KSVS_LISTENER_TYPE_UPLOAD)) {
                KLog.e(TAG, "视频上传失败，error=" + error);
            }
        }

        @Override
        public void onProgress(String type, int params, int progress) {
            if (type.equals(IKSVSShortVideoListener.KSVS_LISTENER_TYPE_UPLOAD)) {
                if (params == IKSVSShortVideoUpload.UPLOAD_PROGRESS_FILE) {
                    Log.e(TAG, "文件上传进度是:" + progress);
                    mUploadProgress.setText("文件上传进度是:" + progress);
                }
            }
        }
    };

}
