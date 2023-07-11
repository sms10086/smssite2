package com.smssite2.mgmt.basicsetting.form;

import com.note.common.page.BasicPagination;

public class PhraseListForm extends BasicPagination {
	private static final long serialVersionUID = 1L;
	private String phraseContent;
	private String phraseType;
	private String EID;
	
	public String getPhraseContent() {
		return phraseContent;
	}
	public void setPhraseContent(String phraseContent) {
		this.phraseContent = phraseContent;
	}
	public String getPhraseType() {
		return phraseType;
	}
	public void setPhraseType(String phraseType) {
		this.phraseType = phraseType;
	}
	public String getEID() {
		return EID;
	}
	public void setEID(String eid) {
		EID = eid;
	}
	
}
