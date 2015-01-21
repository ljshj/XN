package com.bgood.xn.bean;

import com.bgood.xn.system.SystemConfig;

/**
 * @todo:会员实体类
 * @date:2014-10-23 下午3:45:30
 * @author:hg_liuzl@163.com
 */
public class MemberResultBean {

	public String userid;
      public String photo;
      public String name;
      public String sex;
      public String level;
      public String distance;
      public String signatrue;
      public String ican;
      public String ineed;
      public int searchtype;
      public int guanzhutype;
      
  	public String getPhoto() {
		return SystemConfig.FILE_SERVER + photo;
	}
}



