package com.smssite2.mgmt.basicsetting.service;

import java.util.List;
import com.note.NoteException;
import com.smssite2.mgmt.basicsetting.bean.Black;
import com.smssite2.mgmt.basicsetting.bean.Sift;
import com.smssite2.mgmt.basicsetting.form.BlackListForm;

public interface IBasicSettingService {	int addPhrase(String eid, String phraseid, String type, String content, String staffId);
	int delete(String[] ids, String eid, String table, String string);
	int addFeast(String eid, String feastid, String type, String content,
			String fname, String worldDate, String chinadate);
	List<Black> findAllBlack(String eid, BlackListForm blackF);
	int addBlack(String eid, String phraseid, String type, String staffId) throws NoteException;
	List<Sift> findAllSift(String eid, BlackListForm siftF);
	int addsift(String eid, String siftid, String content, String staffId) throws NoteException;
}
