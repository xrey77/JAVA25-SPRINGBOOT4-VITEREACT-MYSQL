package com.java25.java25.springboot4.mysql.dto;

public class UserlistDto {
	private long id;
	private int role_id;
	private String firstname;
	private String lastname;
	private String email;
	private String mobile;
	private String username;
	private String roles;
	private int isactivated;
	private int isblocked;
	private String userpic;
	private String secret;
	private String qrcodeurl;
	
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
		
	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMobile() {
		return mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getRoles() {
		return roles;
	}
	
	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public int getIsactivated() {
		return isactivated;
	}
	
	public void setIsactivated(int isactivated) {
		this.isactivated = isactivated;
	}
	
	public int getIsblocked() {
		return isblocked;
	}
	
	public void setIsblocked(int isblocked) {
		this.isblocked = isblocked;
	}
	
	public String getUserpic() {
		return userpic;
	}
	
	public void setUserpic(String userpic) {
		this.userpic = userpic;
	}
	
	public String getSecret() {
        return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;		
	}

	public String getQrcodeurl() {
		return qrcodeurl;
	}
	
	public void setQrcodeurl(String qrcodeurl) {
		this.qrcodeurl = qrcodeurl;
	}	
}