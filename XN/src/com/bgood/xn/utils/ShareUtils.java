//package com.bgood.xn.utils;
//
//
//import android.app.Activity;
//
//import com.bgood.xn.R;
//
//public class ShareUtils {
//
//    private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//
//    private Activity mActivity;
//    public ShareUtils(Activity activity){
//    	this.mActivity = activity;
//    	  // 配置需要分享的相关平台
//        configPlatforms();
//        // 设置分享的内容
//        setShareContent();
//    }
//    
//    public void doShare(){
//    	mController.getConfig().setPlatforms(
//    			SHARE_MEDIA.WEIXIN,
//    			SHARE_MEDIA.WEIXIN_CIRCLE,
//    			SHARE_MEDIA.QQ,
//    			SHARE_MEDIA.QZONE,
//    			SHARE_MEDIA.SINA,
//    			SHARE_MEDIA.TENCENT,
//    			SHARE_MEDIA.SMS,
//    			SHARE_MEDIA.EMAIL
//    			);
//    	mController.openShare(mActivity, false);
//    }
//    
//   
//
//    /**
//     * 配置分享平台参数</br>
//     */
//    private void configPlatforms() {
//        // 添加新浪SSO授权
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        // 添加腾讯微博SSO授权
//        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
//
//        // 添加QQ、QZone平台
//        addQQQZonePlatform();
//
//        // 添加微信、微信朋友圈平台
//        addWXPlatform();
//    }
//
//
//    /**
//     * 根据不同的平台设置不同的分享内容</br>
//     */
//    private void setShareContent() {
//
//        // 配置SSO
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
//
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity,"100424468", "c7394704798a158208a74ab60104f0ba");
//        qZoneSsoHandler.addToSocialSDK();
//        mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能");
//
//        UMImage localImage = new UMImage(mActivity, R.drawable.device);
//        UMImage urlImage = new UMImage(mActivity,"http://im.kdweibo.com/xuntong/images/app/XT-10000.png");
//
//        WeiXinShareContent weixinContent = new WeiXinShareContent();
//        weixinContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，微信");
//        weixinContent.setTitle("友盟社会化分享组件-微信");
//        weixinContent.setTargetUrl("http://www.umeng.com");
//        weixinContent.setShareMedia(urlImage);
//        mController.setShareMedia(weixinContent);
//
//        // 设置朋友圈分享的内容
//        CircleShareContent circleMedia = new CircleShareContent();
//        circleMedia.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，朋友圈");
//        circleMedia.setTitle("友盟社会化分享组件-朋友圈");
//        circleMedia.setShareImage(urlImage);
//        circleMedia.setTargetUrl("http://www.umeng.com");
//        mController.setShareMedia(circleMedia);
//
//        UMImage qzoneImage = new UMImage(mActivity,"http://www.umeng.com/images/pic/social/integrated_3.png");
//        qzoneImage.setTargetUrl("http://www.umeng.com/images/pic/social/integrated_3.png");
//
//        // 设置QQ空间分享内容
//        QZoneShareContent qzone = new QZoneShareContent();
//        qzone.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QZone");
//        qzone.setTargetUrl("http://www.umeng.com/social");
//        qzone.setTitle("QZone title");
//        qzone.setShareImage(urlImage);
//        mController.setShareMedia(qzone);
//
//
//        QQShareContent qqShareContent = new QQShareContent();
//        qqShareContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能 -- QQ");
//        qqShareContent.setTitle("hello, title");
//        qqShareContent.setShareImage(urlImage);
//        qqShareContent.setTargetUrl("http://www.umeng.com/social");
//        mController.setShareMedia(qqShareContent);
//
//
//        TencentWbShareContent tencent = new TencentWbShareContent();
//        tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，腾讯微博");
//        // 设置tencent分享内容
//        mController.setShareMedia(tencent);
//
//        // 设置邮件分享内容， 如果需要分享图片则只支持本地图片
//        MailShareContent mail = new MailShareContent(localImage);
//        mail.setTitle("share form umeng social sdk");
//        mail.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，email");
//        // 设置tencent分享内容
//        mController.setShareMedia(mail);
//
//        // 设置短信分享内容
//        SmsShareContent sms = new SmsShareContent();
//        sms.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，短信");
//        sms.setShareImage(urlImage);
//        mController.setShareMedia(sms);
//
//        SinaShareContent sinaContent = new SinaShareContent(urlImage);
//        sinaContent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，新浪微博");
//        mController.setShareMedia(sinaContent);
//
//    }
//    
//    /**QQ平台支持*/
//    private void addQQQZonePlatform() {
//        String appId = "100424468";
//        String appKey = "c7394704798a158208a74ab60104f0ba";
//        // 添加QQ支持, 并且设置QQ分享内容的target url
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity,
//                appId, appKey);
//        qqSsoHandler.setTargetUrl("http://www.umeng.com");
//        qqSsoHandler.addToSocialSDK();
//
//        // 添加QZone平台
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity, appId, appKey);
//        qZoneSsoHandler.addToSocialSDK();
//    }
//    
//    
//    /**
//     * @功能描述 : 添加微信平台分享
//     * @return
//     */
//    private void addWXPlatform() {
//        // 注意：在微信授权的时候，必须传递appSecret
//        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//        String appId = "wx967daebe835fbeac";
//        String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(mActivity, appId, appSecret);
//        wxHandler.addToSocialSDK();
//
//        // 支持微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, appId, appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//    }
//}
