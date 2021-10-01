package com.jang.bbs.model;

import javax.validation.constraints.NotEmpty;

public class AdminVO {
	
	@NotEmpty(message = "id �� �Է��ϼ���")
	private String adminid;
	@NotEmpty(message = "�н����� �� �Է��ϼ���")
	private String password;
	private String name;
	
	public String getAdminid() {
		return adminid;
	}
	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
