package com.dashu.datashow.controller;

import com.dashu.datashow.service.BugProjectService;
import com.dashu.datashow.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by shenzhaohua on 16/5/26.
 */

@Controller
public class EditMobileController {

     @Autowired
     private BugProjectService bugProjectService;

  @RequestMapping(value = "/pages/editMobile.jspa", method = RequestMethod.GET)
  public ModelAndView  EditMachine(@RequestParam(value = "edit" ,required = false)  String edit,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {

      Object userNick = request.getSession().getAttribute("userNick");
      ModelAndView mv =new ModelAndView();
      if(userNick !=null) {
          mv = new ModelAndView("/pages/editMobile");
      packageData doPackage =new packageData();
      String machineInfo ="";
      if(edit !=null) {
          machineInfo = doPackage.selectMachineInfo("select id,platform,model,nub,user,modify,os,owner from machine where id =" + edit + "");
          //取值
          String [] machineInfoSplit = machineInfo.split("\\|");
          mv.addObject("id", machineInfoSplit[0]);
          mv.addObject("platform", machineInfoSplit[1]);
          mv.addObject("model", machineInfoSplit[2]);
          mv.addObject("nub", machineInfoSplit[3]);
          mv.addObject("user", machineInfoSplit[4]);
          mv.addObject("modify", machineInfoSplit[5].replace(" ","T"));
          mv.addObject("os", machineInfoSplit[6]);

          if(!userNick.equals("申兆华")) {
              if (!userNick.equals(machineInfoSplit[4])) {
                  response.sendRedirect("/pages/fail.jspa?editphone=no");
              }
          }

      }else{
          mv.addObject("addnew", "addnew");
          mv.addObject("modify", TimeUtil.getCurrentTime().replace(" ","T"));
      }

      //取项目名
          LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
          projectlist = doPackage.projectlist();
          mv.addObject("projectlist", projectlist);


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
