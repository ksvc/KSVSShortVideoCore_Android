package com.ksyun.ts.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: [xiaoqiang]
 * @Description: [Util]
 * @CreateDate: [2017/12/25]
 * @UpdateDate: [2017/12/25]
 * @UpdateUser: [xiaoqiang]
 * @UpdateRemark: []
 */

public class Util {
    public final static List<String> saveLocal(Context context, String path) {
        SharedPreferences preferences = PreferenceManager.
                getDefaultSharedPreferences(context.getApplicationContext());
        String json = preferences.getString(MainActivity.VIDEOPATH, "");
        List<String> urls = new Gson().fromJson(json, new TypeToken<List<String>>() {
        }.getType());

        if (urls == null) {
            urls = new ArrayList<>();
        }
        urls.add(0, path);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(MainActivity.VIDEOPATH, new Gson().toJson(urls));
        editor.apply();
        return urls;
    }

    public final static List<String> getLocalVideo(Context context) {
        SharedPreferences preferences = PreferenceManager.
                getDefaultSharedPreferences(context.getApplicationContext());
        String json = preferences.getString(MainActivity.VIDEOPATH, "");
        List<String> urls = new Gson().fromJson(json, new TypeToken<List<String>>() {
        }.getType());

        if (urls == null) {
            urls = new ArrayList<>();
        }
        return urls;
    }
}
