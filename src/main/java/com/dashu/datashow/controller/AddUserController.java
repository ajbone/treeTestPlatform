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
public class AddUserController {

     @Autowired
     private BugProjectService bugProjectService;

  @RequestMapping(value = "/pages/addUser.jspa", method = RequestMethod.GET)
  public ModelAndView  EditMachine(@RequestParam(value = "eid" ,required = false)  String eid,
                                    @RequestParam(value = "redirect_url" ,required = false)  String redirect_url,
                                        HttpServletRequest request) throws Exception {

      Object userNick = request.getSession().getAttribute("userNick");
      ModelAndView mv =new ModelAndView();
      if(userNick !=null) {
          mv = new ModelAndView("/pages/addUser");

      packageData doPackage =new packageData();
      String projectInfo ="";

      //取项目名
          LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
          projectlist = doPackage.projectlist();
          mv.addObject("projectlist", projectlist);

      if(eid !=null) {
          projectInfo = doPackage.selectMachineInfo("select uid,username,usernick,discription,email,groupname,photo,password from user where uid =" + eid + "");
          //取值
          String [] machineInfoSplit = projectInfo.split("\\|");
          mv.addObject("userid", machineInfoSplit[0]);
          mv.addObject("username", machineInfoSplit[1]);
          mv.addObject("fill_usernick", machineInfoSplit[2]);
          mv.addObject("userDiscription", machineInfoSplit[3]);
          mv.addObject("emailAddress", machineInfoSplit[4]);
          mv.addObject("groupname", machineInfoSplit[5]);
          mv.addObject("headSculpture", machineInfoSplit[6]);
          mv.addObject("fill_password", machineInfoSplit[7]);
      }else{
          mv.addObject("addnew", "addnew");
      }
      }else {
          mv = new ModelAndView("/pages/login");
      }

//      if(edit !=null) {
//          mv.addObject("redirect_url", redirect_url + "?pid=" + edit);
//      }else {
          mv.addObject("redirect_url", request.getRequestURL()+"?"+request.getQueryString());
//      }
      return mv;
  }


}
