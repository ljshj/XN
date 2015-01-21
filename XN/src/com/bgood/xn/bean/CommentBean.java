package com.bgood.xn.bean;

import java.io.Serializable;

import com.bgood.xn.system.SystemConfig;

/**
 * 评论的实体类(微墙，幽默秀，产品)
 */
public class CommentBean implements Serializable
{

	private static final long serialVersionUID = 1L;
	public String userid;
    public String name;
    public String photo;
    public String content;
    public String commenttime;
    
	public String getPhoto() {
		return SystemConfig.FILE_SERVER + photo;
	}
}
