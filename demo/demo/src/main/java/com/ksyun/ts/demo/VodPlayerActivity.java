package com.ksyun.ts.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.ts.shortvideo.kit.IKSVSShortVideoListener;

import java.io.IOException;

/**
 * @Author: [xiaoqiang]
 * @Description: [VodPlayerActivity]
 * @CreateDate: [2017/12/25]
 * @UpdateDate: [2017/12/25]
 * @UpdateUser: [xiaoqiang]
 * @UpdateRemark: []
 */

public class VodPlayerActivity extends Activity {
    private final static String TAG = VodPlayerActivity.class.getName();

    private SurfaceView mVodSurface;
    private String mVideoPath;
    private KSYMediaPlayer mMediaPlayer;
    private TextView mPlayer;
    private TextView mPlayerStatus;

    public final static void startActivity(Context context, String path) {
        Intent intent = new Intent(context, VodPlayerActivity.class);
        intent.putExtra(MainActivity.VIDEOPATH, path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vod);

        mVodSurface = findViewById(R.id.surface_view);
        mPlayer = findViewById(R.id.tv_palyer);
        mPlayerStatus = findViewById(R.id.tv_player_status);
        mVideoPath = getIntent().getStringExtra(MainActivity.VIDEOPATH);
        Log.e(TAG, "播放地址是：" + mVideoPath);
        mPlayer.setText("视频播放地址是：\n" + mVideoPath);
        mVodSurface.getHolder().addCallback(mSurfaceCallback);
        initPlayer(mVideoPath);
    }

    private void initPlayer(String url) {
        mMediaPlayer = new KSYMediaPlayer.Builder(VodPlayerActivity.this).build();
        mMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                if (mMediaPlayer != null) {
                    mPlayerStatus.setText("视频播放状态：开始循环播放");
                    mMediaPlayer.start();
                }
            }
        });
        mMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                mPlayerStatus.setText("视频播放状态：播放错误，错误码是，" + what);
                return false;
            }
        });
        try {
            mMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setLooping(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
    }

    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (mMediaPlayer != null) {
                mMediaPlayer.setDisplay(holder);
                mMediaPlayer.setScreenOnWhilePlaying(true);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mMediaPlayer != null) {
                mMediaPlayer.setDisplay(null);
            }
        }
    };
}
