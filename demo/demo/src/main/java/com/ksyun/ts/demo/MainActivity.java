package com.ksyun.ts.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ksyun.ts.demo.kit.KSVSShortVideoKit;
import com.ksyun.ts.shortvideo.kit.IKSVSShortVideoAuth;

/**
 * @Author: [xiaoqiang]
 * @Description: [MainActivity]
 * @CreateDate: [2017/12/22]
 * @UpdateDate: [2017/12/22]
 * @UpdateUser: [xiaoqiang]
 * @UpdateRemark: []
 */

public class MainActivity extends Activity {
    public final static String SDK_AUTH_TOKEN = "ff9737a56f0805482b1dff8fb662c784";
    private static final String TAG = MainActivity.class.getName();
    public final static String VIDEOPATH = "VideoPath";
    private final static int REQUEST_RECORD = 1;
    private final static int REQUEST_EDITOR = 2;
    private Button mAuthBtn;
    private Button mRecordBtn;
    private Button mEditorBtn;
    private Button mPlayerBtn;
    private Button mUploadBtn;
    private TextView mTvLog;
    private StringBuilder mLog;
    private String mRecordUrl;
    private String mEditorUrl;
    private boolean isAuthorized;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuthBtn = findViewById(R.id.btn_auth);
        mRecordBtn = findViewById(R.id.btn_record);
        mPlayerBtn = findViewById(R.id.btn_player);
        mEditorBtn = findViewById(R.id.btn_editor);
        mUploadBtn = findViewById(R.id.btn_upload);
        mAuthBtn.setOnClickListener(mClickListener);
        mRecordBtn.setOnClickListener(mClickListener);
        mPlayerBtn.setOnClickListener(mClickListener);
        mUploadBtn.setOnClickListener(mClickListener);
        mEditorBtn.setOnClickListener(mClickListener);

        mTvLog = findViewById(R.id.tv_log);
        mLog = new StringBuilder();
        mLog.append("* .启动应用程序，等待进行鉴权\n");
        mTvLog.setText(mLog.toString());
        mRecordUrl = null;
        mEditorUrl = null;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_auth) {
                mLog.append("* .点击鉴权按钮，等待鉴权成功\n");
                mTvLog.setText(mLog.toString());
                auth();
            } else if (v.getId() == R.id.btn_record) {
                if (isAuthorized) {
                    mLog.append("* .点击录制按钮，等待录制结果\n");
                    mTvLog.setText(mLog.toString());
                    record();
                } else {
                    mLog.append("* .查询到还没有鉴权，不可以进行录制\n");
                    mTvLog.setText(mLog.toString());
                }
            } else if (v.getId() == R.id.btn_editor) {
                if (isAuthorized) {
                    mLog.append("* .点击编辑按钮，等待编辑结果\n");
                    mTvLog.setText(mLog.toString());
                    editor();
                } else {
                    mLog.append("* .查询到还没有鉴权，不可以进行编辑\n");
                    mTvLog.setText(mLog.toString());
                }
            } else if (v.getId() == R.id.btn_upload) {
                if (isAuthorized) {
                    mLog.append("* .进入上传页面，进行视频上传\n");
                    mTvLog.setText(mLog.toString());
                    upload();
                } else {
                    mLog.append("* .查询到还没有鉴权，不可以上传视频\n");
                    mTvLog.setText(mLog.toString());
                }
            } else if (v.getId() == R.id.btn_player) {
                if (isAuthorized) {
                    mLog.append("* .进入播放界面，选择视频进行播放\n");
                    mTvLog.setText(mLog.toString());
                    player();
                } else {
                    mLog.append("* .查询到还没有鉴权，不可以播放视频\n");
                    mTvLog.setText(mLog.toString());
                }
            }
        }
    };

    /**
     * 启动录制界面
     */
    private void record() {
        RecordActivity.startActivityForResult(MainActivity.this, REQUEST_RECORD);
    }

    /**
     * 启动编辑界面
     */
    private void editor() {
        if (TextUtils.isEmpty(mRecordUrl)) {
            mLog.append("* .没有查询到录制的视频，请先进行视频录制\n");
            mTvLog.setText(mLog.toString());
        } else {
            EditorActivity.startActivityForResult(MainActivity.this, REQUEST_EDITOR, mRecordUrl);
        }
    }

    /**
     * 启动上传界面
     */
    private void upload() {
        if (TextUtils.isEmpty(mEditorUrl)) {
            mLog.append("* .没有查询到编辑后的视频，请先进行视频录制和编辑\n");
            mTvLog.setText(mLog.toString());
        } else {
            UploadActivity.startActivity(MainActivity.this, mEditorUrl);
        }
    }

    private void player() {
        PlayerActivity.startActivity(MainActivity.this);
    }

    /**
     * 开始鉴权
     */
    private void auth() {
        KSVSShortVideoKit.addAuthorizeListener(MainActivity.this, mAuthListener);
        KSVSShortVideoKit.authorize(MainActivity.this, SDK_AUTH_TOKEN, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_RECORD:// 录制界面返回结果，代表录制成功了
                    mRecordUrl = data.getStringExtra(VIDEOPATH);
                    mLog.append("* .录制完成了，录制的视频地址是：\n");
                    mLog.append(mRecordUrl + "\n");
                    mLog.append("* .点击编辑按钮，进行视频编辑\n");
                    mTvLog.setText(mLog.toString());
                    break;
                case REQUEST_EDITOR:
                    mEditorUrl = data.getStringExtra(VIDEOPATH);
                    mLog.append("* .编辑完成了，最终的视频地址是：\n");
                    mLog.append(mEditorUrl + "\n");
                    mLog.append("* .点击上传按钮，完成视频的上传工作\n");
                    mTvLog.setText(mLog.toString());
                    break;
            }
        }
    }

    private IKSVSShortVideoAuth.IKSVSShortVideoAuthListener mAuthListener =
            new IKSVSShortVideoAuth.IKSVSShortVideoAuthListener() {
                @Override
                public void onSuccess() {
                    KSVSShortVideoKit.removeAuthorizeListener(MainActivity.this, mAuthListener);
                    Toast.makeText(MainActivity.this, "鉴权成功", Toast.LENGTH_SHORT).show();
                    mLog.append("* .鉴权成功，可以进行录制、编辑、上传等操作\n");
                    mTvLog.setText(mLog.toString());
                    isAuthorized = true;
                }

                @Override
                public void onFailed(int error, String message) {
                    KSVSShortVideoKit.removeAuthorizeListener(MainActivity.this, mAuthListener);
                    Log.e(TAG, "鉴权失败,错误码：" + error + "，错误原因：" + message);
                    Toast.makeText(MainActivity.this, "鉴权失败,error:" + error,
                            Toast.LENGTH_SHORT).show();
                    mLog.append("* .鉴权失败,错误码：" + error + "，错误原因：" + message + ",请重新鉴权\n");
                    mTvLog.setText(mLog.toString());
                    isAuthorized = false;
                }
            };


}
