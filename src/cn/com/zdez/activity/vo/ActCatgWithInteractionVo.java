package cn.com.zdez.activity.vo;

import cn.com.zdez.activity.bo.ActInteractionBo;

public class ActCatgWithInteractionVo {

	private int catgId;
	private String catgName;
	private ActInteractionBo actInteractionBo;

	public int getCatgId() {
		return catgId;
	}

	public void setCatgId(int catgId) {
		this.catgId = catgId;
	}

	public String getCatgName() {
		return catgName;
	}

	public void setCatgName(String catgName) {
		this.catgName = catgName;
	}

	public ActInteractionBo getActInteractionBo() {
		return actInteractionBo;
	}

	public void setActInteractionBo(ActInteractionBo actInteractionBo) {
		this.actInteractionBo = actInteractionBo;
	}

}
