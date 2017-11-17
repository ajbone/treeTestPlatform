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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenzhaohua on 16/5/26.
 */

@Controller
public class AddChannelListController {


  @RequestMapping(value = "/pages/addChannelList.jspa", method = RequestMethod.GET)
  public ModelAndView  EditMachine(@RequestParam(value = "edit" ,required = false)  String edit,
                                   @RequestParam(value = "dateAfter" ,required = false)  String dateAfter,
                                   @RequestParam(value = "dateBegin" ,required = false)  String dateBegin,
                                        HttpServletRequest request) throws Exception {

      Object userNick = request.getSession().getAttribute("userNick");
      ModelAndView mv =new ModelAndView();
      if(userNick !=null) {
          mv = new ModelAndView("/pages/addChannelList");
      packageData doPackage =new packageData();
      String projectInfo ="";
      List<Object> channelList = new ArrayList<Object>();

      //取项目名
      LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
      projectlist = doPackage.projectlist();
      mv.addObject("projectlist", projectlist);

      if(dateAfter==null ||dateAfter.isEmpty()||dateAfter==""){
          dateAfter = TimeUtil.getCurrentTime();
      }else{
          dateAfter = dateAfter.replace("T"," ");
      }

      if(dateBegin==null ||dateBegin.isEmpty()||dateBegin==""){
          dateBegin = "2007-11-11 11:11:11";
      }else{
          dateBegin = dateBegin.replace("T"," ");
      }

      //取所有渠道号
          LinkedHashMap<String,String> channelTotal =doPackage.selectMapList("select channelCode,channel from channel where createtime <='"+dateAfter+"' and createtime >='"+dateBegin+"' order by id desc");

      if(edit !=null) {
          projectInfo = doPackage.selectResult("select id,createtime,creater,listName,content,appId from channelList where id =" + edit + "",6);
          //取值
          String [] channelInfoSplit = projectInfo.split("\\|");
          mv.addObject("id", channelInfoSplit[0]);
          mv.addObject("createtime", channelInfoSplit[1]);
          mv.addObject("creater", channelInfoSplit[2]);
          mv.addObject("listName", channelInfoSplit[3]);
          mv.addObject("appId", channelInfoSplit[5]);
          Map mapTypes = JSON.parseObject(channelInfoSplit[4]);
          for (Object key : mapTypes.keySet()) {
              channelList.add(key);
          }

          mv.addObject("channelList", channelList);
          mv.addObject("channelTotal", channelTotal);

          }else{
          mv.addObject("channelTotal", channelTotal);
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
