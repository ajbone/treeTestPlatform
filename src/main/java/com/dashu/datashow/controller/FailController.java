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
public class FailController {

  @RequestMapping(value = "/pages/fail.jspa", method = RequestMethod.GET)
  public ModelAndView showBugPerProject(@RequestParam(value = "platform",required = false)  String platform,
                                        @RequestParam(value = "projectname",required = false)  String projectname,
                                        @RequestParam(value = "svn",required = false)  String svn,
                                        @RequestParam(value = "packagetype",required = false)  String packagetype,
                                        @RequestParam(value = "alias",required = false)  String alias,
                                        @RequestParam(value = "aliasExist",required = false)  String aliasExist,
                                        @RequestParam(value = "gitbranches",required = false)  String gitbranches,
                                        @RequestParam(value = "mobilePlatform",required = false)  String mobilePlatform,
                                        @RequestParam(value = "model",required = false)  String model,
                                        @RequestParam(value = "user",required = false)  String user,
                                        @RequestParam(value = "os",required = false)  String os,
                                        @RequestParam(value = "NO",required = false)  String NO,
                                        @RequestParam(value = "modify",required = false)  String modify,
                                        @RequestParam(value = "NOexist",required = false)  String NOexist,
                                        @RequestParam(value = "projectName",required = false)  String projectName,
                                        @RequestParam(value = "password",required = false)  String password,
                                        @RequestParam(value = "fromAddress",required = false)  String fromAddress,
                                        @RequestParam(value = "toAddress",required = false)  String toAddress,
                                        @RequestParam(value = "ccList",required = false)  String ccList,
                                        @RequestParam(value = "report",required = false)  String report,
                                        @RequestParam(value = "username",required = false)  String username,
                                        @RequestParam(value = "pwd",required = false)  String pwd,
                                        @RequestParam(value = "newPassword",required = false)  String newPassword,
                                        @RequestParam(value = "oldPassword",required = false)  String oldPassword,
                                        @RequestParam(value = "editphone",required = false)  String editphone,
                                        @RequestParam(value = "projectExist",required = false)  String projectExist,
                                        @RequestParam(value = "permission",required = false)  String permission,
                                        @RequestParam(value = "fillFirst",required = false)  String fillFirst,
                                        @RequestParam(value = "pageNotFound",required = false)  String pageNotFound,
                                        @RequestParam(value = "taskName",required = false)  String taskName,
                                        @RequestParam(value = "addressInput",required = false)  String addressInput,
                                        @RequestParam(value = "taskAlias",required = false)  String taskAlias,
                                        @RequestParam(value = "action",required = false)  String action,
                                        @RequestParam(value = "git",required = false)  String git,
                                        @RequestParam(value = "git_branches",required = false)  String git_branches,
                                        @RequestParam(value = "taskAliasExist",required = false)  String taskAliasExist,
                                        @RequestParam(value = "timeout",required = false)  String timeout,
                                        @RequestParam(value = "doChannelPackage",required = false)  String doChannelPackage,
                                        @RequestParam(value = "style",required = false)  String style,
                                        HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/fail");

    if(NOexist==null|| NOexist.isEmpty() || NOexist==""){
      NOexist="-1";
    }
    if(aliasExist==null|| aliasExist.isEmpty() || aliasExist==""){
      aliasExist="-1";
    }
    if(taskAliasExist==null|| taskAliasExist.isEmpty() || taskAliasExist==""){
      taskAliasExist="-1";
    }

    mv.addObject("platform", platform);
    mv.addObject("projectname", projectname);
    mv.addObject("svn", svn);
    mv.addObject("packagetype", packagetype);
    mv.addObject("alias", alias);
    mv.addObject("gitbranches", gitbranches);
    mv.addObject("aliasExist", Integer.parseInt(aliasExist));

    mv.addObject("mobilePlatform", mobilePlatform);
    mv.addObject("model", model);
    mv.addObject("user", user);
    mv.addObject("os", os);
    mv.addObject("NO", NO);
    mv.addObject("modify", modify);
    mv.addObject("NOexist", Integer.parseInt(NOexist));

    mv.addObject("projectName", projectName);
    mv.addObject("password", password);
    mv.addObject("fromAddress", fromAddress);
    mv.addObject("toAddress", toAddress);
    mv.addObject("ccList", ccList);
    mv.addObject("report", report);

    mv.addObject("username", username);
    mv.addObject("pwd", pwd);

    mv.addObject("newPassword", newPassword);
    mv.addObject("oldPassword", oldPassword);

    mv.addObject("editphone", editphone);

    mv.addObject("projectExist", projectExist);

    mv.addObject("permission", permission);

    mv.addObject("fillFirst", fillFirst);

    mv.addObject("pageNotFound", pageNotFound);

    mv.addObject("taskName", taskName);
    mv.addObject("addressInput", addressInput);
    mv.addObject("taskAlias", taskAlias);
    mv.addObject("action", action);
    mv.addObject("git", git);
    mv.addObject("git_branches", git_branches);
    mv.addObject("taskAliasExist", taskAliasExist);
    mv.addObject("timeout", timeout);
    mv.addObject("doChannelPackage", doChannelPackage);

    mv.addObject("style", style);
    //取项目名
    packageData doPackage =new packageData();
    LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
    projectlist = doPackage.projectlist();
    mv.addObject("projectlist", projectlist);
    mv.addObject("redirect_url", request.getRequestURL());

    return mv;
  }
}
