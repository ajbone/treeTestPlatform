package com.dashu.datashow.controller;

import com.alibaba.fastjson.JSON;
import com.dashu.datashow.service.PackageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by shenzhaohua on 16/8/10.
 */

@Controller
public class EmailListController {

  @Autowired
  private PackageListService packageListService;

  @RequestMapping(value = "/emaillist")
  @ResponseBody
  public String showlist() throws SQLException,IOException {
    return JSON.toJSONString(packageListService.getlist("select * from emaillist  order by id DESC"));
  }
  @RequestMapping(value = "/pages/reportList.jspa")
  public ModelAndView showPackageListProject(HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/reportList");


    packageData doPackage =new packageData();
    //取得总记录数
    int totalPageSize = doPackage.selectPackage("select count(*) from emaillist");
    mv.addObject("totalPageSize",totalPageSize);
//    mv.addObject("jobExist",jobExist);

    //取项目名
    LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
    projectlist = doPackage.projectlist();
    mv.addObject("projectlist", projectlist);
    mv.addObject("redirect_url", request.getRequestURL());

    return mv;
  }



}
