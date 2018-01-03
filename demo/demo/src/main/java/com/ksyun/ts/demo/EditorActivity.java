package com.ksyun.ts.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ksyun.media.shortvideo.kit.KSYEditKit;
import com.ksyun.media.shortvideo.utils.ProbeMediaInfoTools;
import com.ksyun.media.shortvideo.utils.ShortVideoConstants;
import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.ts.shortvideo.common.util.KLog;
import com.ksyun.ts.shortvideo.kit.IKSVSShortVideoListener;

import java.io.File;

/**
 * @Author: [xiaoqiang]
 * @Description: [EditorActivity]
 * @CreateDate: [2017/12/22]
 * @UpdateDate: [2017/12/22]
 * @UpdateUser: [xiaoqiang]
 * @UpdateRemark: []
 */

public class EditorActivity extends Activity {
    private final static String VIDEOPATH = "VideoPath";
    private static final String TAG = EditorActivity.class.getName();
    private String mVideoPath;
    private GLSurfaceView mGLSurfaceView;
    private KSYEditKit mEditKit;
    private Button mCompose;
    private TextView mTvLog;
    private Handler mHandler;

    public final static void startActivityForResult(Activity context, int request, String path) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(MainActivity.VIDEOPATH, path);
        context.startActivityForResult(intent, request);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mVideoPath = getIntent().getStringExtra(VIDEOPATH);
        mGLSurfaceView = findViewById(R.id.gl_surface);
        mCompose = findViewById(R.id.btn_editor);
        mTvLog = findViewById(R.id.tv_editor);
        mCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCompose.setVisibility(View.GONE);
                startCompose();
            }
        });
        mHandler = new Handler();
        onInit();
    }

    public void onInit() {
        mEditKit = new KSYEditKit(EditorActivity.this);
        mEditKit.setDisplayPreview(mGLSurfaceView);
        mEditKit.setEditPreviewUrl(mVideoPath);
        mEditKit.setLooping(true);
        mEditKit.startEditPreview();

        mEditKit.setOnErrorListener(new KSYEditKit.OnErrorListener() {
            @Override
            public void onError(int i, long l) {
                Log.e(TAG, "OnError:" + i + "," + l);
            }
        });
        mEditKit.setOnInfoListener(new KSYEditKit.OnInfoListener() {
            @Override
            public Object onInfo(int i, String... strings) {
                Log.e(TAG, "onInfo:" + i + ",");
                if (strings != null) {
                    for (String str : strings) {
                        Log.e(TAG, "onInfo:" + str);
                    }
                }
                switch (i) {
                    case ShortVideoConstants.SHORTVIDEO_COMPOSE_START:
                        mHandler.post(mComposeRunnable);
                        break;
                    case ShortVideoConstants.SHORTVIDEO_COMPOSE_FINISHED:
                        mHandler.removeCallbacks(mComposeRunnable);
                        Intent intent = new Intent();
                        intent.putExtra(MainActivity.VIDEOPATH, strings[0]);
                        setResult(RESULT_OK, intent);
                        Toast.makeText(EditorActivity.this, "视频合成完成，点击上传按钮进行上传", Toast.LENGTH_SHORT).show();
                        finish();
                        return null;
                    case ShortVideoConstants.SHORTVIDEO_COMPOSE_ABORTED:
                        mHandler.removeCallbacks(mComposeRunnable);
                        break;

                }
                return null;
            }
        });
    }

    public void startCompose() {
        mEditKit.setTargetResolution(StreamerConstants.VIDEO_RESOLUTION_480P);
        mEditKit.setVideoFps(StreamerConstants.DEFAULT_TARGET_FPS);
        mEditKit.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
        mEditKit.setVideoCodecId(AVConst.CODEC_ID_AVC);
        mEditKit.setVideoEncodeProfile(VideoEncodeFormat.ENCODE_PROFILE_LOW_POWER);
        mEditKit.setAudioKBitrate(StreamerConstants.DEFAULT_AUDIO_BITRATE);
        mEditKit.setVideoKBitrate(StreamerConstants.DEFAULT_INIT_VIDEO_BITRATE);
        File file = new File(Environment.getExternalStorageDirectory() + "/ksyun/"
                + getPackageName());
        String mVideoDir = file.getAbsolutePath() + "/merge_" + System.currentTimeMillis() + ".mp4";
        try {
            mEditKit.startCompose(mVideoDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable mComposeRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(mComposeRunnable, 1000);
            if (mEditKit != null) {
                int progress = mEditKit.getProgress();
                mTvLog.setText("测试视频编辑功能，Demo中只是进行合成\n" +
                        "合成进度：" + progress);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mEditKit.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mEditKit.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEditKit.stopEditPreview();
        mEditKit.release();
        mEditKit = null;
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }
}
