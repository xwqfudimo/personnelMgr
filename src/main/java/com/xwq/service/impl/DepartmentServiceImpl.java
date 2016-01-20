package com.xwq.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwq.dao.DepartmentDao;
import com.xwq.model.Department;
import com.xwq.model.Pagination;
import com.xwq.service.DepartmentService;
import com.xwq.vo.DeptVo;

@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	private DepartmentDao departmentDao;

	@Override
	public void add(Department t) {
		this.departmentDao.add(t);
	}

	@Override
	public void delete(int id) {
		this.departmentDao.delete(id);
	}

	@Override
	public void update(Department t) {
		this.departmentDao.update(t);
	}

	@Override
	public Department get(int id) {
		return this.departmentDao.get(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeptVo> getDeptList() {
		String hql = "select id, name from Department order by sort";
		List<Object[]> list = this.departmentDao.queryList(hql);
		
		List<DeptVo> voList = new ArrayList<DeptVo>();
		for(Object[] objs : list) {
			DeptVo vo = new DeptVo();
			vo.setId(Integer.parseInt(objs[0].toString()));
			vo.setName(objs[1].toString());
			
			voList.add(vo);
		}
		
		return voList;
	}

	@Override
	public List<Department> list() {
		int totalCount = Integer.parseInt(this.departmentDao.query("select count(*) from Department").toString());
		Pagination.setTotalCount(totalCount);
		
		return this.departmentDao.getListByPage("from Department", Pagination.getOffset(), Pagination.getPageSize());
	}
}
