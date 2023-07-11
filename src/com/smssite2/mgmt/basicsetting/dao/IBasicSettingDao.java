package com.smssite2.mgmt.basicsetting.dao;

import java.util.List;
import com.smssite2.mgmt.basicsetting.bean.*;
import com.smssite2.mgmt.basicsetting.form.*;

public interface IBasicSettingDao {	boolean addSift(Sift sift) throws Exception;
	boolean deleteSift(int id) throws Exception;
	boolean modifySift(Sift sift) throws Exception;
	Sift querySiftByID(int id) throws Exception;
	List<Sift> querySiftWithPagination(SiftListForm form) throws Exception;
	
	boolean addBlack(Black black) throws Exception;
	boolean deleteBlack(int id) throws Exception;
	boolean modifyBlack(Black black) throws Exception;
	Black queryBlackByID(int id) throws Exception;
	List<Black> queryBlackWithPagination(BlackListForm form) throws Exception;
	
	boolean addPhrase(Phrase phrase) throws Exception;
	boolean deletePhrase(int id) throws Exception;
	boolean modifyPhrase(Phrase phrase) throws Exception;
	Phrase queryPhraseByID(int id) throws Exception;
	List<Phrase> queryPhraseWithPagination(PhraseListForm form) throws Exception;
	
	boolean addFeast(Feast feast) throws Exception;
	boolean deleteFeast(int id) throws Exception;
	boolean modifyFeast(Feast feast) throws Exception;
	Feast queryFeastByID(int id) throws Exception;
	List<Feast> queryFeastWithPagination(FeastListForm form) throws Exception;
	@SuppressWarnings("unchecked")
	int excute(String sql, List list);
	@SuppressWarnings("unchecked")
	List select(StringBuffer sql, StringBuffer count, List list,
			BlackListForm blackF, Class class1);
	Black findBlackByphone(String sql1, Object[] objects);
	Sift findSiftBycontent(String sql1, Object[] objects);
	String getId(String string, Object[] objects);
}
