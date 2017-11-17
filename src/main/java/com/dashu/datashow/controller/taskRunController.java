package com.dashu.datashow.controller;

import com.alibaba.fastjson.JSON;
import com.dashu.datashow.service.PackageListService;
import com.dashu.datashow.service.ZXingCode;
import com.dashu.datashow.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by shenzhaohua on 16/8/10.
 */

@Controller
public class taskRunController {

  String buildStatusSql ="";
  packageData doPackage =new packageData();
  Properties parameters = doPackage.getParameterProperties();
  String JENKINS_ADDRESS=parameters.getProperty("JENKINS_ADDRESS");
  String JENKINS_URL=parameters.getProperty("JENKINS_URL");

  @RequestMapping(value = "/pages/taskRun.jspa")
  public ModelAndView showPackageListProject(@RequestParam(value = "runTask" ,required = false)  boolean runTask,
                                             @RequestParam(value = "taskId",required = false)  String taskId,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/taskRun");
    packageData doPackage = new packageData();
    Object userNick = request.getSession().getAttribute("userNick");
    String Date = TimeUtil.getCurrentTime();
    int taskNumber =0;

    //取项目名
    LinkedHashMap<String, List> projectlist = new LinkedHashMap<String, List>();
    projectlist = doPackage.projectlist();
    mv.addObject("projectlist", projectlist);
    mv.addObject("redirect_url", request.getRequestURL()+"?"+request.getQueryString());

    //取任务信息
    String taskInfo = doPackage.selectResult("select taskId,taskName,taskAlias,git,gitbranches,updatetime,action from task_list where taskId =" + taskId + "",7);
    //取值
    String [] taskInfoSplit = taskInfo.split("\\|");
    mv.addObject("taskId", taskInfoSplit[0]);
    mv.addObject("taskName", taskInfoSplit[1]);
    mv.addObject("taskAlias", taskInfoSplit[2]);
    mv.addObject("git", taskInfoSplit[3]);
    mv.addObject("gitbranches", taskInfoSplit[4]);
    mv.addObject("updatetime", taskInfoSplit[5]);
    if(taskInfoSplit[6].length()<=42){
      mv.addObject("action", taskInfoSplit[6].substring(0,taskInfoSplit[6].length()));
    }else {
      mv.addObject("action", taskInfoSplit[6].substring(0, 41));
    }

    //执行job参数配置
    String BUILD_JOB = JENKINS_ADDRESS + "job/task_" + taskInfoSplit[2] + "/build?token=12345 --user shenzh:shenzh";

    //把运行结束的任务置状态
    String BUILD_NUMBER = JENKINS_ADDRESS + "job/task_" + taskInfoSplit[2] + "/lastBuild/buildNumber --user shenzh:shenzh";
    String curl = "curl  " + BUILD_NUMBER + "";
    int buildNumber = doPackage.getBuildNumber(curl) + 1;
    if(buildNumber<=20){
      taskNumber = buildNumber-1;
    }else{
      taskNumber =20;
    }
    for (int i = buildNumber - taskNumber; i <= buildNumber; i++) {
      //获取任务状态
      String Status = "select status from task_action_list where taskId ='" + taskId + "' and buildNumber =" + i + "";
      if (doPackage.selectStatus(Status).equals("正在执行")) {
        String curl_build_status = "curl " + JENKINS_ADDRESS + "job/task_" + taskInfoSplit[2] + "/" + i + "/api/json?tree=result --user shenzh:shenzh";
        int buildStatus =doPackage.getBuildStatus(curl_build_status);
        String jenkinsConsole =JENKINS_URL+"job/task_"+taskInfoSplit[2]+"/"+(buildNumber-1)+"/console";
        if (buildStatus==1){
          buildStatusSql = "update task_action_list set status ='执行成功',statusNumber ='1',result ='" + jenkinsConsole + "' where taskId ='" + taskId + "' and buildNumber =" + i + "";
          doPackage.executePackage(buildStatusSql);
        }else if (buildStatus==2){
          buildStatusSql = "update task_action_list set status ='执行失败',statusNumber ='2',result ='" + jenkinsConsole + "' where taskId ='" + taskId + "' and buildNumber =" + i + "";
          doPackage.executePackage(buildStatusSql);
        }else if (buildStatus==3){
          buildStatusSql = "update task_action_list set status ='执行中断',statusNumber ='3',result ='"+jenkinsConsole+"' where taskId ='" + taskId + "' and buildNumber =" + i + "";
          doPackage.executePackage(buildStatusSql);
        }
      }
    }

    //判断是否有执行动作
    if (runTask) {
      //执行jenkins构建命令
      Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "curl -X GET " + BUILD_JOB + ""});

      //写入数据库
      String Sql = "insert into task_action_list(taskId,creater,createtime,status,result,buildNumber,statusNumber) values(" + taskId + ",'" + userNick + "','"+Date+"','正在执行','<div  onclick=\"location.reload()\"><i class=\"fa fa-refresh fa-spin\"></i></div>','"+buildNumber+"','0')";
      doPackage.executePackage(Sql);

    }


    //取任务执行列表数据
    LinkedHashMap<String, List> taskActionList = new LinkedHashMap<String, List>();
    taskActionList = doPackage.selectList("select actionId,creater,createtime,status,result,statusNumber from task_action_list where taskId ='" + taskId + "' order by actionId DESC",6);
    mv.addObject("taskActionList", taskActionList);

    return mv;
  }

}
