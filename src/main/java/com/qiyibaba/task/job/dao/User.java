package com.qiyibaba.task.job.dao;

import javax.persistence.Column;
import javax.persistence.Table;

// 注解在类上:提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
// 同时需要在eclipse下安装
@Table(name = "tb_user")
public class User {

	public User(){
		
	}
	
	public User(Long uid, String tag, Integer type) {
		this.uid = uid;
		this.tag = tag;
		this.type = type;
	}

	@Column(name = "uid", unique = true, nullable = false)
	private Long uid;

	@Column(name = "tag")
	private String tag;

	@Column(name = "type")
	private Integer type;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
