package com.dashu.datashow.controller;

import com.alibaba.fastjson.JSON;
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
import java.util.*;

/**
 * Created by shenzhaohua on 16/5/26.
 */

@Controller
public class UploadPackageController {

  String Sql ="";

  @RequestMapping(value = "/pages/uploadPackage.jspa")
  public ModelAndView  UploadPackage(@RequestParam(value = "uploadPackage" ,required = false)  boolean uploadPackage,
                                   @RequestParam(value = "pid" ,required = false)  String pid,
                                   @RequestParam(value = "channelArraySelect" ,required = false)  String channelArraySelect,
                                   HttpServletRequest request) throws Exception {

      ModelAndView mv =new ModelAndView();
      String Date = TimeUtil.getCurrentTime();
      if(channelArraySelect ==null) {
          mv = new ModelAndView("/pages/fail");
          return mv;
      }else{
          mv = new ModelAndView("/pages/uploadPackage");
          packageData doPackage = new packageData();
          List<Object> channelList = new ArrayList<Object>();

          //取项目名
          LinkedHashMap<String, List> projectlist = new LinkedHashMap<String, List>();
          projectlist = doPackage.projectlist();
          mv.addObject("projectlist", projectlist);
          //输出哪个项目
          String projectName = doPackage.selectStatus("select projectname from project where id =" + pid);
          mv.addObject("projectName", projectName);

          //输出选择的渠道
          Sql ="select listName from channelList where id ="+channelArraySelect;
          String channelListName = doPackage.selectStatus(Sql);
          mv.addObject("channelListName", channelListName);
          //取缓存里的uploadResult
          Object uploadResult = request.getSession().getAttribute("uploadResult");
          mv.addObject("uploadResult", uploadResult);
          mv.addObject("date", Date);

      }
      return mv;
  }



}
