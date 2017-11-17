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
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenzhaohua on 16/5/26.
 */

@Controller
public class AddInterfaceController {


  @RequestMapping(value = "/pages/addInterface.jspa", method = RequestMethod.GET)
  public ModelAndView  EditMachine(@RequestParam(value = "edit" ,required = false)  String edit,
                                   @RequestParam(value = "interfaceId" ,required = false)  String interfaceId,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {

      Object userNick = request.getSession().getAttribute("userNick");
      ModelAndView mv =new ModelAndView();
      if(userNick !=null) {
          mv = new ModelAndView("/pages/addInterface");
      packageData doPackage =new packageData();
      String interfaceInfo ="";
      Object interConfig = "";
      int i=0;
      if(edit !=null) {
          interfaceInfo = doPackage.selectMachineInfo("select id,interface,config,discription,creater,createtime,type,testType from interface where id =" + edit + "");
          String[] interfaceSplit = interfaceInfo.split("\\|");
          Map mapTypes = JSON.parseObject(interfaceSplit[2]);
          for (Object key : mapTypes.keySet()) {
              if (i==0){
                  interConfig = key;
              }else {
                  interConfig = interConfig + "|" +key;
              }
              i++;
          }
          mv.addObject("interfaceId", interfaceSplit[0]);
          mv.addObject("interfaceName", interfaceSplit[1]);
          mv.addObject("interfaceConfig", interConfig);
          mv.addObject("interfaceDiscription", interfaceSplit[3]);
          mv.addObject("interfaceType", interfaceSplit[6]);
          mv.addObject("testType", interfaceSplit[7]);
      }
      //取项目名
          LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
          projectlist = doPackage.projectlist();
          mv.addObject("projectlist", projectlist);

      }else {
          mv = new ModelAndView("/pages/login");
      }

          mv.addObject("redirect_url", "/pages/interfaceList.jspa");
      return mv;
  }


}
