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
public class EditProfileController {


  @RequestMapping(value = "/pages/editProfile.jspa", method = RequestMethod.GET)
  public ModelAndView showBugPerProject(@RequestParam(value = "uid" ,required = false)  String uid,
                                        @RequestParam(value = "redirect_url" ,required = false)  String redirect_url,
                                        HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/editProfile");

    //取项目名
    packageData doPackage =new packageData();
    LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
    projectlist = doPackage.projectlist();
    mv.addObject("projectlist", projectlist);

    String userProfile ="";
    userProfile = doPackage.selectMachineInfo("select uid,username,usernick,password,discription,email,groupname,photo from user where uid ='" + uid + "'");
    //取值
    String [] userProfileSplit = userProfile.split("\\|");
    mv.addObject("discription", userProfileSplit[4]);
    mv.addObject("email", userProfileSplit[5]);
    mv.addObject("photo", userProfileSplit[7]);

    mv.addObject("redirect_url", redirect_url);
    return mv;
  }



}
