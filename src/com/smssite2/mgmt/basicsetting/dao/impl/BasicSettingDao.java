package com.smssite2.mgmt.basicsetting.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import com.note.common.DbBean;
import com.smssite2.mgmt.basicsetting.bean.Black;
import com.smssite2.mgmt.basicsetting.bean.Feast;
import com.smssite2.mgmt.basicsetting.bean.Phrase;
import com.smssite2.mgmt.basicsetting.bean.Sift;
import com.smssite2.mgmt.basicsetting.dao.IBasicSettingDao;
import com.smssite2.mgmt.basicsetting.form.BlackListForm;
import com.smssite2.mgmt.basicsetting.form.FeastListForm;
import com.smssite2.mgmt.basicsetting.form.PhraseListForm;
import com.smssite2.mgmt.basicsetting.form.SiftListForm;

public class BasicSettingDao implements IBasicSettingDao {
	public boolean addSift(Sift sift) throws Exception {
		String sql = " insert into Sift(EID,SiftContent,staffId,AddTime) values(?,?,?,?,?)";
		int sc = DbBean.executeUpdate(sql,
				 new Object[] {sift.getEID(),sift.getSiftContent(),sift.getSiftId(),sift.getAddTime()});
		return sc==1;
	}

	public boolean deleteSift(int id) throws Exception {
		String sql = " delete from Sift where SiftID=? ";
		int r = DbBean.executeUpdate(sql,new Object[]{id});
		return r==1;
	}
	
	public boolean modifySift(Sift sift) throws Exception {
		String sql = " update Sift set EID=?,SiftContent=?,staffId=?,AddTime=? where SiftID=?";
		return DbBean.executeUpdate(sql,
				new Object[]{sift.getEID(),sift.getSiftContent(),sift.getSiftId(),sift.getAddTime(),sift.getSiftId()})==1 ;
	}
	
	public Sift querySiftByID(int id) throws Exception {
		String sql="select * from Sift where SiftID=?";
		Sift sift=(Sift)DbBean.singleObject(sql, new Object[]{id}, Sift.class);
		return sift;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Sift> querySiftWithPagination(SiftListForm form) throws Exception {
		ArrayList p0 = new ArrayList();
				
		StringBuffer sb = new StringBuffer();
		StringBuffer w = new StringBuffer();
		
		sb.append("select SiftID,EID,SiftContent,staffId,AddTime from Sift where rownum < ? ");
			
		if (form.getSiftContent()!=null && form.getSiftContent().trim()!=null) {
			w.append(" and SiftContent like '% ").append(StringEscapeUtils.escapeSql(form.getSiftContent())).append("%'");
		}
		
		String sqlCount = "select count(SiftID) from Sift ";
		if (w.length() > 0) {
			sb.append(w);
			w.delete(0, 4);
			sqlCount += " where " + w.toString();
		}
		sb.append(") where seq >=? ");
		sb.append(" order by AddTime desc ");
		Object[] x = p0.toArray(new Object[p0.size()]);
		
		form.setTotalItems(DbBean.getInt(sqlCount, x));
		form.adjustPageIndex();
				
		p0.add(0, new Integer(form.getEndIndex()+1));
		p0.add(new Integer(form.getStartIndex()+1));
		
		Object[] p = p0.toArray(new Object[p0.size()]);

		return DbBean.select(sb.toString(), p, 0, form.getPageSize(), Sift.class);
	}
	
	public boolean addBlack(Black black) throws Exception {
		String sql = " insert into Black(blackPhoneNum,staffId,EID,addTime) values(?,?,?,?)";
		int sc = DbBean.executeUpdate(sql,
				 new Object[]{black.getBlackPhoneNum(),black.getStaffId(),black.getEID(),black.getAddTime()});
		return sc == 1;
	}

	public boolean deleteBlack(int id) throws Exception {
		String sql = " delete from Black where blackId=? ";
		int r = DbBean.executeUpdate(sql,new Object[]{id});
		return r==1;
	}
	
	public boolean modifyBlack(Black black) throws Exception {
		String sql = " update Black set blackPhoneNum=?,staffId=?,EID=?,addTime=? where blackId=?";
		return DbBean.executeUpdate(sql,
			new Object[]{black.getBlackPhoneNum(),black.getStaffId(),black.getEID(),black.getAddTime(),black.getBlackId()})==1 ;
	}
	
	public Black queryBlackByID(int id) throws Exception {
		String sql = "select * from Black where blackId=?";
		Black black = (Black)DbBean.singleObject(sql, new Object[]{id}, Black.class);
		return black;
	}
	
	@SuppressWarnings("unchecked")
	public List<Black> queryBlackWithPagination(BlackListForm form)	throws Exception {
		ArrayList p0 = new ArrayList();
		
		StringBuffer sb = new StringBuffer();
		StringBuffer w = new StringBuffer();
		
		sb.append("select blackId,blackPhoneNum,staffId,addTime,EID from Black where 1=1 ");
			
		if (form.getBlackPhoneNum()!=null && form.getBlackPhoneNum().trim()!=null) {
			w.append(" and blackPhoneNum like '% ").append(StringEscapeUtils.escapeSql(form.getBlackPhoneNum())).append("%'");
		}
		
		String sqlCount = "select count(blackId) from Sift ";
		if (w.length() > 0) {
			sb.append(w);
			w.delete(0, 4);
			sqlCount += " where " + w.toString();
		}
		sb.append(") where seq >=? ");
		
		Object[] x = p0.toArray(new Object[p0.size()]);
		
		form.setTotalItems(DbBean.getInt(sqlCount, x));
		form.adjustPageIndex();
				
		p0.add(0, new Integer(form.getEndIndex()+1));
		p0.add(new Integer(form.getStartIndex()+1));
		
		Object[] p = p0.toArray(new Object[p0.size()]);

		return DbBean.select(sb.toString(), p, 0, form.getPageSize(), Sift.class);
}
	
	public boolean addFeast(Feast feast) throws Exception {
		String sql = " insert into Feast(feastName,feastStyle,worldDate,chinaDate,feastContent,EID) values(?,?,?,?,?,?)";
		int sc = DbBean.executeUpdate(sql,
				new Object[]{feast.getFeastName(),feast.getFeastStyle(),feast.getWorldDate(),feast.getChinaDate(),
				             feast.getFeastContent(),feast.getEID()});
		return sc == 1;
	}

	public boolean deleteFeast(int id) throws Exception {
		String sql = " delete from Feast where feastId=? ";
		int r = DbBean.executeUpdate(sql,new Object[]{id});
		return r==1;
	}
	
	public boolean modifyFeast(Feast feast) throws Exception {
		String sql = " update Feast set feastName=?,feastStyle=?,worldDate=?,chinaDate=?,feastContent=?,EID=? where feastId=?";
		return DbBean.executeUpdate(sql,
			new Object[]{feast.getFeastName(),feast.getFeastStyle(),feast.getWorldDate(),feast.getChinaDate(),
				         feast.getFeastContent(),feast.getEID(),feast.getFeastId()})==1 ;
	}
	
	public Feast queryFeastByID(int id) throws Exception {
		String sql = "select * from Feast where feastId=?";
		Feast feast = (Feast)DbBean.singleObject(sql, new Object[]{id}, Feast.class);
		return feast;
	}
	

	@SuppressWarnings("unchecked")
	public List<Feast> queryFeastWithPagination(FeastListForm form)	throws Exception {
		ArrayList p0 = new ArrayList();
		
		StringBuffer sb = new StringBuffer();
		StringBuffer w = new StringBuffer();
		
		sb.append("select feastId,feastName,feastStyle,worldDate,chinaDate,feastContent,EID from Feast where 1=1 ");
			
		if (form.getEID()!=null && form.getEID().trim()!=null) {
			w.append(" and EID=? ").append(form.getEID());
			p0.add(form.getEID());
		}
		if(form.getFeastName()!=null && form.getFeastName().trim()!=null){
			w.append(" and feastName like '% ").append(StringEscapeUtils.escapeSql(form.getFeastName())).append("%'");
		}
		
		String sqlCount = "select count(feastId) from Feast ";
		if (w.length() > 0) {
			sb.append(w);
			w.delete(0, 4);
			sqlCount += " where " + w.toString();
		}
		sb.append(") where seq >=? ");
		
		Object[] x = p0.toArray(new Object[p0.size()]);
		
		form.setTotalItems(DbBean.getInt(sqlCount, x));
		form.adjustPageIndex();
				
		p0.add(0, new Integer(form.getEndIndex()+1));
		p0.add(new Integer(form.getStartIndex()+1));
		
		Object[] p = p0.toArray(new Object[p0.size()]);

		return DbBean.select(sb.toString(), p, 0, form.getPageSize(), Feast.class);
	}

	public boolean addPhrase(Phrase phrase) throws Exception {
		String sql = " insert into Phrase(phraseContent,phraseType,staffId,EID,addTime,updateTime) values(?,?,?,?,?,?) ";
		int sc = DbBean.executeUpdate(sql,
				new Object[]{phrase.getPhraseContent(),phrase.getPhraseType(),phrase.getStaffId(),phrase.getEID(),
				             phrase.getAddTime(),phrase.getUpdateTime()});
		return sc ==1;
	}

	public boolean deletePhrase(int id) throws Exception {
		String sql = " delete from Phrase where phraseId=? ";
		int r = DbBean.executeUpdate(sql,new Object[]{id});
		return r==1;
	}

	public boolean modifyPhrase(Phrase phrase) throws Exception {
		String sql = " update Phrase set phraseContent=?,phraseType=?,staffId=?,EID=?,addTime=?,updateTime=? where phraseId=?";
		return DbBean.executeUpdate(sql,
			new Object[]{phrase.getPhraseContent(),phrase.getPhraseType(),phrase.getStaffId(),phrase.getEID(),
				         phrase.getAddTime(),phrase.getUpdateTime(),phrase.getPhraseId()})==1 ;
	}

	public Phrase queryPhraseByID(int id) throws Exception {
		String sql = "select * from Phrase where phraseId=?";
		Phrase phrase = (Phrase)DbBean.singleObject(sql, new Object[]{id}, Phrase.class);
		return phrase;
	}

	@SuppressWarnings("unchecked")
	public List<Phrase> queryPhraseWithPagination(PhraseListForm form) throws Exception {
		ArrayList p0 = new ArrayList();
		
		StringBuffer sb = new StringBuffer();
		StringBuffer w = new StringBuffer();
		
		sb.append("select phraseId,phraseContent,phraseType,staffId,EID,addTime,updateTime from Phrase where 1=1 ");
			
		if (form.getEID()!=null && form.getEID().trim()!=null) {
			w.append(" and EID=? ").append(form.getEID());
			p0.add(form.getEID());
		}
		
		String sqlCount = "select count(phraseId) from Phrase ";
		if (w.length() > 0) {
			sb.append(w);
			w.delete(0, 4);
			sqlCount += " where " + w.toString();
		}
		sb.append(") where seq >=? ");
		
		Object[] x = p0.toArray(new Object[p0.size()]);
		
		form.setTotalItems(DbBean.getInt(sqlCount, x));
		form.adjustPageIndex();
				
		p0.add(0, new Integer(form.getEndIndex()+1));
		p0.add(new Integer(form.getStartIndex()+1));
		
		Object[] p = p0.toArray(new Object[p0.size()]);

		return DbBean.select(sb.toString(), p, 0, form.getPageSize(), Feast.class);
	}
	@SuppressWarnings({ "static-access", "unchecked" })
	public int excute(String sql, List list) {
		DbBean db= new DbBean();
		try{
			int a=db.executeUpdate(sql, list.toArray());
			return a;
		}catch(Exception e){
			db.rollback();
			e.printStackTrace();
			return 0;
		}
	}

		@SuppressWarnings("unchecked")
		public List select(StringBuffer sql, StringBuffer count, List list,
				BlackListForm blackF, Class class1) {
				blackF.setTotalItems(DbBean.getInt(count.toString(), list.toArray()));
				blackF.adjustPageIndex();
				
			return DbBean.select(sql.toString(), list.toArray(), blackF.getStartIndex(), blackF.getPageSize(), class1);
		}

		public Black findBlackByphone(String sql1, Object[] objects) {
			
			return (Black) DbBean.selectFirst(sql1, objects, Black.class);
		}

		public Sift findSiftBycontent(String sql1, Object[] objects) {
			 return (Sift) DbBean.selectFirst(sql1, objects, Sift.class);
		}
		public String getId(String string, Object[] objects){
			return DbBean.getString(string, objects);
		}
}
