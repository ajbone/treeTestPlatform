package com.dashu.datashow.controller;

import com.dashu.datashow.service.BugProjectService;
import com.dashu.datashow.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by shenzhaohua on 16/5/26.
 */

@Controller
public class AddDayReportController {

     @Autowired
     private BugProjectService bugProjectService;

  @RequestMapping(value = "/pages/addDayReport.jspa", method = RequestMethod.GET)
  public ModelAndView AddDayReport(HttpServletRequest request) throws Exception {

    Object userNick = request.getSession().getAttribute("userNick");
    ModelAndView mv =new ModelAndView();
    if(userNick !=null) {
      mv = new ModelAndView("/pages/addDayReport");
      String startDate = TimeUtil.getAdjustTime(Calendar.DATE, -30).replace(" ","T");
      //项目列表
      HashMap<String, String>  optionProject=bugProjectService.selectBarPicture();
      HashMap<String, String>  EmailList=bugProjectService.selectUserEmail();

      mv.addObject("optProject", optionProject);
      mv.addObject("EmailList", EmailList);
      mv.addObject("startDate", startDate);
      //取项目名
      packageData doPackage =new packageData();
      LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
      projectlist = doPackage.projectlist();
      mv.addObject("projectlist", projectlist);
    }else {
      mv = new ModelAndView("/pages/login");
    }
      mv.addObject("redirect_url", request.getRequestURL());
      return mv;
  }


}
