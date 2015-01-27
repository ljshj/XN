package com.bgood.xn.utils;


import android.app.Activity;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


/***
 * 
 * @todo:分享帮助类
 * @date:2014-11-20 上午11:13:31
 * @author:hg_liuzl@163.com
 */
public class ShareUtils {
	
	
	
//	public static final String TENCENTWB_APP_KEY = "801551617";
//	public static final String TENCENTWB_APP_SECRET = "5be3f0bb7f149080826b040ec1f23e9c";

	public static final String SINAWB_APP_KEY = "2584417705";
	public static final String SIANWB_APP_SECRET = "0ee4dc674882d8d3a53596dddba7a15b";
	
	public static final String WX_APP_ID = "wx8fd7339391e0d06a";
	public static final String WX_APP_SECRET = "a6cfc6406d9c31305c882f77b8aead65";
	
	public static final String QQ_APP_ID = "1103507475";
	public static final String QQ_APP_KEY = "uQrjhyh93ifnPE0W";

	public static final String LOGO_URL = "http://114.215.189.189/files/logo.png";
	
	public static final String TARGET_URL = "http://www.showneng.com/app-download.html";
	
    public final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    private Activity mActivity;
    public ShareUtils(Activity activity){
    	this.mActivity = activity;
    	  // 配置需要分享的相关平台
        configPlatforms();
        // 设置分享的内容
      //  setShareContent();
    }
    
    private void doShare(){
    	mController.getConfig().setPlatforms(
    			SHARE_MEDIA.WEIXIN,
    			SHARE_MEDIA.WEIXIN_CIRCLE,
    			SHARE_MEDIA.QQ,
    			SHARE_MEDIA.QZONE,
    			SHARE_MEDIA.SINA,
    			SHARE_MEDIA.TENCENT
    			);
    	mController.openShare(mActivity, false);
    }
    
   

    /**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        // 添加新浪SSO授权
      //  mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加腾讯微博SSO授权
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        // 添加QQ、QZone平台
        addQQQZonePlatform();
        // 添加微信、微信朋友圈平台
        addWXPlatform();
    }


    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    public void setShareContent(String content,String imgUrl) {

        // 配置SSO
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity,QQ_APP_ID, QQ_APP_KEY);
        qZoneSsoHandler.addToSocialSDK();
        mController.setShareContent(content);

       // UMImage localImage = new UMImage(mActivity, R.drawable.icon_app);
        UMImage urlImage = new UMImage(mActivity,TextUtils.isEmpty(imgUrl) ? LOGO_URL:imgUrl);

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle("炫能，炫了更有可能!");
        weixinContent.setTargetUrl(TARGET_URL);
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle("炫能，炫了更有可能!");
        circleMedia.setShareImage(urlImage);
        circleMedia.setTargetUrl(TARGET_URL);
        mController.setShareMedia(circleMedia);

        UMImage qzoneImage = new UMImage(mActivity,TextUtils.isEmpty(imgUrl) ? LOGO_URL:imgUrl);
        qzoneImage.setTargetUrl(LOGO_URL);

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setTargetUrl(TARGET_URL);
        qzone.setTitle("炫能，炫了更有可能!");
        qzone.setShareImage(urlImage);
        mController.setShareMedia(qzone);


        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setTitle("炫能，炫了更有可能!");
        qqShareContent.setShareImage(urlImage);
        qqShareContent.setTargetUrl(TARGET_URL);
        mController.setShareMedia(qqShareContent);


        TencentWbShareContent tencent = new TencentWbShareContent(urlImage);
        tencent.setShareContent(content);
        // 设置tencent分享内容
        mController.setShareMedia(tencent);

        SinaShareContent sinaContent = new SinaShareContent(urlImage);
        sinaContent.setShareContent(content);
        mController.setShareMedia(sinaContent);

        doShare();
    }
    
    /**QQ平台支持*/
    private void addQQQZonePlatform() {
//        String appId = "100424468";
//        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity,QQ_APP_ID, QQ_APP_KEY);
        qqSsoHandler.setTargetUrl(TARGET_URL);
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity, QQ_APP_ID, QQ_APP_KEY);
        qZoneSsoHandler.addToSocialSDK();
    }
    
    
    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//        String appId = "wx967daebe835fbeac";
//        String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mActivity, WX_APP_ID, WX_APP_SECRET);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, WX_APP_ID, WX_APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }
}
