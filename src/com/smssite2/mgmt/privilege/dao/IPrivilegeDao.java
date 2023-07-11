package com.smssite2.mgmt.privilege.dao;

import java.util.ArrayList;
import java.util.List;
import com.note.bean.Role;
import com.smssite2.mgmt.privilege.bean.MenuBean;
import com.smssite2.mgmt.privilege.form.RoleListForm;

public interface IPrivilegeDao {




List<Role> queryRoleByEId(RoleListForm roleList, String id);


Role findRoleByName(String roleName, String EId);

int deleteRole(String id, String[] roleId);
Role queryRoleByRoleId(String string, String EId);
int updateRole(String roleId, String roleName, String roleMemo, String credit, String EId);
ArrayList<MenuBean> findALlMenu(String sql);
public String findRoleId(String EId);
int saveRole(String roleId, String uname, String umemu, String credit,
		String EId);
}
