package com.xwq.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xwq.dao.RolePrivilegeDao;
import com.xwq.model.RolePrivilege;

@Repository("roleResourceDao")
public class RoleResourceDaoImpl extends BaseDaoImpl<RolePrivilege> implements
		RolePrivilegeDao {

	/**
	 * 批插入
	 */
	@Override
	public void batchAdd(List<RolePrivilege> rps) {
		String sql = "insert into role_privilege(role_id, privilege_id) values";
		for(RolePrivilege rp : rps) {
			sql += "("+ rp.getRoleId() + "," + rp.getPrivilegeId() + "),";
		}
		sql = sql.substring(0, sql.length()-1);
		getSQLQuery(sql).executeUpdate();
	}

}
