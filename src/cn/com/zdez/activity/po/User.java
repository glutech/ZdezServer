package cn.com.zdez.activity.po;

public class User {
	public static enum UsrTypeEnum {
		teacher, student, enterprise
	}

	public static enum UsrAbleStateEnum {
		yes, no
	}

	private int usrId;
	private String usrSignature;
	private String usrAvatar;
	private String usrPhone;
	private String usrNickname;
	private int stuId;
	private UsrAbleStateEnum usrAbleState;
	private UsrTypeEnum usrTypeEnum;

	public int getUsrId() {
		return usrId;
	}

	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}

	public String getUsrSignature() {
		return usrSignature;
	}

	public void setUsrSignature(String usrSignature) {
		this.usrSignature = usrSignature;
	}

	public String getUsrAvatar() {
		return usrAvatar;
	}

	public void setUsrAvatar(String usrAvatar) {
		this.usrAvatar = usrAvatar;
	}

	public String getUsrPhone() {
		return usrPhone;
	}

	public void setUsrPhone(String usrPhone) {
		this.usrPhone = usrPhone;
	}

	public String getUsrNickname() {
		return usrNickname;
	}

	public void setUsrNickname(String usrNickname) {
		this.usrNickname = usrNickname;
	}

	public int getStuId() {
		return stuId;
	}

	public void setStuId(int stuId) {
		this.stuId = stuId;
	}

	public UsrAbleStateEnum getUsrAbleStateEnum() {
		return usrAbleState;
	}

	public void setUsrAbleStateEnum(UsrAbleStateEnum usrAbleState) {
		this.usrAbleState = usrAbleState;
	}

	public UsrTypeEnum getUsrTypeEnum() {
		return usrTypeEnum;
	}

	public void setUsrTypeEnum(UsrTypeEnum usrTypeEnum) {
		this.usrTypeEnum = usrTypeEnum;
	}
}
