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
public class AddProjectController {

     @Autowired
     private BugProjectService bugProjectService;

  @RequestMapping(value = "/pages/addProject.jspa", method = RequestMethod.GET)
  public ModelAndView  EditMachine(@RequestParam(value = "edit" ,required = false)  String edit,
                                    @RequestParam(value = "redirect_url" ,required = false)  String redirect_url,
                                        HttpServletRequest request) throws Exception {

      Object userNick = request.getSession().getAttribute("userNick");
      ModelAndView mv =new ModelAndView();
      if(userNick !=null) {
          mv = new ModelAndView("/pages/addProject");

      packageData doPackage =new packageData();
      String projectInfo ="";

      //取项目名
          LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
          projectlist = doPackage.projectlist();
          mv.addObject("projectlist", projectlist);

      if(edit !=null) {
          projectInfo = doPackage.selectProjectInfo("select id,platform,svn,task,projectname,alias,gitbranches,createtime,appId from project where id =" + edit + "");
          //取值
          String [] machineInfoSplit = projectInfo.split("\\|");
          mv.addObject("id", machineInfoSplit[0]);
          mv.addObject("platform", machineInfoSplit[1]);
          mv.addObject("svn", machineInfoSplit[2]);
          mv.addObject("task", machineInfoSplit[3]);
          mv.addObject("projectname", machineInfoSplit[4]);
          mv.addObject("alias", machineInfoSplit[5]);
          mv.addObject("gitbranches", machineInfoSplit[6]);
          mv.addObject("appId", machineInfoSplit[8]);
      }else{
          mv.addObject("addnew", "addnew");
      }
      }else {
          mv = new ModelAndView("/pages/login");
      }

      if(edit !=null) {
          mv.addObject("redirect_url", redirect_url);
      }else {
          mv.addObject("redirect_url", request.getRequestURL());
      }
      return mv;
  }


}
