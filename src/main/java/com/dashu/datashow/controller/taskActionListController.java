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
public class taskActionListController {

  @Autowired
  private PackageListService packageListService;

  @RequestMapping(value = "/pages/taskActionList.jspa")
  public ModelAndView showPackageListProject(HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/taskActionList");

    packageData doPackage =new packageData();

    //取项目名
    LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
    projectlist = doPackage.projectlist();
    mv.addObject("projectlist", projectlist);
    mv.addObject("redirect_url", request.getRequestURL());

    //取任务执行列表数据
    LinkedHashMap<String, List> taskActionList = new LinkedHashMap<String, List>();
    taskActionList = doPackage.selectList("select tal.actionId,tal.creater,tal.createtime,tal.status,tal.result,tal.statusNumber,tal.taskId,tl.taskName from task_action_list tal left join task_list tl on tal.taskId = tal.taskId order by tal.actionId DESC",8);
    mv.addObject("taskActionList", taskActionList);

    return mv;
  }



}
