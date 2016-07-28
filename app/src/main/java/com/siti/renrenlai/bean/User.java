package com.siti.renrenlai.bean;

import com.siti.renrenlai.util.ConstantValue;

import java.io.Serializable;

public class User implements Serializable{

	private int userId;
	private String groupName;
	private String userName;
	private String userHeadPicImagePath;
	private String sex;
	private String realName;
	private String interetsAndHobbies;
	private String introduction;
	private String telephone;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserHeadPicImagePath() {
		return ConstantValue.urlRoot + userHeadPicImagePath;
	}

	public void setUserHeadPicImagePath(String userHeadPicImagePath) {
		this.userHeadPicImagePath = userHeadPicImagePath;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getInteretsAndHobbies() {
		return interetsAndHobbies;
	}

	public void setInteretsAndHobbies(String interetsAndHobbies) {
		this.interetsAndHobbies = interetsAndHobbies;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
