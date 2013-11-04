package cn.com.zdez.vo;

import java.util.List;

public class NewsVo {
	
	private int id;
	private String title;
	private String content;
	private String date;
	private String coverPath;
	private List<String> destSchools;
	private int receiverNum;
	private int receivedNum;
	private int isTop;
	
	public int getIsTop() {
		return isTop;
	}
	public void setIsTop(int isTop) {
		this.isTop = isTop;
	}
	public List<String> getDestSchools() {
		return destSchools;
	}
	public void setDestSchools(List<String> destSchools) {
		this.destSchools = destSchools;
	}
	public int getReceiverNum() {
		return receiverNum;
	}
	public void setReceiverNum(int receiverNum) {
		this.receiverNum = receiverNum;
	}
	public int getReceivedNum() {
		return receivedNum;
	}
	public void setReceivedNum(int receivedNum) {
		this.receivedNum = receivedNum;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCoverPath() {
		return coverPath;
	}
	public void setCoverPath(String coverPath) {
		this.coverPath = coverPath;
	}
	
}
