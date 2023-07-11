package com.smssite2.mgmt.linkman.dao;

import java.util.List;
import java.util.Map;
import com.smssite2.mgmt.linkman.bean.Group;
import com.smssite2.mgmt.linkman.bean.Linkman;
import com.smssite2.mgmt.linkman.form.LinkmanListForm;
import com.smssite2.mgmt.message.bean.PhoneBean;

public interface ILinkmanDao {	
	@SuppressWarnings("unchecked")
	List<Group> findAllGroup(String sql, List list);

	List<Linkman> select(StringBuffer sql, StringBuffer count,
			LinkmanListForm manF, Class<Linkman> class1,String flag);

	@SuppressWarnings("unchecked")
	Map findGroup(String staffId, String eid);

	Linkman findLinkman(String sql, Object[] params);

	PhoneBean findPhone(String sql);
	
}
