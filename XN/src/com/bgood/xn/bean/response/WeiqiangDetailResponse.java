package com.bgood.xn.bean.response;

import java.util.List;

import com.bgood.xn.bean.WeiqiangCommentBean;

/**
 * @todo:微墙详情
 * @date:2014-10-28 上午10:25:43
 * @author:hg_liuzl@163.com
 */
public class WeiqiangDetailResponse {
	public List<WeiqiangCommentBean> comments;
}


//<msgbody>
//<content/> //内容
//<imgs> 图片列表
//   <img>product_img.jpg</img>//大图<img_thum>product_img_thum.jpg</img_thum>//缩略图
// <like_count/>
// <comment_count/>
// <forward_count/>
// <share_count/>
//
//</imgs>
//<comments>
//  <item>
//     <userid/>
//      <name/>//用户名
//      <photo/>//用户头像
//      <content/>//评论内容
//  </item>
//</comments>
//</msgbody>