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
public class MobileListController {
  String Sql ="";

  @Autowired
  private PackageListService packageListService;

  @RequestMapping(value = "/machinelist")
  @ResponseBody
  public String showlist() throws SQLException,IOException {
    return JSON.toJSONString(packageListService.getlist("select * from machine  order by id DESC"));
  }
  @RequestMapping(value = "/pages/mobilePhone.jspa")
  public ModelAndView showPackageListProject(HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/mobilePhone");


    packageData doPackage =new packageData();
    //取得总记录数
    int totalPageSize = doPackage.selectPackage("select count(*) from machine");
    mv.addObject("totalPageSize",totalPageSize);
//    mv.addObject("jobExist",jobExist);

    //取项目名
    LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
    projectlist = doPackage.projectlist();
    mv.addObject("projectlist", projectlist);
    mv.addObject("redirect_url", request.getRequestURL());

    //取手机列表数据
    LinkedHashMap<String,List> mobilelist = new LinkedHashMap<String,List>();
    mobilelist = doPackage.selectList("select id,createtime,platform,nub,model,os,user,modify from machine order by id DESC",8);
    mv.addObject("mobilelist", mobilelist);

    return mv;
  }



}
