package com.xwq.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwq.dao.PrivilegeDao;
import com.xwq.dao.RolePrivilegeDao;
import com.xwq.model.Privilege;
import com.xwq.model.RolePrivilege;
import com.xwq.service.PrivilegeService;
import com.xwq.util.Pagination;

@Service("PrivilegeService")
public class PrivilegeServiceImpl implements PrivilegeService {
	@Autowired
	private PrivilegeDao PrivilegeDao;
	@Autowired
	private RolePrivilegeDao rolePrivilegeDao;

	@Override
	public void add(Privilege res) {
		this.PrivilegeDao.add(res);
	}

	@Override
	public void delete(int id) {
		this.PrivilegeDao.delete(id);
	}

	@Override
	public void update(Privilege res) {
		this.PrivilegeDao.update(res);
	}

	@Override
	public Privilege get(int id) {
		return this.PrivilegeDao.get(id);
	}

	/**
	 * 分页查询权限列表
	 */
	@Override
	public List<Privilege> list(String search) {
		int totalCount = Integer.parseInt(this.PrivilegeDao.query("select count(id) from Privilege where name like ?", "%"+search+"%").toString());
		Pagination.setTotalCount(totalCount);
		
		return this.PrivilegeDao.getListByPage("from Privilege where name like ?", Pagination.getOffset(), Pagination.getPageSize(), "%"+search+"%");
	}

	/**
	 * 根据用户id查询用户所拥有的权限
	 */
	@Override
	public List<Privilege> listByUserId(int userId) {
		String hql = "select pri from User u, Role r, Privilege pri, UserRole ur, RolePrivilege rp " + 
						"where u.id=ur.userId AND r.id=ur.roleId AND r.id=rp.roleId AND pri.id=rp.privilegeId " + 
						"AND u.id = ?";
		return this.PrivilegeDao.getList(hql, userId);
	}

	/**
	 * 根据角色id查询角色所拥有的权限
	 */
	@Override
	public List<Privilege> listByRoleId(int roleId) {
		String hql = "select pri from Role r, Privilege pri, RolePrivilege rp where r.id=rp.roleId AND pri.id=rp.privilegeId AND r.id = ?";
		return this.PrivilegeDao.getList(hql, roleId);
	}

	/**
	 * 新增角色-权限关联
	 */
	@Override
	public void addRolePrivilege(int roleId, int priId) {
		RolePrivilege rr = new RolePrivilege();
		rr.setRoleId(roleId);
		rr.setPrivilegeId(priId);
		
		this.rolePrivilegeDao.add(rr);
	}

	/**
	 * 删除角色-权限关联
	 */
	@Override
	public void deleteRolePrivilege(int roleId, int priId) {
		String hql = "delete from RolePrivilege rp where rp.roleId=? and rp.privilegeId=?";
		this.rolePrivilegeDao.execute(hql, roleId, priId);
	}

	/**
	 * 根据group分组查询权限列表,Map： key 分组名称，  value 相同分组权限列表
	 */
	@Override
	public Map<String, List<Privilege>> listByGroup() {
		List<Privilege> list = this.PrivilegeDao.getList("from Privilege");
		
		Map<String, List<Privilege>> map = new HashMap<String, List<Privilege>>();
		
		for(Privilege p : list) {
			if(map.containsKey(p.getGroup())) {
				List<Privilege> mpList = map.get(p.getGroup());
				mpList.add(p);
				map.put(p.getGroup(), mpList);
			}
			else {
				List<Privilege> pList = new ArrayList<Privilege>();
				pList.add(p);
				map.put(p.getGroup(), pList);
			}
		}
		
		return map;
	}

	/**
	 * 批量添加角色-权限关联
	 */
	@Override
	public void batchAddRolePrivilege(List<RolePrivilege> rps) {
		this.rolePrivilegeDao.batchAdd(rps);
	}

	/**
	 * 分组查询指定角色id的权限列表
	 */
	@Override
	public Map<String, List<Privilege>> listByGroupAndRoleId(int roleId) {
		String hql = "select p from Privilege p, Role r, RolePrivilege rp where p.id=rp.privilegeId AND r.id=rp.roleId AND r.id=?";
		List<Privilege> list = this.PrivilegeDao.getList(hql, roleId);
		
		Map<String, List<Privilege>> map = new HashMap<String, List<Privilege>>();
		
		for(Privilege p : list) {
			if(map.containsKey(p.getGroup())) {
				List<Privilege> mpList = map.get(p.getGroup());
				mpList.add(p);
				map.put(p.getGroup(), mpList);
			}
			else {
				List<Privilege> pList = new ArrayList<Privilege>();
				pList.add(p);
				map.put(p.getGroup(), pList);
			}
		}
		
		return map;
	}

	/**
	 * 删除指定id角色的所用角色权限关联
	 * @param roleId
	 */
	@Override
	public void deleteAllRolePrivilege(int roleId) {
		String hql = "delete from RolePrivilege where roleId = ?";
		this.rolePrivilegeDao.execute(hql, roleId);
	}

	/**
	 * 查询指定username的用户所拥有的权限的uri列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllPrivilegeUrlByUsername(String username) {
		String hql = "select p.uri from User u, Role r, UserRole ur, Privilege p, RolePrivilege rp " + 
						"where u.id=ur.userId and r.id=ur.roleId and r.id=rp.roleId and p.id=rp.privilegeId and u.username = ?";
		
		return this.PrivilegeDao.queryList(hql, username);
	}


}
