package com.dashu.datashow.controller;

import com.alibaba.fastjson.JSON;
import com.dashu.datashow.service.BugProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenzhaohua on 16/5/26.
 */

@Controller
public class AddChannelController {


  @RequestMapping(value = "/pages/addChannel.jspa", method = RequestMethod.GET)
  public ModelAndView  EditMachine(@RequestParam(value = "edit" ,required = false)  String edit,
                                        HttpServletRequest request) throws Exception {

      Object userNick = request.getSession().getAttribute("userNick");
      ModelAndView mv =new ModelAndView();
      if(userNick !=null) {
          mv = new ModelAndView("/pages/addChannel");

      packageData doPackage =new packageData();
      String projectInfo ="";

          //取项目名
          LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
          projectlist = doPackage.projectlist();
          mv.addObject("projectlist", projectlist);


      if(edit !=null) {
          projectInfo = doPackage.selectResult("select id,createtime,creater,channelCode,channel from channel where id =" + edit + "",5);
          //取值
          String [] channelInfoSplit = projectInfo.split("\\|");
          mv.addObject("id", channelInfoSplit[0]);
          mv.addObject("createtime", channelInfoSplit[1]);
          mv.addObject("creater", channelInfoSplit[2]);
          mv.addObject("channelCode", channelInfoSplit[3]);
          mv.addObject("channel", channelInfoSplit[4]);
      }
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
