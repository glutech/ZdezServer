package cn.com.zdez.activity.po;

public class Activity {
	public static enum ActStateEnum {
		ok, cancle, finished
	}

	public static enum ActTypeEnum {
		shool_level, department_level, major_level
	}

	private int actId;
	private String actName;
	private String actAddr;
	private String actInfo;
	private int catgId;
	private int tmId;
	private ActStateEnum actState;
	private ActTypeEnum actType;

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getActAddr() {
		return actAddr;
	}

	public void setActAddr(String actAddr) {
		this.actAddr = actAddr;
	}

	public String getActInfo() {
		return actInfo;
	}

	public void setActInfo(String actInfo) {
		this.actInfo = actInfo;
	}

	public int getCatgId() {
		return catgId;
	}

	public void setCatgId(int catgId) {
		this.catgId = catgId;
	}

	public int getTmId() {
		return tmId;
	}

	public void setTmId(int tmId) {
		this.tmId = tmId;
	}

	public ActStateEnum getActState() {
		return actState;
	}

	public void setActState(ActStateEnum actState) {
		this.actState = actState;
	}

	public ActTypeEnum getActType() {
		return actType;
	}

	public void setActType(ActTypeEnum actType) {
		this.actType = actType;
	}

}
