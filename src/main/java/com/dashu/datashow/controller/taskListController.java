package com.dashu.datashow.controller;

import com.dashu.datashow.service.PackageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by shenzhaohua on 16/8/10.
 */

@Controller
public class taskListController {

  @Autowired
  private PackageListService packageListService;

  @RequestMapping(value = "/pages/taskList.jspa")
  public ModelAndView showPackageListProject(HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/taskList");

    packageData doPackage =new packageData();

    //取项目名
    LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
    projectlist = doPackage.projectlist();
    mv.addObject("projectlist", projectlist);
    mv.addObject("redirect_url", request.getRequestURL());

    //取任务列表数据
    LinkedHashMap<String,List> tasklist = new LinkedHashMap<String,List>();
    tasklist = doPackage.selectList("select taskId,createtime,creater,taskName,taskAlias,git,gitbranches from task_list order by taskId DESC",7);
    mv.addObject("tasklist", tasklist);

    return mv;
  }



}
