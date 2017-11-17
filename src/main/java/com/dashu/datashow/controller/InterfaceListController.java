package com.dashu.datashow.controller;

import com.dashu.datashow.service.PackageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by shenzhaohua on 16/8/10.
 */

@Controller
public class InterfaceListController {

  @Autowired
  private PackageListService packageListService;

  @RequestMapping(value = "/pages/interfaceList.jspa")
  public ModelAndView showPackageListProject(HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/interfaceList");

    packageData doPackage =new packageData();

    //取项目名
    LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
    projectlist = doPackage.projectlist();
    mv.addObject("projectlist", projectlist);
    mv.addObject("redirect_url", request.getRequestURL());

    //取接口列表数据
    LinkedHashMap<String,List> interfacelist = new LinkedHashMap<String,List>();
    interfacelist = doPackage.selectList("select id,createtime,creater,type,interface,discription,testType from interface order by id DESC",7);
    mv.addObject("interfacelist", interfacelist);

    return mv;
  }



}
