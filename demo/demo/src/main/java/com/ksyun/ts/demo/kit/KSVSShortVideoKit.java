package com.ksyun.ts.demo.kit;

import android.content.Context;

import com.ksyun.ts.shortvideo.KSVSShortVideoManager;
import com.ksyun.ts.shortvideo.kit.IKSVSShortVideoAuth;
import com.ksyun.ts.shortvideo.kit.IKSVSShortVideoListener;
import com.ksyun.ts.shortvideo.kit.IKSVSShortVideoUpload;

/**
 * @Author: [xiaoqiang]
 * @Description: [对于API层的简单封装，目前仅支持鉴权和上传文件。移除UI界面的约束]
 * @CreateDate: [2017/12/22]
 * @UpdateDate: [2018/1/3]
 * @UpdateUser: [xiaoqiang]
 * @UpdateRemark: [增加接口注释]
 */

public class KSVSShortVideoKit {
    private static IKSVSShortVideoUpload mUpload;

    /**
     * 上传文件，已支持PNG图片和mp4视频
     * <p>
     * 目前仅支持单个文件上传，请不要
     *
     * @param uid       上传用户ID，必须是一个唯一的用户标示
     * @param path      上传的视频文件或者图片，注意格式是png和mp4。还有必须保证文件存在本地
     * @param mListener 监听器，监听上传的状态、进度、错误码之类
     */
    public static void uploadFile(Context mContext, String uid, String path,
                                  IKSVSShortVideoListener mListener) {
        if (mUpload == null) {
            mUpload = new KSVSShortVideoManager(mContext).createVideoUpload();
        }
        mUpload.setIKSVSShortVideoListener(mListener);
        mUpload.uploadOtherFile(uid, path);
    }

    /**
     * 在上传完成后，如果不需要在使用上传功能，建议销毁上传对象。否则可能会出现内存泄漏或者上传失败等情况
     */
    public static void releaseUpload() {
        if (mUpload != null) {
            mUpload.release();
        }
        mUpload = null;
    }

    /**
     * 调用解决方案鉴权。只有在鉴权完成后才能用后续的工作：录制、编辑、上传、播放等等
     * 如果鉴权失败，调用其他接口可能会出现未知问题
     *
     * 注意：如果您使用过短视频SDK，需要替换为短视频解决方案，那么您需要使用这个鉴权，在鉴权通过后直接使用短视频SDK
     * 中的其他功能就型。千万注意：不需要在单独调用短视频SDK的鉴权代码了
     *
     * @param mContext  上下文
     * @param sdkToken  到官网申请的token。申请步骤请自行到wiki查看
     * @param userToken 一种双向验证的策略，提供UserToken后，SDK 服务器会和您的服务器进行校验
     */
    public static void authorize(Context mContext, String sdkToken, String userToken) {
        KSVSShortVideoManager.getInstance(mContext).authorize(sdkToken, userToken);
    }

    /**
     * 判断是否鉴权成功。
     * <p>
     * 为了防止频繁的鉴权，SDK会在本地加密记录鉴权信息。所以有️时没有调用authorize接口也会发现鉴权成功了，这属于正常
     * 现象。不过我还是建议用户在合适的机会正常调用authorize
     *
     * @param mContext 上下文
     * @return 返回鉴权结果，true标示鉴权成功
     */
    public static boolean isAuthorized(Context mContext) {
        return KSVSShortVideoManager.getInstance(mContext).isAuthorized();
    }

    /**
     * 添加一个鉴权的监听器。建议和Demo一样，在application中添加一个永久的监听器。在调用鉴权的地方添加一个自己临时的
     * 监听器
     *
     * @param context  上下文
     * @param listener 监听鉴权状态
     */
    public static void addAuthorizeListener(Context context,
                                            IKSVSShortVideoAuth.IKSVSShortVideoAuthListener listener) {
        KSVSShortVideoManager.getInstance(context).addKSVSShortVideoAuthListener(listener);
    }

    /**
     * 移除鉴权监听器，建议移除临时的鉴权监听
     *
     * @param context  上下文
     * @param listener 需要移除的监听器
     */
    public static void removeAuthorizeListener(Context context,
                                               IKSVSShortVideoAuth.IKSVSShortVideoAuthListener listener) {
        KSVSShortVideoManager.getInstance(context).removeKSVSShortVideoAuthListener(listener);
    }

    /**
     * 移除所有的监听器
     *
     * @param mContext 上下文
     */
    public static void releaseAuthorize(Context mContext) {
        KSVSShortVideoManager.getInstance(mContext).release();
    }
}
