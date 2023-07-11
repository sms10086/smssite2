package com.smssite2.mgmt.linkman.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.note.common.DbBean;
import com.smssite2.mgmt.linkman.bean.Group;
import com.smssite2.mgmt.linkman.bean.Linkman;
import com.smssite2.mgmt.linkman.dao.ILinkmanDao;
import com.smssite2.mgmt.linkman.form.LinkmanListForm;
import com.smssite2.mgmt.message.bean.PhoneBean;

public class LinkmanDao implements ILinkmanDao{
	@SuppressWarnings("unchecked")
	public List<Group> findAllGroup(String sql, List list) {
		
		return DbBean.select(sql, list.toArray(), 0, Integer.MAX_VALUE, Group.class);
	}

	@SuppressWarnings("unchecked")
	public List<Linkman> select(StringBuffer sql, StringBuffer count,
			 LinkmanListForm manF, Class<Linkman> class1,String flag) {
		if(flag!=null&&flag.equals("repout")){
			
		}else{
			manF.setTotalItems(DbBean.getInt(count.toString(), null));
			manF.adjustPageIndex();
		}
		List<Linkman> lists= DbBean.select(sql.toString(), null, 0, manF.getPageSize(), class1);
		return lists;
	
	}

	@SuppressWarnings("unchecked")
	public Map findGroup(String staffId, String eid) {
		List<Group> list =new ArrayList<Group>();
		if(!staffId.trim().toLowerCase().equals("admin")){
			String sql=" select groupid ,groupname from lingroup where eid=? and staffid=? union  select groupid ,groupname||'('||staffId||'¹²Ïí)' as groupname from lingroup where eid=? and staffid<>? and isshare='Y'";
			list=DbBean.select(sql, new Object[]{eid,staffId,eid,staffId}, 0, 10000, Group.class);
		}else{
			String sql=" select groupid,staffId," +
					"groupname as Groupname,isshare" +
					" from lingroup where eid=? ";
			list=DbBean.select(sql, new Object[]{eid}, 0, 10000, Group.class);
			if(list!=null && list.size()!=0){
				Group group = null;
				for(int i=0;i<list.size();i++){
					group = list.get(i);
					if(!staffId.equals(group.getStaffId())){
						if("Y".equals(group.getIsShare())){
							group.setGroupName(group.getGroupName()+"("+group.getStaffId()+"¹²Ïí)");
						}else{
							group.setGroupName(group.getGroupName()+"("+group.getStaffId()+"Ë½ÓÐ)");
						}
						list.set(i, group);
					}
				}
			}
		}
		Map map=new HashMap();
		for(Group g:list){
			map.put(g.getGroupId(), g.getGroupName());
		}
		return map;
	}

	public Linkman findLinkman(String sql, Object[] params) {
		
		return (Linkman)DbBean.selectFirst(sql, params, Linkman.class);
	}

	public PhoneBean findPhone(String sql) {
		
		return (PhoneBean) DbBean.selectFirst(sql, null,PhoneBean.class);
	}
	 
}
