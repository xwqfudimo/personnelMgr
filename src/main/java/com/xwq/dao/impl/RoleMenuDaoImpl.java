package com.xwq.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xwq.dao.RoleMenuDao;
import com.xwq.model.RoleMenu;

@Repository("roleMenuDao")
public class RoleMenuDaoImpl extends BaseDaoImpl<RoleMenu> implements RoleMenuDao {
	/**
	 * 批插入
	 */
	@Override
	public void batchAdd(List<RoleMenu> rms) {
		String sql = "insert into role_menu(role_id, menu_id) values";
		for(RoleMenu rm : rms) {
			sql += "("+ rm.getRoleId() + "," + rm.getMenuId() + "),";
		}
		sql = sql.substring(0, sql.length()-1);
		getSQLQuery(sql).executeUpdate();
	}
}
