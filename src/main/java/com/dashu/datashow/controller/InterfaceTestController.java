package com.dashu.datashow.controller;

import com.alibaba.fastjson.JSON;
import com.dashu.datashow.service.BugProjectService;
import com.dashu.datashow.util.FormatUtil;
import com.dashu.datashow.util.SignUtil;
import com.dashu.datashow.util.TimeUtil;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Created by shenzhaohua on 16/5/26.
 */

@Controller
public class InterfaceTestController {

     @Autowired
     private BugProjectService bugProjectService;

  @RequestMapping(value = "/pages/interface.jspa")
  public ModelAndView  EditMachine(@RequestParam(value = "interfaceId" ,required = false)  String interfaceId,
                                   @RequestParam(value = "loginName" ,required = false)  String loginName,
                                   @RequestParam(value = "redirect_url" ,required = false)  String redirect_url,
                                   @RequestParam(value = "postCapthCode" ,required = false)  boolean postCapthCode,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {

      String environmentConfig = "";
      HashMap<String, String> interfaceConfig = new HashMap<String, String>();
      Object interResult = request.getSession().getAttribute("result");
      String interfaceInfo = "";
      packageData doPackage = new packageData();
      ModelAndView mv = new ModelAndView();
      //判断url带没带id
      if (interfaceId == null) {
          response.sendRedirect("/pages/fail.jspa?projectExist=noproject");
          return mv;
      } else {

          //取缓存中config的上次输入值
          Object ConfigValue = request.getSession().getAttribute("ConfigValue");

          //当页面切换到另一个接口页面时seesion删除掉
          Object sessionId = request.getSession().getAttribute("sessionId");
          if (!interfaceId.equals(sessionId)) {
              //config的配置删除掉
              request.getSession().removeAttribute("loginName");
              request.getSession().removeAttribute("loginName_seller");
              request.getSession().removeAttribute("password_seller");
              request.getSession().removeAttribute("ConfigValue");
              //结果的seesion删除掉
              request.getSession().removeAttribute("result");
          }

          interfaceInfo = doPackage.selectMachineInfo("select id,interface,config,discription,creater,createtime,type,testType from interface where id =" + interfaceId + "");
          //取值
          String[] interfaceInfoSplit = interfaceInfo.split("\\|");
          String interfaceType = interfaceInfoSplit[6];
          String testType = interfaceInfoSplit[7];

          if (interResult == null || interResult == "") {
              interResult = "empty";
          }
          String result = interResult.toString();


          if (interfaceType.equals("dev")) {
              environmentConfig = "http://test.";
          }
          if (interfaceType.equals("prod")) {
              environmentConfig = "https://www.";
          }

          if ((loginName == null || loginName == "" || loginName.isEmpty()) && request.getSession().getAttribute("loginName") != null) {
              loginName = request.getSession().getAttribute("loginName").toString();
          }


          mv = new ModelAndView("/pages/interface");

          //取项目名
          LinkedHashMap<String, List> projectlist = new LinkedHashMap<String, List>();
          projectlist = doPackage.projectlist();
          mv.addObject("projectlist", projectlist);

          //取值
          mv.addObject("interfaceId", interfaceInfoSplit[0]);
          mv.addObject("interfaceName", interfaceInfoSplit[1]);
          mv.addObject("config", interfaceInfoSplit[2]);
          mv.addObject("discription", interfaceInfoSplit[3]);
          mv.addObject("creater", interfaceInfoSplit[4]);
          mv.addObject("testType", testType);
          mv.addObject("interfaceType", interfaceType);
          //判断缓存是否有值
          if (ConfigValue == null) {
              Map mapTypes = JSON.parseObject(interfaceInfoSplit[2]);
              mv.addObject("parameter", mapTypes);
          } else {
              mv.addObject("parameter", ConfigValue);
          }

          if (postCapthCode) {
              //发送验证码
              interfaceConfig.put("mobile", loginName);
              Response CapthCodeRes = given().
                      params(interfaceConfig).
                      request().
                      expect().
                      when().
                      post(environmentConfig + "91gfd.com.cn/usercenterv2/api/sms/sendSmsCode");
              if (CapthCodeRes.statusCode() != 200) {
                  response.sendRedirect("/pages/fail.jspa?pageNotFound=" + CapthCodeRes.statusCode());
              }
              result = CapthCodeRes.jsonPath().get().toString();
              //删掉验证码过期的标记
//              request.getSession().removeAttribute("verification");
          }

          result = FormatUtil.formatJson(result);
          mv.addObject("result", result);

          mv.addObject("loginName", loginName);
          //验证码置成过期
//          if(doVerification){
//            request.getSession().setAttribute("verification", "expired");
//          }
          Object verification = request.getSession().getAttribute("verification");
          mv.addObject("verification", verification);
          System.out.println("verification="+verification);
          //删除过期缓存
          request.getSession().removeAttribute("verification");

          if (interfaceId != null) {
              mv.addObject("redirect_url", redirect_url);
          } else {
              mv.addObject("redirect_url", request.getRequestURL());
          }
          return mv;
      }
  }

}
