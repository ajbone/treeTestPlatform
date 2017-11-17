package com.dashu.datashow.controller;

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
public class SuccessfulController {


  @RequestMapping(value = "/pages/success.jspa", method = RequestMethod.GET)
  public ModelAndView showBugPerProject(@RequestParam(value = "redirect_url",required = false)  String redirect_url,
                                        HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/success");

    //取项目名
    packageData doPackage =new packageData();
    LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
    projectlist = doPackage.projectlist();
    mv.addObject("projectlist", projectlist);

        redirect_url=redirect_url.replace("http://localhost:8080","").replace("http://192.168.12.4:8080","");
    if(redirect_url==null|| redirect_url.isEmpty() || redirect_url==""){
      redirect_url="/pages/index.jspa";
    }
    mv.addObject("redirect_url", redirect_url);

    return mv;
  }



}
