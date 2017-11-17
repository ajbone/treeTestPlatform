package com.dashu.datashow.controller;

import com.dashu.datashow.service.BugProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by shenzhaohua on 16/5/26.
 */

@Controller
public class AddTaskController {

     @Autowired
     private BugProjectService bugProjectService;

  @RequestMapping(value = "/pages/addTask.jspa", method = RequestMethod.GET)
  public ModelAndView  EditMachine(@RequestParam(value = "edit" ,required = false)  String edit,
                                        HttpServletRequest request) throws Exception {

      Object userNick = request.getSession().getAttribute("userNick");
      ModelAndView mv =new ModelAndView();
      if(userNick !=null) {
          mv = new ModelAndView("/pages/addTask");

      packageData doPackage =new packageData();
      String projectInfo ="";

      //取项目名
          LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
          projectlist = doPackage.projectlist();
          mv.addObject("projectlist", projectlist);

      if(edit !=null) {
          projectInfo = doPackage.selectResult("select taskId,taskName,taskAlias,git,gitbranches,action,addressInput from task_list where taskId =" + edit + "",7);
          //取值
          String [] machineInfoSplit = projectInfo.split("\\|");
          mv.addObject("taskId", machineInfoSplit[0]);
          mv.addObject("taskName", machineInfoSplit[1]);
          mv.addObject("taskAlias", machineInfoSplit[2]);
          mv.addObject("git", machineInfoSplit[3]);
          mv.addObject("gitbranches", machineInfoSplit[4]);
          mv.addObject("action", machineInfoSplit[5]);
          mv.addObject("addressInput", machineInfoSplit[6]);
      }else{
          mv.addObject("addnew", "addnew");
      }
      }else {
          mv = new ModelAndView("/pages/login");
      }

      if(edit !=null) {
          mv.addObject("redirect_url", request.getRequestURL()+ "?edit=" + edit);
      }else {
          mv.addObject("redirect_url", request.getRequestURL());
      }
      return mv;
  }


}
