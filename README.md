# KSVSShortVideoCore_Android
**“短视频” 相关项目说明**

 ![image](https://raw.githubusercontent.com/wiki/ksvc/KSVSShortVideoKit_Android/images/shortvideo_api.png)
 
 * [KSYMediaEditorKit_Android](https://github.com/ksvc/KSYMediaEditorKit_Android) 短视频SDK，只包含短视频相关功能。如果使用这个SDK，需要自己实现APP展示效果、上传、播放等。在使用时，必须通过短视频SDK的鉴权流程
 * [KSVSShortVideoCore_Android](https://github.com/ksvc/KSVSShortVideoCore_Android) 短视频解决方案Core SDK，包含短视频SDK所有接口，并且还增加上传、```智能推荐(暂时未开放)```。使用这个SDK，需要自己实现APP展示效果和播放功能。在使用时，只需要完成解决方案的鉴权流程。注意：不需要在进行短视频SDK提供的鉴权
 * [KSVSShortVideoKit_Android](https://github.com/ksvc/KSVSShortVideoKit_Android) 短视频解决方案，包含短视频SDK一部分功能，支持视频上传、播放、智能推荐，并且实现了一套直接可用的展示效果。如果使用这个SDK，只需要在完成鉴权的前提下开启相应的界面，可以快速的进行集成。
 
 如果您使用过短视频SDK **“KSYMediaEditorKit_Android”**，需要升级为KSVSShortVideoCore_Android，请跳转到[这里](https://github.com/ksvc/KSVSShortVideoCore_Android/wiki/%E7%9F%AD%E8%A7%86%E9%A2%91SDK%E5%8D%87%E7%BA%A7%E4%B8%BA%E7%9F%AD%E8%A7%86%E9%A2%91%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88)
 
## 1 简述
## 2 项目架构
短视频解决方案Core层架构图如下：
![image](https://raw.githubusercontent.com/wiki/ksvc/KSVSShortVideoKit_iOS/svApiStructure.png)

### 2.1 架构流程描述
* APP 集成该方案SDK时，需要先鉴权才能使用短视频解决方案后续功能
* Core版本方案包含上传图片（头像、封面）、视频功能，上传至金山云端
* 录制、编辑、美颜等功能由金山云短视频SDK（KSYMediaEditorKit）提供，KSYMediaEditorKit与解决方案SDK是两个独立的部分，而鉴权操作统一由解决方案SDK处理即可
* 播放功能由KSYMediaPlayer提供
### 2.2 鉴权流程
短视频解决方案有一个更加安全、合理的鉴权方案，保证APP以及用户数据的安全。具体的鉴权流程如下：
   1. 申请SDK Token并且调用SDK提供的鉴权接口
   2. APP 向解决方案提供User Token，可以增加二次鉴权逻辑。(注:User Token 可以不提供)
   3. 解决方案向SDK Server 验证SDK Token是否正确
   4. SDK Server 向APP Server验证User Token 是否正确

   鉴权流程图解如下：

  ![image](https://raw.githubusercontent.com/wiki/ksvc/KSVSShortVideoKit_Android/images/auth.png)
## 3 功能介绍
* **短视频SDK**

* **金山云存储**

* **金山云转码**
## 4 接入流程
 如果您使用过短视频SDK **“KSYMediaEditorKit_Android”**，需要升级为KSVSShortVideoCore_Android，请跳转到[这里](https://github.com/ksvc/KSVSShortVideoCore_Android/wiki/%E7%9F%AD%E8%A7%86%E9%A2%91SDK%E5%8D%87%E7%BA%A7%E4%B8%BA%E7%9F%AD%E8%A7%86%E9%A2%91%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88)
### 4.1 Token 申请流程
 1. 若购买短视频解决方案套餐包，需进入[金山云短视频解决方案官网](https://www.ksyun.com/post/solution/KSVS)，点击“购买套餐包”，确认购买，填写表单信息，授权token会以邮件的形式提供。
   2. 若单独购买短视频SDK，联系金山云销售进行授权申请，或者直接拨打：62927777 转 5120

### 4.2 集成流程
 1. 从github下载AAR文件或者使用Gradle 依赖 
     
     ```
      github 地址：https://github.com/ksvc/KSVSShortVideoCore_Android
      gradle 依赖：compile 'com.ksyun.ts:ShortVideoCore:1.1.1' 
     ```
     
 1. 通过jcenter依赖其他相关项目

     ```
        // 短视频SDK
        compile 'com.ksyun.media:libksysv-java:2.0.0'
        compile 'com.ksyun.media:libksysv-arm64:2.0.0'
        compile 'com.ksyun.media:libksysv-armv7a:2.0.0'
        compile 'com.ksyun.media:libksysv-x86:2.0.0'
        
        compile 'com.android.volley:volley:1.0.0'
        compile 'com.ksyun.ks3:ks3androidsdk:1.4.1'
        compile 'io.reactivex.rxjava2:rxjava:2.0.1'
        compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
        compile 'com.squareup.retrofit2:retrofit:2.3.0'
        compile 'com.squareup.retrofit2:converter-gson:2.3.0'
        compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
        compile 'com.squareup.okhttp3:okhttp:3.9.0'
    ```

 1. 在AndroidManifest文件中，注册所需要的权限

      ```xml
         <uses-permission android:name="android.permission.READ_PHONE_STATE" />
         <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
         <uses-permission android:name="android.permission.INTERNET" />
         <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
         <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
         <uses-permission android:name="android.permission.READ_PHONE_SINTERNETWIFI_STATE" />
         <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
         <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
         <uses-permission android:name="android.permission.CAMERA" />
         <uses-permission android:name="android.permission.RECORD_AUDIO" />
         <uses-permission android:name="android.permission.FLASHLIGHT" />
         <uses-permission android:name="android.permission.READ_LOGS" />
         <uses-permission android:name="android.permission.GET_TASKS" />
         <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
         <uses-permission android:name="android.permission.WAKE_LOCK" />
         <uses-feature android:name="android.hardware.camera" />
         <uses-feature android:name="android.hardware.camera.autofocus" />
      ```
  4. 复制demo工程的KSVSShortVideoKit类到您的项目中。
  
   **注意：KSVSShortVideoKit就是SDK对外提供的接口。在wiki中我会仔细描述每个方法的用途**
   
  5. 具体接口的使用，请在WIKI查看：[wiki](https://github.com/ksvc/KSVSShortVideoCore_Android/wiki)
## 5 反馈与建议
### 5.1 反馈模版
|类型|描述|
|:--:|:--:|
|SDK名称	|KSVSShortVideoKit_Android|
|SDK版本	|v1.0.0|
|设备型号	|oppo r9s|
|OS版本	|Android 6.0.1|
|问题描述	|描述问题出现的现象|
|操作描述	|描述经过如何操作出现上述问题|
|额外附件|文本形式控制台log、crash报告、其他辅助信息（界面截屏或录像等）|
### 5.2 短视频解决方案咨询
金山云官方产品客服，帮您快速了解对接金山云短视频解决方案：

 ![image](https://raw.githubusercontent.com/wiki/ksvc/KSVSShortVideoKit_Android/images/wechat.png)
### 5.3 联系方式
  * 主页：[金山云](http://www.ksyun.com/)
  * 邮箱: zengfanping@kingsoft.com
  * QQ讨论群：
    * 574179720 视频云技术交流群
    * 620036233 视频云Android技术交流
    * 以上两个加一个QQ群即可
  * Issues: https://github.com/ksvc/KSVSShortVideoCore_Android/issues
