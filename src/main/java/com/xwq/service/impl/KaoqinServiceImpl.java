package com.xwq.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xwq.dao.KaoqinDao;
import com.xwq.model.Kaoqin;
import com.xwq.service.KaoqinService;
import com.xwq.util.DateUtil;
import com.xwq.util.Pagination;

@Service("kaoqinService")
public class KaoqinServiceImpl implements KaoqinService {
	@Autowired
	private KaoqinDao kaoqinDao;

	@Override
	public void add(Kaoqin t) {
		this.kaoqinDao.add(t);
	}

	@Override
	public void delete(int id) {
		this.kaoqinDao.delete(id);
	}

	@Override
	public void update(Kaoqin t) {
		this.kaoqinDao.update(t);
	}

	@Override
	public Kaoqin get(int id) {
		return this.kaoqinDao.get(id);
	}

	@Override
	public List<Kaoqin> getListByEmpId(int empId, String filter) {
		List<Kaoqin> result = null;
		if(filter == null || "this-month".equals(filter)) {
			String hql = "from Kaoqin kq where kq.employee.id = ? and kq.date like ? order by kq.date desc";
			
			int totalCount = Integer.parseInt(this.kaoqinDao.query("select count(*) " + hql, empId, DateUtil.getThisMonth() + "%").toString());
			Pagination.setTotalCount(totalCount);
			
			result = this.kaoqinDao.getListByPage(hql, Pagination.getOffset(), Pagination.getPageSize(), empId, DateUtil.getThisMonth() + "%");
		}
		else if("last-month".equals(filter)) {
			String hql = "from Kaoqin kq where kq.employee.id = ? and kq.date like ? order by kq.date desc";
			
			int totalCount = Integer.parseInt(this.kaoqinDao.query("select count(*) " + hql, empId, DateUtil.getLastMonth() + "%").toString());
			Pagination.setTotalCount(totalCount);
			
			result = this.kaoqinDao.getListByPage(hql, Pagination.getOffset(), Pagination.getPageSize(), empId, DateUtil.getLastMonth() + "%");
		}
		else {
			String hql = "from Kaoqin kq where kq.employee.id = ? order by kq.date desc";
			
			int totalCount = Integer.parseInt(this.kaoqinDao.query("select count(*) " + hql, empId).toString());
			Pagination.setTotalCount(totalCount);
			
			result = this.kaoqinDao.getListByPage(hql, Pagination.getOffset(), Pagination.getPageSize(), empId);
		}
			
		return result;
	}

}
