package com.bgood.xn.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bgood.xn.R;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWebPage;
import com.umeng.socialize.sso.SmsHandler;

public class SharedHelp implements IWXAPIEventHandler{
	
	public static final String STitle = "showmsg_title";
	public static final String SMessage = "showmsg_message";
	public static final String BAThumbData = "showmsg_thumb_data";
	
	/**微信对应的appid**/
	public static final String WX_APP_ID ="wx15a1087ed6bf4bb3";
	
	
	/**qq对应的Appid*/
	public static final String QQ_APP_ID ="1101093724";
	
	/**qq对应的Appid*/
	public static final String QQ_APP_KEY ="iZ5HCy7dO0M88p8l";
	
	// 首先在您的Activity中添加如下成员变
	public   UMSocialService sController = UMServiceFactory.getUMSocialService("com.umeng.share");

	private  Tencent mTencent;
	private  QQShare mQQShare;
	private Activity mActivity;
	private static IWXAPI api;
	
	public SharedHelp(final Context context){
		// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
		// 其中APP_ID是分配给第三方应用的appid，类型为String。
		mTencent = Tencent.createInstance(QQ_APP_ID, context);
		mQQShare =  new QQShare(context, mTencent.getQQToken());
		 // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(context, WX_APP_ID, true);
        api.registerApp(WX_APP_ID);	//注册微信分享
	}
	
	public void setActivity(final Activity activity){
		this.mActivity = activity;
		api.handleIntent(activity.getIntent(), this);
	}
	
	
	/*************************************微信分享部分*********************************************/
	/**
	 * 微信分享文本
	 * @param isCirle 是否分享到朋友圈
	 * @param Text
	 */
	public void shareText2WX(boolean isCirle,String text) {
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		msg.description = text;
		shareRequest(isCirle, msg);
	}
	
	/**
	 * 微信分享图片
	 * @param isCirle 是否分享到朋友圈
	 * @param imageData
	 */
	public void shareImg2WX(boolean isCirle,byte[] imageData){
		WXImageObject imgObj = new WXImageObject();
		imgObj.imageData = imageData;
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;
		msg.thumbData = imageData;
		shareRequest(isCirle, msg);
	}
	
	/**
	 * 微信分享网页
	 * @param isCircle 是否分享到朋友圈
	 * @param title
	 * @param description
	 * @param thumbData
	 * @param webpageUrl
	 */
	public void shareRichText2WX(boolean isCircle,String title,String description,byte[] thumbData,String webpageUrl){
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = webpageUrl;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = description;
		msg.thumbData = thumbData;
		shareRequest(isCircle, msg);
	}
	
	private void shareRequest(boolean isCircle,WXMediaMessage msg){
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("share"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		req.scene = isCircle?SendMessageToWX.Req.WXSceneTimeline:SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}
	
	public static  String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			//goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		
		Toast.makeText(mActivity, result, Toast.LENGTH_LONG).show();
	}
	
//	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
//		Intent intent = null;
//		WXMediaMessage wxMsg = showReq.message;		
//		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
//		StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
//		msg.append("description: ");
//		msg.append(wxMsg.description);
//		msg.append("\n");
//		msg.append("extInfo: ");
//		msg.append(obj.extInfo);
//		msg.append("\n");
//		msg.append("filePath: ");
//		msg.append(obj.filePath);
//		
//		if(mActivity instanceof InvitesLinkActivity){
//			intent = new Intent(mActivity, InvitesLinkActivity.class);
//		}else if(mActivity instanceof InvitesQrcodeActivity){
//			intent = new Intent(mActivity, InvitesQrcodeActivity.class);
//		}else if(mActivity instanceof InvitesColleaguesActivity){
//			intent = new Intent(mActivity, InvitesColleaguesActivity.class);
//		}
//		intent.putExtra(STitle, wxMsg.title);
//		intent.putExtra(SMessage, msg.toString());
//		intent.putExtra(BAThumbData, wxMsg.thumbData);
//		mActivity.startActivity(intent);
//		mActivity.finish();
//	}
	
	
	
	//#########################################下面是QQ分享的部分###########################################
	/**
	 * @todo:给QQ分享纯图片
	 * @date:2014年7月31日 上午11:05:31
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
    public void shareImg2QQ(boolean share2Qzone,String imgUrl) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,imgUrl);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, share2Qzone?QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN:QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        doShareToQQ(params);
    }
	 /**
	  * @todo:把文本分享到QQ中去
	  * @date:2014年8月4日 下午4:33:28
	  * @author:hg_liuzl@163.com
	  * @params:@param context
	  * @params:@param url
	  */
	 public void shareText2QQ(final boolean share2Qzone,final String context,final String url) { 
	     final Bundle params = new Bundle();
	     params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
	     params.putString(QQShare.SHARE_TO_QQ_TITLE, "标题");
	     params.putString(QQShare.SHARE_TO_QQ_SUMMARY, context);
	     params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
	     params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://im.kdweibo.com/xuntong/images/app/XT-10000.png");
	     params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, share2Qzone?QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN:QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
	     doShareToQQ(params);
	 }
	 
	 /**
	  * @todo:把文本分享到QQ中去
	  * @date:2014年8月4日 下午4:33:28
	  * @author:hg_liuzl@163.com
	  * @params:@param context
	  * @params:@param url
	  */
	 public void shareRichText2QQ(final boolean share2Qzone,String title,String description,String webpageUrl) { 
	     final Bundle params = new Bundle();
	     params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
	     params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
	     params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
	     params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webpageUrl);
	     params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://im.kdweibo.com/xuntong/images/app/XT-10000.png");
	     params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, share2Qzone?QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN:QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
	     doShareToQQ(params);
	 }
		
	/**
	 * 
	 * @todo:异步方式分享到QQ
	 * @date:2014年8月4日 下午4:37:59
	 * @author:hg_liuzl@163.com
	 * @params:@param params
	 * @params:@param mQQShare
	 * @params:@param activity
	 */
	 private void doShareToQQ(final Bundle params) {
	     new Thread(new Runnable() {
	         @Override
	         public void run() {
	             // TODO Auto-generated method stub
	             mQQShare.shareToQQ(mActivity, params, new IUiListener() {
	
	                 @Override
	                 public void onCancel() {
	                 }
	                 @Override
	                 public void onComplete(Object response) {
	                 }
	
	                 @Override
	                 public void onError(UiError e) {
	                 }
	
	             });
	         }
	     }).start();
	 }
	 
	 /**
	 * @todo:退出分享，回收
	 * @date:2014年8月4日 下午4:55:03
	 * @author:hg_liuzl@163.com
	 * @params:
	 */
	public void releaseShare() {
    	mTencent = null;
     if (mQQShare != null) {
         mQQShare.releaseResource();
         mQQShare = null;
       }
   }
	
	
	//################################分享到新浪与短信部分########################################
		private  void postShare(final Activity context,final SHARE_MEDIA medias) {
			sController.getConfig().closeToast();
			sController.postShare(context, medias, new SnsPostListener() {
				@Override
				public void onStart() {
				}
				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode,	SocializeEntity entity) {
				}
				
			});
		}
		/**
		 * 
		 * @todo:分享文本到新浪
		 * @date:2014年7月21日 上午10:28:15
		 * @author:hg_liuzl@163.com
		 */
		public void shareText2Sina(final String shareContent,final SHARE_MEDIA medias){
			sController.getConfig().setDefaultShareLocation(false);
			sController.setShareContent(shareContent);
			postShare(mActivity, medias);
		}
		
		/**
		 * 
		 * @todo:分享图片到新浪
		 * @date:2014年7月21日 上午10:28:15
		 * @author:hg_liuzl@163.com
		 */
		public void shareImg2Sina(final byte[] ImageData,final SHARE_MEDIA medias){
			sController.getConfig().setDefaultShareLocation(false);
			sController.setShareMedia(new UMImage(mActivity, ImageData));
			postShare(mActivity, medias);
		}
		
		/**
		 * 
		 * @todo:分享链接到新浪
		 * @date:2014年7月21日 上午10:28:15
		 * @author:hg_liuzl@163.com
		 */
		public void shareRichText2Sina(String title,String description,byte[] thumbData,String webpageUrl,final SHARE_MEDIA medias){
			sController.getConfig().setDefaultShareLocation(false);
			UMWebPage webPage = new UMWebPage(webpageUrl);
			webPage.setTitle(title);
			webPage.setDescription(description);
			webPage.setThumb(new UMImage(mActivity, thumbData));
			sController.setShareMedia(webPage);
			postShare(mActivity, medias);
		}
		
		/**
		 * 
		 * @todo:分享文本到短信
		 * @date:2014年7月21日 上午10:28:00
		 * @author:hg_liuzl@163.com
		 */
		public void shareText2SMS(final String shareContent,final SHARE_MEDIA medias){
			SmsHandler smsHandler = new SmsHandler();
			smsHandler.addToSocialSDK();
			SmsShareContent sms = new SmsShareContent();
			sms.setShareContent(shareContent);
			sController.setShareMedia(sms);
			postShare(mActivity, medias);
		}
}
