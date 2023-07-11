package com.smssite2.mgmt.basicsetting.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.note.NoteException;
import com.note.common.DbBean;
import com.note.common.TimeUtil;
import com.smssite2.mgmt.basicsetting.bean.Black;
import com.smssite2.mgmt.basicsetting.bean.Sift;
import com.smssite2.mgmt.basicsetting.dao.IBasicSettingDao;
import com.smssite2.mgmt.basicsetting.dao.impl.BasicSettingDao;
import com.smssite2.mgmt.basicsetting.form.BlackListForm;
import com.smssite2.mgmt.basicsetting.service.IBasicSettingService;

public class BasicSettingService implements IBasicSettingService {
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(BasicSettingService.class);
	
	
	@SuppressWarnings("unchecked")
	public int addPhrase(String eid, String phraseid, String type,
			String content,String staffId) {
		String sql="";
		List list =new ArrayList();
		if(phraseid==null||phraseid.trim().equals("")){
			sql="insert into Phrase(phraseContent,phraseType,staffId,Eid,addTime,updateTime) values(?,?,?,?,?,?)";
			list.add(content);
			list.add(type);
			list.add(staffId);
			list.add(eid);
			list.add(TimeUtil.now());
			list.add(TimeUtil.now());
		}else{
			sql="update Phrase set phraseContent=?,phraseType=?,updateTime=? where phraseid=?";
			list.add(content);
			list.add(type);
			list.add(TimeUtil.now());
			list.add(phraseid);
		}
		
		IBasicSettingDao dao =new BasicSettingDao();
		int a=dao.excute(sql,list);
		return a;
	}
	@SuppressWarnings("unchecked")
	public int delete(String[] ids, String eid, String table,String Id) {
		List list=new ArrayList();
		IBasicSettingDao dao =new BasicSettingDao();
		
		int a=0;
		for(String id :ids){
			if(id.equals("-1"))continue;
			String sql=" delete from "+table+" where eid='"+eid+"' and "+Id+"=? ";
			list.add(id);
			try{
				int b=	dao.excute(sql,list);
				list.clear();
				if(b>=0)a++;
			}catch(Exception e){
			}
			
		}
		
		return a;
	}
	@SuppressWarnings("unchecked")
	public int addFeast(String eid, String feastid, String type,
			String content, String fname, String worldDate, String chinadate) {
		String sql="";
		List list =new ArrayList();
		if(feastid==null||feastid.trim().equals("")){
			sql="insert into Feast(feastname,feastStyle,worldDate,chinaDate,feastContent,eid) values(?,?,?,?,?,?)";
			list.add(fname);list.add(type);list.add(worldDate);list.add(chinadate);list.add(content);list.add(eid);
		}else{
			sql="update Feast set feastname=?,feastStyle=?,worldDate=?,chinadate=?,feastContent=? where eid='"+eid+"' and feastid=? ";
			list.add(fname);list.add(type);list.add(worldDate);list.add(chinadate);list.add(content);list.add(feastid);
		}
		IBasicSettingDao dao =new BasicSettingDao();
		int a=dao.excute(sql,list);
		return a;
	}
	@SuppressWarnings("unchecked")
	public List<Black> findAllBlack(String eid, BlackListForm blackF) {
		StringBuffer sql=new StringBuffer();
		StringBuffer count=new StringBuffer();
		StringBuffer w =new StringBuffer();
		List list=new ArrayList()	;
		sql.append(" select * from black where eid=? ");
		count.append("select count(1) from black where eid=? ");
		list.add(eid);
		if(blackF.getBlackPhoneNum()!=null&&!blackF.getBlackPhoneNum().trim().equals("")){
			w.append(" and blackphonenum like ? ");
			list.add(blackF.getBlackPhoneNum().trim()+"%");
		}
		sql.append(w);
		count.append(w);
		sql.append(" order by blackphonenum");
		IBasicSettingDao dao =new BasicSettingDao();
		List arr=dao.select(sql, count, list, blackF, Black.class);
		return arr;
	}
	@SuppressWarnings("unchecked")
	public int addBlack(String eid, String blackid, String phone, String staffId) throws NoteException {
		IBasicSettingDao dao =new BasicSettingDao();
		String sql1="select * from black where eid='"+eid+"' and blackPhoneNum=? ";
		Black b=dao.findBlackByphone(sql1,new Object[]{phone});
		if(b!=null){
			throw new NoteException("该手机号码:"+phone+"已经在黑名单中！");
		}
		String sql="";
		List list =new ArrayList();
		if(blackid==null||blackid.trim().equals("")){
			sql=" insert into black(blackPhoneNum,staffId,addTime,eid) values('"+phone+"','"+staffId+"','"+TimeUtil.now()+"','"+eid+"')";
		}else {
			sql="update black set blackPhoneNum=? where eid=? and blackid=?";
			list.add(phone);list.add(eid);list.add(blackid);
			
		}
		int a=dao.excute(sql,list);
		return a;
	}
	@SuppressWarnings("unchecked")
	public List<Sift> findAllSift(String eid, BlackListForm siftF) {
		StringBuffer sql=new StringBuffer();
		StringBuffer count=new StringBuffer();
		StringBuffer w=new StringBuffer();
		List list=new ArrayList();
		sql.append(" select siftId,siftcontent,eid ,addTime from sift where eid in(?,'000000') ");
		count.append("select count(1) from sift where eid in (?,'000000') ");
		list.add(eid);
		if(siftF.getSiftContent()!=null&&!siftF.getSiftContent().trim().equals("")){
			w.append("  and siftContent like ? ");
			list.add(siftF.getSiftContent().trim()+"%");
		}
		sql.append(w);
		count.append(w);
		sql.append(" order by eid,siftcontent");
		IBasicSettingDao dao =new BasicSettingDao();
		List arr=dao.select(sql, count, list, siftF, Sift.class);
		
		if(arr!=null && arr.size()!=0){
			Sift sift = null;
			for(int i=0;i<arr.size();i++){
				sift = (Sift) arr.get(i);
				if("000000".equals(sift.getEID())){
					sift.setEID("系统");
					arr.set(i, sift);
				}
			}
		}
		return arr;
	}
	@SuppressWarnings("unchecked")
	public int addsift(String eid, String siftid, String content, String staffId) throws NoteException {
		IBasicSettingDao dao =new BasicSettingDao();
		String sql1="select count(siftId) from sift where eid in('"+eid+"','000000') and siftcontent=? ";
		int b=DbBean.getInt(sql1,new Object[]{content});
		if(b>0){
			throw new NoteException("该过滤词:"+content+"已经在列表中！");
		}
		String sql="";
		List list =new ArrayList();
		if(siftid==null||siftid.trim().equals("")){
			sql=" insert into sift(siftcontent,staffId,addTime,eid) values(?,?,?,?)";
			list.add(content);list.add(staffId);list.add(TimeUtil.now());list.add(eid);
		}else {
			sql="update sift set siftContent=? where eid=? and siftid=?";
			list.add(content);list.add(eid);list.add(siftid);
			
		}
		int a=dao.excute(sql,list);
		return a;
	}
}
