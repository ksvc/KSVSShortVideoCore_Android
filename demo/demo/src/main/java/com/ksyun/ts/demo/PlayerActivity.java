package com.ksyun.ts.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ksyun.ts.shortvideo.common.util.KLog;

import java.util.List;

/**
 * @Author: [xiaoqiang]
 * @Description: [PlayerActivity]
 * @CreateDate: [2017/12/22]
 * @UpdateDate: [2017/12/22]
 * @UpdateUser: [xiaoqiang]
 * @UpdateRemark: []
 */

public class PlayerActivity extends Activity {
    private static final String TAG = UploadActivity.class.getName();
    private List<String> mUri;
    private ListView mVideoList;
    private ArrayAdapter mAdapter;

    public final static void startActivity(Context context) {
        Intent intent = new Intent(context, PlayerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mUri = Util.getLocalVideo(PlayerActivity.this);
        mVideoList = findViewById(R.id.list_view);
        mAdapter = new ArrayAdapter<String>
                (this, R.layout.simple_list_item, mUri);

        mVideoList.setAdapter(mAdapter);
        mVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VodPlayerActivity.startActivity(PlayerActivity.this, (String) mAdapter.getItem(position));
            }
        });
    }
}
