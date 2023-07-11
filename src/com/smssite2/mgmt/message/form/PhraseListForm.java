package com.smssite2.mgmt.message.form;

import com.note.common.page.BasicPagination;

public class PhraseListForm extends BasicPagination{
	private String phraseContent;
	private String phraseType;
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
}
