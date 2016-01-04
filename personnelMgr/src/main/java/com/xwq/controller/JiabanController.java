package com.xwq.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xwq.model.Jiaban;
import com.xwq.util.DateUtil;

@Controller
public class JiabanController extends BaseController {
	/**
	 * 加班申请列表
	 */
	@RequestMapping(value="/jiabanApply", method=RequestMethod.GET)
	public String jiabanApply(HttpServletRequest request, Model model) {
		infoSetting("jiabanApply", model);
		
		String filter = request.getParameter("filter");
		int empId = Integer.parseInt(request.getSession().getAttribute("empId").toString());
		List<Jiaban> list = this.jiabanService.getListByEmpId(empId, filter);
		model.addAttribute("jiabanList", list);
		
		if(filter == null) filter = "all-apply";
		model.addAttribute("filter", filter);
		
		return "jiabanApply";
	}
	
	/**
	 * 新增加班申请
	 */
	@RequestMapping(value="/jiabanApply_add", method=RequestMethod.GET)
	public String addJiabanApply(Model model) {
		infoSetting("jiabanApply", model);
		
		return "jiabanApply_add";
	}
	
	/**
	 * 新增加班申请提交
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/jiabanApply_add", method=RequestMethod.POST)
	public String addJiabanApply(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		
		Jiaban jiaban = new Jiaban();
		jiaban.setStartTime(request.getParameter("start_time"));
		jiaban.setEndTime(request.getParameter("end_time"));
		jiaban.setDayNum(Integer.parseInt(request.getParameter("day_num")));
		jiaban.setHourNum(Integer.parseInt(request.getParameter("hour_num")));
		jiaban.setJbReason(request.getParameter("reason"));
		jiaban.setEmployee(this.employeeService.get(Integer.parseInt(session.getAttribute("empId").toString())));
		jiaban.setEmpName(session.getAttribute("empName").toString());
		jiaban.setDeptName(session.getAttribute("deptName").toString());
		jiaban.setSubmitTime(DateUtil.getCurrentTime());
		
		this.jiabanService.add(jiaban);
		
		return "redirect:/jiabanApply";
	}
	
	
	/**
	 * 查看加班申请
	 */
	@RequestMapping(value="/jiabanApply/{id}", method=RequestMethod.GET)
	public String viewJiabanApply(@PathVariable int id, Model model) {
		infoSetting("jiabanApply", model);
		
		Jiaban jb = this.jiabanService.get(id);
		model.addAttribute("jb", jb);
		
		return "jiabanApply_show";
	}
	
	
	/**
	 * 加班申请修改
	 */
	@RequestMapping(value="/jiabanApply_edit/{id}", method=RequestMethod.GET)
	public String editJiabanApply(@PathVariable int id, Model model) {
		infoSetting("jiabanApply", model);
		
		Jiaban jb = this.jiabanService.get(id);
		model.addAttribute("jb", jb);
		
		return "jiabanApply_edit";
	}
	
	/**
	 * 加班申请修改提交
	 */
	@RequestMapping("/jiabanApply_edit_submit")
	public String editJiabanApplySubmit(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		
		Jiaban jb = this.jiabanService.get(id);
		jb.setStartTime(request.getParameter("start_time"));
		jb.setEndTime(request.getParameter("end_time"));
		jb.setDayNum(Integer.parseInt(request.getParameter("day_num")));
		jb.setHourNum(Integer.parseInt(request.getParameter("hour_num")));
		jb.setJbReason(request.getParameter("reason"));
		
		this.jiabanService.update(jb);
		
		return "redirect:/jiabanApply";
	}
	
	
	/**
	 * 加班申请删除
	 */
	@RequestMapping(value="/jiabanApply_del/{id}", method=RequestMethod.GET)
	public @ResponseBody boolean delJiabanApply(@PathVariable int id, Model model) {
		this.jiabanService.delete(id);
		
		return true;
	}
}
