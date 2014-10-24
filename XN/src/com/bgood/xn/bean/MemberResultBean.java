package com.bgood.xn.bean;
/**
 * @todo:会员实体类
 * @date:2014-10-23 下午3:45:30
 * @author:hg_liuzl@163.com
 */
public class MemberResultBean {
	
/*	  <userid/>
      <img/>// 头像
      <name/>//昵称
      <sex/>//性别
      <level/>//会员级别
      <distance/>//距离
      <signatrue/>//签名
      <ican/>//我能
      <ineed/>//我需
*/	
      
      public String userid;
      public String img;
      public String name;
      public String sex;
      public String level;
      public String distance;
      public String signatrue;
      public String ican;
      public String ineed;
      
      
      
      
	public MemberResultBean() {
		super();
	}




	public MemberResultBean(String userid, String img, String name, String sex,
			String level, String distance, String signatrue, String ican,
			String ineed) {
		super();
		this.userid = userid;
		this.img = img;
		this.name = name;
		this.sex = sex;
		this.level = level;
		this.distance = distance;
		this.signatrue = signatrue;
		this.ican = ican;
		this.ineed = ineed;
	}
      
      
	
	
}
