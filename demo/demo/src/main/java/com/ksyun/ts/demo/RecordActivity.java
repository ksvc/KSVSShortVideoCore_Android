package com.ksyun.ts.demo;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ksyun.media.shortvideo.kit.KSYRecordKit;
import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.ts.shortvideo.common.util.KLog;

import java.io.File;

/**
 * @Author: [xiaoqiang]
 * @Description: [RecordActivity]
 * @CreateDate: [2017/12/22]
 * @UpdateDate: [2017/12/22]
 * @UpdateUser: [xiaoqiang]
 * @UpdateRemark: []
 */

public class RecordActivity extends Activity {
    private final static String TAG = RecordActivity.class.getName();
    private KSYRecordKit mKSYRecordKit;
    private GLSurfaceView mCameraPreviewView;
    private Button mStartButton;
    private TextView mRecordTime;
    private Handler mHandler;
    private int mTime = 5;

    public final static void startActivityForResult(Activity context, int request) {
        Intent intent = new Intent(context, RecordActivity.class);
        context.startActivityForResult(intent, request);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        mCameraPreviewView = findViewById(R.id.gl_surface);
        mStartButton = findViewById(R.id.btn_record);
        mRecordTime = findViewById(R.id.tv_record);
        mHandler = new Handler();
        init();
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartButton.setVisibility(View.GONE);
                File file = new File(Environment.getExternalStorageDirectory() + "/ksyun/"
                        + getPackageName());
                if (!file.exists()) {
                    file.mkdirs();
                }
                mTime = 5;
                boolean status = mKSYRecordKit.startRecord(file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4");
                if (status) {
                    mHandler.postDelayed(mRunnable, 1000);
                }
            }
        });
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mTime--;
            mRecordTime.setText("测试录制功能，Demo中只是录制5秒视频\n" +
                    "录制倒计时：" + mTime);
            if (mTime <= 0) {
                File file = new File(Environment.getExternalStorageDirectory() + "/ksyun/"
                        + getPackageName());
                if (!file.exists()) {
                    file.mkdirs();
                }
                String outFile = file.getAbsolutePath() + "/" +
                        "merger_" + System.currentTimeMillis() + ".mp4";
                mKSYRecordKit.stopRecord(outFile, mMergeListener);
            } else {
                mHandler.postDelayed(mRunnable, 1000);
            }
        }
    };

    private void init() {
        mKSYRecordKit = new KSYRecordKit(RecordActivity.this);
        mKSYRecordKit.setPreviewFps(StreamerConstants.DEFAULT_TARGET_FPS);
        mKSYRecordKit.setTargetFps(StreamerConstants.DEFAULT_TARGET_FPS);
        mKSYRecordKit.setVideoKBitrate(StreamerConstants.DEFAULT_INIT_VIDEO_BITRATE);
        mKSYRecordKit.setAudioKBitrate(StreamerConstants.DEFAULT_AUDIO_BITRATE);
        mKSYRecordKit.setPreviewResolution(StreamerConstants.VIDEO_RESOLUTION_480P);
        mKSYRecordKit.setTargetResolution(StreamerConstants.VIDEO_RESOLUTION_480P);
        mKSYRecordKit.setVideoCodecId(AVConst.CODEC_ID_AVC);
        mKSYRecordKit.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
        mKSYRecordKit.setVideoEncodeProfile(VideoEncodeFormat.ENCODE_PROFILE_LOW_POWER);
        mKSYRecordKit.setRotateDegrees(0);
        mKSYRecordKit.setEnableRepeatLastFrame(false);
        mKSYRecordKit.setCameraFacing(CameraCapture.FACING_FRONT);
        mKSYRecordKit.setFrontCameraMirror(true);
        mKSYRecordKit.setOnInfoListener(new KSYStreamer.OnInfoListener() {
            @Override
            public void onInfo(int i, int i1, int i2) {
                KLog.e(TAG, "ONINFO:" + i + "," + i1 + "," + i2);
            }
        });
        mKSYRecordKit.setOnErrorListener(new KSYStreamer.OnErrorListener() {
            @Override
            public void onError(int i, int i1, int i2) {
                KLog.e(TAG, "onError:" + i + "," + i1 + "," + i2);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mKSYRecordKit.setDisplayPreview(mCameraPreviewView);
        mKSYRecordKit.onResume();
        mKSYRecordKit.startCameraPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        mKSYRecordKit.onPause();
        if (!mKSYRecordKit.isRecording() && !mKSYRecordKit.isFileRecording()) {
            mKSYRecordKit.stopCameraPreview();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mKSYRecordKit != null) {
            mKSYRecordKit.setOnInfoListener(null);
            mKSYRecordKit.setOnErrorListener(null);
            mCameraPreviewView = null;
            mKSYRecordKit.setDisplayPreview(mCameraPreviewView);
            mKSYRecordKit.setOnLogEventListener(null);
            mKSYRecordKit.release();
            mKSYRecordKit = null;
        }
    }

    private KSYRecordKit.MergeFilesFinishedListener mMergeListener =
            new KSYRecordKit.MergeFilesFinishedListener() {
                @Override
                public void onFinished(String s) {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.VIDEOPATH, s);
                    setResult(Activity.RESULT_OK, intent);
                    Toast.makeText(RecordActivity.this, "视频录制完成，点击编辑按钮进行视频编辑", Toast.LENGTH_SHORT).show();
                    finish();
                }
            };

}
