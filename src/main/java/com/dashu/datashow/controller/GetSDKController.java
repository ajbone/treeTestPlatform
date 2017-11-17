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
import java.util.*;

/**
 * Created by shenzhaohua on 17/6/8.
 */

@Controller
public class GetSDKController {

  String Sql = "";
  packageData doPackage =new packageData();
  Properties parameters = doPackage.getParameterProperties();
  String JENKINS_ADDRESS=parameters.getProperty("JENKINS_ADDRESS");
  String WEB_ROOT=parameters.getProperty("WEB_ROOT");
  String JENKINS_ROOT =parameters.getProperty("JENKINS_ROOT");
  String JENKINS_WORKSPACE = parameters.getProperty("JENKINS_WORKSPACE");

  @RequestMapping(value = "/pages/getSDK.jspa")
  public ModelAndView showPackageListProject(@RequestParam(value = "Packaging", required = false) boolean Packaging,
                                             @RequestParam(value = "sid", required = false) String sid,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/getSDK");

    Object userNick = request.getSession().getAttribute("userNick");
    Object userEmail = request.getSession().getAttribute("email");
    Object group = request.getSession().getAttribute("groupname");

    String androidGit = "ssh://git@192.168.5.24:10022/saas/frontend/sdk-andriod.git";
    String androidGitTwo = "git@192.168.5.252:android/TFLibraryContainer.git";
    String iOSGit = "git@192.168.5.252:ios/ServicePlugin.git";
    String androidGitBranches = "master";
    String iOSGitBranches = "master";
//    String h5GitBranches = "master";
    String androidGitTwoBranches ="master";
    Map<String, String> versionMap = new HashMap<String, String>();
    String Str = "";
    String Str02 = "";

    int count = 0;
    String buildSuccesSql = "";
    String Status = "";
    String buildFailSql = "";
    String keyword = "";
    String[] aarNameSplit = new String[]{};
    int taskNumber = 0;

    String gitLog = "";
    String propertiesPath ="";
    String sdk_version ="";
    String sdkVersion ="";

    //重置输出状态
    if (sid !=null) {
      //判断有没有登录
      if (userEmail == null || userEmail == "") {
        response.sendRedirect("/pages/login.jspa?redirect_url=" + request.getRequestURL());
        return mv;
      }else if (!group.toString().contains("sdk")){
        response.sendRedirect("/pages/fail.jspa?permission=no");
        return mv;
      }
      Sql ="select isPost from DataSdk where  id =" + sid + "";
      String outputStatus = doPackage.selectStatus(Sql);
      if(outputStatus.equals("已输出")) {
        Sql = "update DataSdk set isPost ='未输出' where id =" + sid + "";
      }else if(outputStatus.equals("未输出")) {
        Sql = "update DataSdk set isPost ='已输出' where id =" + sid + "";
      }
      doPackage.executePackage(Sql);
      response.sendRedirect("/pages/getSDK.jspa");
    }

//    String[] projectName = {"sdk_android", "sdk-ios", "sdk-h5"};
    String[] projectName = {"sdk_android","TFLibraryContainer","TreefinanceService"};


    for (String project : projectName) {

      //mac
      String APK_DIRECTORY = WEB_ROOT + "app_" + project + "/";
      String LOG_DIRECTORY = WEB_ROOT + "log_" + project + "/";
      String JENKINS_DIRECTORY = JENKINS_ROOT+"app_" + project + "/builds/";
      String BUILD_NUMBER = JENKINS_ADDRESS + "job/app_" + project + "/lastBuild/buildNumber --user shenzh:shenzh";

      //取得总记录数
      String curl = "curl  " + BUILD_NUMBER + "";
      int buildNumber = doPackage.getBuildNumber(curl) + 1;
//      String credentials = "641268f1-09ae-4dd4-ab59-a6ad05f2f605";
//      String credentials = "2b22e99e-1531-453c-b179-21d71cbdae67";
//      String project = doPackage.getProjectName(androidGit);

      //定义包名关键字
      if(project =="TreefinanceService" ) {
        keyword = "TreefinanceService";
      }else{
        keyword = "aar";
      }

      if (buildNumber <= 20) {
        taskNumber = buildNumber - 1;
      }else{
        taskNumber =20;
      }

      for (int i = buildNumber - taskNumber; i <= buildNumber; i++) {
        String apkPath = APK_DIRECTORY + i + "/aar";
        Sql = "select count(*) from DataSdk where branches ='" + project + "' and job =" + i + "";
        Status = "select status from DataSdk where branches ='" + project + "' and job =" + i + "";
        count = doPackage.selectPackage(Sql);
        String curl_build_status = "curl " + JENKINS_ADDRESS + "job/app_" + project + "/" + i + "/api/json?tree=result --user shenzh:shenzh";

        if (count != 0 && doPackage.selectStatus(Status).equals("正在打包")) {
          String copylog = "cp " + JENKINS_DIRECTORY + i + "/log  " + LOG_DIRECTORY + i;
          Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", copylog});
          //gitlog
          String gitlogaddress = "/app_" + project + "/" + i + "/gitlog.txt";
          String filePath = APK_DIRECTORY + i + "/gitlog.txt";

          //定义ios sdk包路径
          String sourcePath =APK_DIRECTORY + i + "/framework/";
          String zipPath =APK_DIRECTORY + i + "/aar/iOS_TreefinanceService.zip";

          if (project == "sdk_android"){
            propertiesPath =JENKINS_WORKSPACE + "/app_" + project+"/treefinanceservice/src/main/assets/gfdserviceconfig.properties";
            sdk_version ="sdk_version";
          }else if (project == "TFLibraryContainer"){
            propertiesPath = JENKINS_WORKSPACE + "/app_" + project+"/treefinancetools/gradle.properties";
            sdk_version ="LIBRARY_VERSION";
          }else if (project == "TreefinanceService"){
            propertiesPath = JENKINS_WORKSPACE + "/app_" + project+"/ServicePlugin/TreefinanceService.m";
            Str ="return @\"";
            Str02 ="\";";
          }
          //判断job的build状态,1为打包完成,2为打包失败,3为打包中断
          if (doPackage.getBuildStatus(curl_build_status) == 1) {
            String aarlink = "";
            //如果是ios项目,压缩成zip
            if(project =="TreefinanceService" ) {
              doPackage.createZip(sourcePath, zipPath);
            }
            //取aar名字
            String aarName = doPackage.getApkName(apkPath, keyword);

//            if (projectInfoSplit[0].equals("Android")) {
            if (aarName != "") {
              aarNameSplit = aarName.split(",");
              for (int j = 0; j < aarNameSplit.length; j++) {
                aarlink = aarlink + "," + "/app_" + project + "/" + i + "/aar/" + aarNameSplit[j];
              }
              aarlink = aarlink.substring(1);
            }

            //获取svnlog内容
            gitLog = doPackage.readFileContent(filePath).replace("\'", "").replace("\"", "");

            //获取version
            if(project == "TreefinanceService") {
              sdkVersion = doPackage.getIosVersion(propertiesPath,Str,Str02);
            }else {
              versionMap = doPackage.readProperties(propertiesPath, sdk_version);
              sdkVersion = versionMap.get("version");
            }

            buildSuccesSql = "update DataSdk set status ='打包成功',package ='" + aarName + "',aarlink ='" + aarlink + "',version ='"+sdkVersion+"',gitlog ='" + gitLog + "',gitlogaddress ='" + gitlogaddress + "' where branches ='" + project + "' and job =" + i + "";
            doPackage.executePackage(buildSuccesSql);
          }
          if (doPackage.getBuildStatus(curl_build_status) == 2) {
            //获取svnlog内容
            if (doPackage.readFileContent(filePath) != "") {
              gitLog = doPackage.readFileContent(filePath).replace("\'", "").replace("\"", "");
            }

            buildFailSql = "update DataSdk set status ='打包失败',package ='',gitlog ='" + gitLog + "',gitlogaddress ='" + gitlogaddress + "' where branches ='" + project + "' and job =" + i + "";
            doPackage.executePackage(buildFailSql);

          }
          if (doPackage.getBuildStatus(curl_build_status) == 3) {
            buildFailSql = "update DataSdk set status ='打包中断',package ='' where branches ='" + project + "' and job =" + i + "";
            doPackage.executePackage(buildFailSql);

          }

        }

      }
    }

    String Date = TimeUtil.getCurrentTime();
    String groupName = Date.replace("-", "").replace(" ", "").replace(":", "").substring(3);
    if (Packaging) {
      //判断有没有登录
      if (userEmail == null || userEmail == "") {
        response.sendRedirect("/pages/login.jspa?redirect_url=" + request.getRequestURL());
        return mv;
      }

      // 执行修改项目操作

      for (String DataProject : projectName) {
        String APK_DIRECTORY = WEB_ROOT + "app_" + DataProject + "/";
        String BUILD_NUMBER = JENKINS_ADDRESS + "job/app_" + DataProject + "/lastBuild/buildNumber --user shenzh:shenzh";
        String BUILD_JOB = JENKINS_ADDRESS + "job/app_" + DataProject + "/build?token=12345 --user shenzh:shenzh";
        String curl = "curl  " + BUILD_NUMBER + "";
        int buildNumber = doPackage.getBuildNumber(curl) + 1;
        //创建目录
        doPackage.createDirectory(APK_DIRECTORY + buildNumber);
        doPackage.createDirectory(APK_DIRECTORY + buildNumber + "/aar");
        doPackage.createDirectory(APK_DIRECTORY + buildNumber + "/framework");
        //执行job
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "curl -X GET " + BUILD_JOB + ""});
        String logadreass = "/log_" + DataProject + "/" + buildNumber;
        Sql = "insert into DataSdk(createtime,creater,groupName,status,package,aarlink,version,gitlog,gitlogaddress,jlog,isPost,branches,job) values('" + Date + "','" + userNick + "','" + groupName + "','正在打包','<div  onclick=\"location.reload()\"><i class=\"fa fa-refresh fa-spin\"></i></div>','','','','','" + logadreass + "','未输出','" + DataProject + "','" + buildNumber + "')";
        doPackage.executePackage(Sql);
      }
    }

    //取最新构建批次和版本号
    LinkedHashMap<String,List> sdkList = new LinkedHashMap<String,List>();
    String getNewBatchSql ="select branches,max(version)from DataSdk where isPost ='已输出' group by branches";
    String getNewGroupSql ="select groupName from DataSdk  where createtime =(select max(createtime) from DataSdk where isPost ='已输出')";
    sdkList = doPackage.selectList(getNewBatchSql,2);
    String getGroupName = doPackage.selectStatus(getNewGroupSql);
    mv.addObject("sdkList", sdkList);
    mv.addObject("newGroupName", getGroupName);


      mv.addObject("androidGit", androidGit);
      mv.addObject("androidGitTwo", androidGitTwo);
      mv.addObject("iOSGit", iOSGit);
//      mv.addObject("h5Git", "h5Git");
      mv.addObject("androidGitBranches", androidGitBranches);
      mv.addObject("androidGitTwoBranches", androidGitTwoBranches);
      mv.addObject("iOSGitBranches", iOSGitBranches);
//      mv.addObject("h5GitBranches", h5GitBranches);

      //取项目名
      LinkedHashMap<String, List> projectlist = new LinkedHashMap<String, List>();
      projectlist = doPackage.projectlist();
      mv.addObject("projectlist", projectlist);

      //取打包列表数据
      LinkedHashMap<String, List> packagelist = new LinkedHashMap<String, List>();
      packagelist = doPackage.selectList("select id,createtime,creater,groupName,status,package,aarlink,version,gitlog,gitlogaddress,jlog,isPost,branches,job from DataSdk  order by id DESC", 14);
      mv.addObject("packagelist", packagelist);

      mv.addObject("redirect_url", request.getRequestURL());

      return mv;
    }

  }
