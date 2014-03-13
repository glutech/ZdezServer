package cn.com.zdez.activity.po;

import java.sql.Date;

public class Notice {

	public static enum NoteStateEnum {
		ok, cancel, delete;
	}

	private int noteId;
	private Date noteTime;
	private String noteContent;
	private NoteStateEnum noteState;
	private int actId;

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public Date getNoteTime() {
		return noteTime;
	}

	public void setNoteTime(Date noteTime) {
		this.noteTime = noteTime;
	}

	public String getNoteContent() {
		return noteContent;
	}

	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}

	public NoteStateEnum getNoteState() {
		return noteState;
	}

	public void setNoteState(NoteStateEnum noteState) {
		this.noteState = noteState;
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int actId) {
		this.actId = actId;
	}

}
