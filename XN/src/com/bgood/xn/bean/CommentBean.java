package com.bgood.xn.bean;

import java.io.Serializable;

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
}
