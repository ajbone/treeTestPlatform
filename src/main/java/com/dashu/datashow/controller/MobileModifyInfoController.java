package com.dashu.datashow.controller;

import com.alibaba.fastjson.JSON;
import com.dashu.datashow.service.PackageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by shenzhaohua on 16/8/10.
 */

@Controller
public class MobileModifyInfoController {

  @Autowired
  private PackageListService packageListService;

  @RequestMapping(value = "/machineinfolist")
  @ResponseBody
  public String showlist(@RequestParam(value = "mid",required = false)  String mid) throws SQLException,IOException {
    return JSON.toJSONString(packageListService.getlist("select modifytime,info from machine_modify_info mmi LEFT JOIN machine ma ON ma.id = mmi.mid where mmi.mid =" + mid + " order by modifytime DESC"));
  }

  @RequestMapping(value = "/pages/mobileModifyInfo.jspa", method = RequestMethod.GET)
  public ModelAndView showMachineList(@RequestParam(value = "mid" ,required = false)   String mid,
                                             HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/mobileModifyInfo");


    packageData doPackage =new packageData();
    HashMap<String,String> machineInfo = new HashMap<String,String>();
    String machineModify ="";
    if(mid !=null) {
      machineModify = doPackage.selectMachineInfo("select platform,model,nub,user,modify,id,os,owner from machine where id =" + mid + "");
      //取值
      String [] machineModifySplit = machineModify.split("\\|");
      mv.addObject("platform", machineModifySplit[0]);
      mv.addObject("model", machineModifySplit[1]);
      mv.addObject("nub", machineModifySplit[2]);
      mv.addObject("user", machineModifySplit[3]);
      mv.addObject("id", machineModifySplit[5]);
      mv.addObject("os", machineModifySplit[6]);
//      mv.addObject("machineInfo", machineInfo);
    }

    //取项目名
    LinkedHashMap<String,List> projectlist = new LinkedHashMap<String,List>();
    projectlist = doPackage.projectlist();
    mv.addObject("projectlist", projectlist);
    mv.addObject("redirect_url", request.getRequestURL());
    return mv;
  }



}
