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
public class ReportDetailController {

     @Autowired
     private BugProjectService bugProjectService;

  @RequestMapping(value = "/pages/reportDetail.jspa", method = RequestMethod.GET)
  public ModelAndView  EditMachine(@RequestParam(value = "eid" ,required = false)  int eid,
                                        HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/reportDetail");


      packageData doPackage =new packageData();
      String reportInfo ="";
      int eidPrevious =0;
      int eidNext =0;

      //取项目名
      LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
      projectlist = doPackage.projectlist();
      mv.addObject("projectlist", projectlist);
      mv.addObject("redirect_url", request.getRequestURL());

      int maxpage =doPackage.selectPackage("select max(id) from emaillist");
      int minpage =doPackage.selectPackage("select min(id) from emaillist");
      if(eid < maxpage){
          eidNext =eid+1;
      }else{
          eidNext =maxpage;
      }
      if(eid > minpage){
          eidPrevious =eid-1;
      }else{
          eidPrevious=minpage;
      }

      if(eid !=0) {
          reportInfo = doPackage.selectMachineInfo("select project,toaddress,cc,quality,title,description,buglist,id from emaillist where id =" + eid + "");
          //取值
          String [] reportInfoSplit = reportInfo.split("\\|");
          mv.addObject("project", reportInfoSplit[0]);
          mv.addObject("toaddress", reportInfoSplit[1]);
          mv.addObject("cc", reportInfoSplit[2]);
          mv.addObject("quality", reportInfoSplit[3]);
          mv.addObject("title", reportInfoSplit[4]);
          mv.addObject("description", reportInfoSplit[5]);
          mv.addObject("buglist", reportInfoSplit[6]);
          mv.addObject("eidPrevious", eidPrevious);
          mv.addObject("eidNext", eidNext);
      }else{
          mv.addObject("addnew", "addnew");
      }

      return mv;
  }


}
