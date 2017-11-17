package com.dashu.datashow.controller;

import com.alibaba.fastjson.JSON;
import com.dashu.datashow.service.PackageListService;
import com.dashu.datashow.service.ZXingCode;
import com.dashu.datashow.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by shenzhaohua on 16/8/10.
 */

@Controller
public class GetPackageController {
  String Sql ="";
  packageData doPackage =new packageData();
  Properties parameters = doPackage.getParameterProperties();
  String JENKINS_ADDRESS=parameters.getProperty("JENKINS_ADDRESS");
  String LOCATION_ADDRESS=parameters.getProperty("LOCATION_ADDRESS");
  String keyword_apk ="jiagu";

  @RequestMapping(value = "/pages/getPackage.jspa")
  public ModelAndView showPackageListProject(@RequestParam(value = "historypackaging" ,required = false)  boolean historypackaging,
                                             @RequestParam(value = "pid",required = false)  String pid,
                                             @RequestParam(value = "podInstall" ,required = false)  boolean podInstall,
                                             @RequestParam(value = "markId",required = false)  String markId,
                                             @RequestParam(value = "doRelease" ,required = false)  boolean doRelease,
                                             @RequestParam(value = "channelPackage" ,required = false)  boolean channelPackage,
                                             @RequestParam(value = "channelArraySelect" ,required = false)  String channelArraySelect,
                                             @RequestParam(value = "killPackage" ,required = false)  String killPackage,
                                             @RequestParam(value = "jiagu_360" ,required = false)  boolean jiagu_360,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
    ModelAndView mv = new ModelAndView("/pages/getPackage");
    String email ="";
    String Date = TimeUtil.getCurrentTime();

    Object userNick = request.getSession().getAttribute("userNick");
    Object userEmail = request.getSession().getAttribute("email");

//    //tab切换存入缓存
//    request.getSession().setAttribute("channelActive", "testTabDisplay");


    //判断有没有登录
    if(userEmail==null || userEmail==""){
      userEmail="empty";
    }

    //强制结束渠道包打包
    if(killPackage!=null) {
      Sql = "select count(*) from channelReleaseStatus where projectId =" + pid;
      int releaseCount = doPackage.selectPackage(Sql);
      if (releaseCount != 0) {
        Sql = "update channelReleaseStatus set creater ='" + userNick + "',createtime = '" + Date + "',status = 3 where projectId =" + pid;
        doPackage.executePackage(Sql);
      }
      response.sendRedirect("/pages/getPackage.jspa?pid="+pid);
    }

    email=userEmail.toString();
    String apkKeyWord ="";
    apkKeyWord =doPackage.selectStatus("select alias from project where id ="+pid);
    String appId =doPackage.selectStatus("select appId from project where id ="+pid);
    if(appId ==null || appId =="" ||appId.isEmpty()){
      appId ="other";
    }

    //标记测试包是否能用
    if (markId !=null) {
      request.getSession().removeAttribute("channelActive");
      //判断有没有登录
      if (userNick == null || userNick == "") {
//        response.sendRedirect("/pages/login.jspa?redirect_url=" + request.getRequestURL()+"?pid="+pid+"");
//        return mv;
        mv = new ModelAndView("/pages/login");
        return mv;
      }
      Sql ="select mark from app_package where  id =" + markId + "";
      String outputStatus = doPackage.selectStatus(Sql);
      if(outputStatus.equals("能用")) {
        Sql = "update app_package set mark ='不能用' where id =" + markId + "";
      }else if(outputStatus.equals("不能用")||outputStatus.equals("未标记")) {
        Sql = "update app_package set mark ='能用' where id =" + markId + "";
      }
      doPackage.executePackage(Sql);
      response.sendRedirect("/pages/getPackage.jspa?pid="+pid);
    }


    //取项目数据
    String projectInfo = doPackage.selectResult("select platform,svn,task,projectname,alias,modify,creater,updater,gitbranches from project where id =" + pid + "",9);
    if(projectInfo==null || projectInfo.isEmpty() || projectInfo==""){
      response.sendRedirect("/pages/fail.jspa?projectExist=noproject");
    }else {
      //取值
      String[] projectInfoSplit = projectInfo.split("\\|");

      //mac
      String WEB_ROOT = parameters.getProperty("WEB_ROOT");
      String APK_DIRECTORY = WEB_ROOT + "app_" + apkKeyWord + "/";
      String LOG_DIRECTORY = WEB_ROOT + "log_" + apkKeyWord + "/";
      String JENKINS_ROOT =parameters.getProperty("JENKINS_ROOT");
      String JENKINS_WORKSPACE =parameters.getProperty("JENKINS_WORKSPACE");
      String credentials = parameters.getProperty("credentials");
      String JENKINS_DIRECTORY = JENKINS_ROOT+"app_" + apkKeyWord + "/builds/";
      String BUILD_NUMBER = JENKINS_ADDRESS + "job/app_" + apkKeyWord + "/lastBuild/buildNumber --user shenzh:shenzh";
      String BUILD_JOB = JENKINS_ADDRESS + "job/app_" + apkKeyWord + "/build?token=12345 --user shenzh:shenzh";
      String filename =JENKINS_ROOT+"app_"+apkKeyWord+"/config.xml";
      String UPDATE_CONFIG =JENKINS_ADDRESS+"job/app_"+apkKeyWord+"/config.xml --user shenzh:shenzh --data-binary \"@"+filename+"\"";


      //取得总记录数
//      int totalPageSize = doPackage.selectPackage("select count(*) from app_package where branches ='" + apkKeyWord + "'");
      int totalNumber =0;
      String curl = "curl  " + BUILD_NUMBER + "";
      int buildNumber = doPackage.getBuildNumber(curl) + 1;
      int count = 0;
      String buildSuccesSql = "";
      String Status = "";
      String buildFailSql = "";
      int jobExist = 0;
      String keyword = "";
      String[] apkNameSplit = new String[]{};
      ZXingCode getPhoto = new ZXingCode();
      String target ="";
      String config_Path ="";
      String pathConfig ="";
      String existSql = "";

      String html_content = "";
      String plist_content = "";
      String html_path = "";
      String plist_path = "";
      String projectName = "";
      String projectTitle = "";
      String packageInfo = "";
      String apkPart ="";
      String newName ="";
      String ipaName ="";
//      String credentials = "641268f1-09ae-4dd4-ab59-a6ad05f2f605";
//    String credentials = "2b22e99e-1531-453c-b179-21d71cbdae67";
      String project = doPackage.getProjectName(projectInfoSplit[1]);

      //定义target
      if (projectInfoSplit[0].equals("iOS")) {
        keyword ="ipa";
        if (project.contains("gongfudai")) {
          projectName = "com.treefinance.gongfudai";
          projectTitle = "功夫贷";
          target = "gongfudai";
          ipaName = project;

        } else if (project.contains("kaixindai")) {
          projectName = "com.treefinance.kaxindai";
          projectTitle = "开薪贷";
          target = project;
          ipaName = project;
        } else if (project.contains("xiaoqidai")) {
          projectName = "com.treefinance.xiaoqidai";
          projectTitle = "小期贷";
          target = project;
          ipaName = project;
        } else if (project.contains("CreditReporting")) {
          projectName = "com.datatree.xianjinchaoshi";
          projectTitle = "现金超市";
          target = project;
          ipaName = project;
        } else if (project.contains("sdk-ios")) {
          projectName = "com.treefinance.servicePluginDemoTest";
          projectTitle = "sdkDemo";
          target = "TreefinanceService-Example";
          ipaName = "TreefinanceService-Example";
          credentials =parameters.getProperty("credentials");
        }else if (project.contains("aidashi")) {
          projectName = "com.treefinance.-ServicePluginDemo.Daidashi";
          projectTitle = "贷大师";
          target = "Daidashi";
          ipaName = "Daidashi";
        } else {
          projectName = "com.treefinance." + project;
          projectTitle = "新产品";
          target = project;
          ipaName = project;
        }
      } else if(projectInfoSplit[0].equals("Android")) {
        keyword ="apk";
        if (project.contains("xianjinchaoshi")) {
          apkPart = "supermarket";
        } else if(project.contains("sdk-and")){
          apkPart = "GFDServiceApplication";
          credentials =parameters.getProperty("credentials");
        }else {
          apkPart = "app";
        }
      }
//定义循环起始数目
      if(buildNumber<=20){
        totalNumber = buildNumber-1;
      }else{
        totalNumber =20;
      }

      for (int i = buildNumber - totalNumber; i <= buildNumber; i++) {
        String apkPath = APK_DIRECTORY + i + "/apk";
        Sql = "select count(*) from app_package where branches ='" + apkKeyWord + "' and job =" + i + "";
        Status = "select jobstatus from app_package where branches ='" + apkKeyWord + "' and job =" + i + "";
        count = doPackage.selectPackage(Sql);
        String curl_build_status = "curl " + JENKINS_ADDRESS + "job/app_" + apkKeyWord + "/" + i + "/api/json?tree=result --user shenzh:shenzh";
        if (count != 0 && doPackage.selectStatus(Status).equals("正在打包")) {
          String copylog = "cp " + JENKINS_DIRECTORY + i + "/log  " + LOG_DIRECTORY + i;
          Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", copylog});
          //gitlog
          String svnlogaddress = "/app_" + apkKeyWord + "/" + i + "/gitlog.txt";
          String filePath = APK_DIRECTORY + i + "/gitlog.txt";
          String svnLog = "";
          String apklink = "";
          String erweima = "";
          String photoUrl = "";
          String outPutPath = "";

          if (doPackage.getBuildStatus(curl_build_status) == 1) {
            String apkName = doPackage.getApkName(apkPath, keyword);

            if (projectInfoSplit[0].equals("Android")) {

              if(apkName !="") {
                apkNameSplit = apkName.split(",");
                for (int j = 0; j < apkNameSplit.length; j++) {
                  photoUrl = LOCATION_ADDRESS + "app_" + apkKeyWord + "/" + i + "/apk/" + apkNameSplit[j];
                  outPutPath = APK_DIRECTORY + i + "/" + apkNameSplit[j] + ".png";
                  getPhoto.getLogoQRCode(photoUrl, null, apkNameSplit[j], outPutPath, WEB_ROOT);

                  apklink = apklink + "," + "/app_" + apkKeyWord + "/" + i + "/apk/" + apkNameSplit[j];
                  erweima = erweima +","+ "/app_" + apkKeyWord + "/" + i + "/" + apkNameSplit[j] + ".png";
                }
                apklink =apklink.substring(1);
                erweima =erweima.substring(1);
              }

            } else {

              html_content = "<!DOCTYPE HTML>  \n" +
                      "<html>  \n" +
                      "  <head>  \n" +
                      "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />  \n" +
                      "    <title>安装"+projectTitle+"应用</title>  \n" +
                      "  </head>  \n" +
                      "  <body>  \n" +
                      "    <br>  \n" +
                      "    <br>  \n" +
                      "    <br>  \n" +
                      "    <br>  \n" +
                      "    <p align=center>  \n" +
                      "        <font size=\"8\">  \n" +
                      "        <a href='itms-services:///?action=download-manifest&url="+LOCATION_ADDRESS+"app_" + apkKeyWord + "/" + i + "/" + apkKeyWord + "_" + i + ".plist'>点击安装(如果是微信扫码，请点击右上角在safari打开)</a>\n" +
                      "        </font>  \n" +
                      "    </p>  \n" +
                      "   </div>  \n" +
                      "  </body>  \n" +
                      "</html>";
              plist_content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                      "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
                      "<plist version=\"1.0\">\n" +
                      "<dict>\n" +
                      "   <key>items</key>\n" +
                      "   <array>\n" +
                      "       <dict>\n" +
                      "           <key>assets</key>\n" +
                      "           <array>\n" +
                      "               <dict>\n" +
                      "                   <key>kind</key>\n" +
                      "                   <string>software-package</string>\n" +
                      "                   <key>url</key>\n" +
                      "                   <string>"+LOCATION_ADDRESS+"app_" + apkKeyWord + "/" + i + "/apk/"+ipaName+".ipa</string>\n" +
                      "               </dict>\n" +
                      "           </array><key>metadata</key>\n" +
                      "           <dict>\n" +
                      "               <key>bundle-identifier</key>\n" +
                      "               <string>" + projectName + "</string>\n" +
                      "               <key>bundle-version</key>\n" +
                      "               <string>1.0.0</string>\n" +
                      "               <key>kind</key>\n" +
                      "               <string>software</string>\n" +
                      "               <key>subtitle</key>\n" +
                      "               <string>" + projectTitle + "</string>\n" +
                      "               <key>title</key>\n" +
                      "               <string>" + projectTitle + "</string>\n" +
                      "           </dict>\n" +
                      "       </dict>\n" +
                      "   </array>\n" +
                      "</dict>\n" +
                      "</plist>";
              html_path = APK_DIRECTORY + i + "/" + apkKeyWord + "_" + i + ".html";
              plist_path = APK_DIRECTORY + i + "/" + apkKeyWord + "_" + i + ".plist";
              photoUrl = LOCATION_ADDRESS + "app_" + apkKeyWord + "/" + i + "/" + apkKeyWord + "_" + i + ".html";

              doPackage.createFile(html_path, html_content);
              doPackage.createFile(plist_path, plist_content);
              if(apkName !="") {
                apklink = "/app_" + apkKeyWord + "/" + i + "/apk/" + apkName;
                erweima = "/app_" + apkKeyWord + "/" + i + "/" + apkName + ".png";
                outPutPath = APK_DIRECTORY + i + "/" + apkName + ".png";
                getPhoto.getLogoQRCode(photoUrl, null, apkName, outPutPath, WEB_ROOT);
              }

            }

            //获取svnlog内容
            svnLog = doPackage.readFileContent(filePath).replace("\'","").replace("\"","");

//              svnLog = new String(svnLog.getBytes("UTF-8"));

            buildSuccesSql = "update app_package set jobstatus ='打包成功',apk ='" + apkName + "',erweima ='" + erweima + "',apklink ='" + apklink + "',svnlog ='" + svnLog + "',svnlogaddress ='" + svnlogaddress + "' where branches ='" + apkKeyWord + "' and job =" + i + "";
            doPackage.executePackage(buildSuccesSql);
//            getPhoto.getLogoQRCode(photoUrl, null, apkName, outPutPath, WEB_ROOT);
          }
          if (doPackage.getBuildStatus(curl_build_status) == 2) {
            //获取svnlog内容
            if (doPackage.readFileContent(filePath) != "") {
//              svnLog = doPackage.readFileContent(filePath).substring(0, txtLength);
              svnLog = doPackage.readFileContent(filePath).replace("\'","").replace("\"","");
            }

            buildFailSql = "update app_package set jobstatus ='打包失败',apk ='',svnlog ='" + svnLog + "',svnlogaddress ='" + svnlogaddress + "' where branches ='" + apkKeyWord + "' and job =" + i + "";
            doPackage.executePackage(buildFailSql);

          }
          if (doPackage.getBuildStatus(curl_build_status) == 3) {
            buildFailSql = "update app_package set jobstatus ='打包中断',apk ='' where branches ='" + apkKeyWord + "' and job =" + i + "";
            doPackage.executePackage(buildFailSql);

          }

        }

      }

      if (historypackaging||doRelease) {
        //判断有没有登录
        if(userNick==null || userNick==""){
//          response.sendRedirect("/pages/login.jspa?redirect_url=" + request.getRequestURL()+"?pid="+pid+"");
          mv = new ModelAndView("/pages/login");
          return mv;
        }

        String packagingSql = "select count(*) from app_package where branches ='" + apkKeyWord + "' and job =" + buildNumber;
        if (doPackage.selectPackage(packagingSql) == 1) {
          jobExist = 1;

        } else {
          // 执行修改项目操作
          packageInfo = doPackage.selectProjectInfo("select git,gitbranches,WEB_ROOT,JENKINS_APP_PATH,APK_DIRECTORY,packagetype,platform,alias,projectid from package_config where alias ='" + apkKeyWord + "'" );
          String [] packageInfoSplit = packageInfo.split("\\|");
          String git = packageInfoSplit[0];
          String gitbranches = packageInfoSplit[1];
          String webRoot = packageInfoSplit[2];
          String jenkinsAppPath = packageInfoSplit[3];
          String apkDirectory = packageInfoSplit[4];
          String packagetype = packageInfoSplit[5];
          String platform = packageInfoSplit[6];
          String alias = packageInfoSplit[7];

          if(doRelease){
            if(platform.equals("iOS")){
              packagetype = "PROD";
            }else if(platform.equals("Android")){
              packagetype = "assemblePro";
            }
            gitbranches ="master";
            request.getSession().setAttribute("channelActive", "channelTabDisplay");
          }else if(historypackaging){
            request.getSession().removeAttribute("channelActive");
          }

          if(target.equals("CreditReporting")||target.equals("Daidashi")){
            pathConfig ="Common";
          }else{
            pathConfig ="Supporting\\ Files";
          }

          String content = doPackage.configFile(git,gitbranches,webRoot,jenkinsAppPath,apkDirectory,packagetype,platform,alias,email,pathConfig,apkPart,credentials);
//          String configPath = APK_DIRECTORY+"/config.h";
          //更新config.xml
          doPackage.createFile(filename, content);

          if (platform.equals("iOS")&&!project.contains("sdk-ios")) {
            if(target.equals("CreditReporting")||target.equals("Daidashi")){
              pathConfig ="Common";
            }else{
              pathConfig ="Supporting Files";
            }
            config_Path = JENKINS_WORKSPACE+"app_" + apkKeyWord + "/"+target+"/"+pathConfig+"/config.h";
            String outPath=APK_DIRECTORY+"/config.h";
            String oldStr = "#define ISDEV YES";
            String oldStr02 = "#define ISDEV NO";
            String contentType = "#define ISDEV NO";
            String contentType02 = "#define ISDEV YES";

            //定义ios的config
            if(packagetype.equals("DEV")){
//              configPlist = doPackage.iosconfigFile("YES");
              doPackage.replaceContentToFile(config_Path,outPath,oldStr,oldStr02,contentType02);
            }else if(packagetype.equals("PROD")){
//              configPlist = doPackage.iosconfigFile("NO");
              doPackage.replaceContentToFile(config_Path,outPath,oldStr,oldStr02,contentType);
            }
//            doPackage.createFile(configPath,configPlist);
          }
          Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","curl -X POST "+UPDATE_CONFIG});


          Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "curl -X GET " + BUILD_JOB + ""});
          String logadreass = "/log_" + apkKeyWord + "/" + buildNumber;
//          packagetype = packagetype.replace("assemble", "");
          Sql = "insert into app_package(job,apk,createtime,jobstatus,erweima,apklink,log,branches,svnlog,svnlogaddress,pagtype,user,mark,gitbranches) values(" + buildNumber + ",'<div  onclick=\"location.reload()\"><i class=\"fa fa-refresh fa-spin\"></i></div>','" + Date + "','正在打包','','','" + logadreass + "','" + apkKeyWord + "','','','" + packagetype + "','" + userNick + "','未标记','"+gitbranches+"')";
          doPackage.executePackage(Sql);
          doPackage.createDirectory(APK_DIRECTORY + buildNumber);
          doPackage.createDirectory(APK_DIRECTORY + buildNumber + "/apk");
          doPackage.createDirectory(APK_DIRECTORY + buildNumber + "/output");
        }
      }

      //判断是否执行pod insatall
      if(podInstall){
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "sh " + APK_DIRECTORY + apkKeyWord+".sh"});
      }

      mv.addObject("jobExist", jobExist);

      mv.addObject("platform", projectInfoSplit[0]);
      mv.addObject("svn", projectInfoSplit[1].replace("git@192.168.5.252:","...").replace("ssh://git@192.168.5.24:10022","..."));
      mv.addObject("task", projectInfoSplit[2]);
      mv.addObject("projectname", projectInfoSplit[3]);
      mv.addObject("alias", projectInfoSplit[4]);
      mv.addObject("modify", projectInfoSplit[5]);
      mv.addObject("creater", projectInfoSplit[6]);
      mv.addObject("updater", projectInfoSplit[7]);
      mv.addObject("gitbranches", projectInfoSplit[8]);
      mv.addObject("pid", pid);

      //ios去除缓存
      if(projectInfoSplit[0].equals("iOS")) {
        request.getSession().removeAttribute("channelActive");
      }

      //取项目名
      LinkedHashMap<String, List> projectlist = new LinkedHashMap<String, List>();
      projectlist = doPackage.projectlist();
      mv.addObject("projectlist", projectlist);

      //取打包列表数据
      LinkedHashMap<String,List> packagelist = new LinkedHashMap<String,List>();
      packagelist = doPackage.selectList("select id,createtime,user,pagtype,jobstatus,apklink,apk,erweima,svnlogaddress,svnlog,log,mark,gitbranches from app_package where branches  ='"+apkKeyWord+"' order by id DESC",13);
      mv.addObject("packagelist", packagelist);

      //取渠道包tab的release包数据
      if(projectInfoSplit[0].equals("Android")) {
        Sql = "select id,createtime,user,pagtype,jobstatus,apk,apklink,erweima,svnlogaddress,svnlog,log,mark,job from app_package where createtime =(select max(createtime) from app_package where branches  ='" + apkKeyWord + "' and pagtype not in ('assembleDev','assembleDebug') and gitbranches ='master')";
      }else if(projectInfoSplit[0].equals("iOS")){
        Sql = "select id,createtime,user,pagtype,jobstatus,apk,apklink,erweima,svnlogaddress,svnlog,log,mark,job from app_package where createtime =(select max(createtime) from app_package where branches  ='" + apkKeyWord + "' and pagtype ='PROD' and gitbranches ='master')";
      }
      String releasePackage = doPackage.selectResult(Sql,13);
      List<String> releasePackageList = new ArrayList<String>();
      String[] releasePackageSplit = releasePackage.split("\\|");
      if(releasePackage !="") {
        for (int i = 0; i < releasePackageSplit.length; i++) {
          releasePackageList.add(releasePackageSplit[i]);
        }
      }
      mv.addObject("releasePackageList", releasePackageList);
      mv.addObject("releasePackageLength", releasePackageSplit.length);

      //定义apk路径
      String intput_dir ="";
      if(releasePackageSplit.length >1) {
        intput_dir = APK_DIRECTORY + releasePackageList.get(12) + "/apk/";
      }

      if(jiagu_360){
        //判断有没有master包
        if(releasePackageSplit.length == 1) {
          response.sendRedirect("/pages/fail.jspa?doChannelPackage=no");
          return mv;
        }
        //显示channeltab,存入缓存
        request.getSession().setAttribute("channelActive", "channelTabDisplay");
        String shName = APK_DIRECTORY+releasePackageList.get(12)+"/"+apkKeyWord+".sh";
        //加固前先删除所有包含jiagu的apk
        if(doPackage.delFilesByPath(intput_dir,keyword_apk)){
          System.out.println(intput_dir+"中包含"+keyword_apk+"的文件已经全部删除成功!");
        }else{
          System.out.println(intput_dir+"中包含"+keyword_apk+"的文件已经删除失败或该文件夹下不存在这类文件!");
        }
        //360加固
        String JIAGU=parameters.getProperty("360JIAGU");
        String jiaguSH ="cd "+JIAGU+"\n"+
                "java -jar jiagu.jar -login xiaoji123pt zhangping\n" +
                "java -jar jiagu.jar -config -crashlog -x86\n" +
                "java -jar jiagu.jar -jiagu "+intput_dir+"*elease.apk "+intput_dir+" -autosign";
        doPackage.createFile(shName, jiaguSH);
        Process jiaguProcess = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "sh " + shName + ""});
        try {
          jiaguProcess.waitFor();
        } catch (InterruptedException e) {
          response.sendRedirect("/pages/fail.jspa?timeout=yes");
          return mv;
        }
        //把加固后的apk改名
        if(appId.equals("gfdapp")){
          newName = "gfd-release-jiagu.apk";
        }else if(appId.equals("xqd")){
          newName = "xqd-release-jiagu.apk";
        }else if(appId.equals("kxd")){
          newName = "kxd-release-jiagu.apk";
        }if(appId.equals("xjcs")){
          newName = "supermarket-release-jiagu.apk";
        }else if(appId.equals("dds")){
          newName = "dds-release-jiagu.apk";
        }
        boolean result = doPackage.renameApk(intput_dir,keyword_apk,newName);
        if(!result){
          response.sendRedirect("/pages/fail.jspa");
          return mv;
        }

      }
      //如果android打过基础包的,在把加固信息发到前端
      if(releasePackageSplit.length > 1) {
        //把加固包的名字传到前台
        newName = doPackage.getFileName(intput_dir, keyword_apk);
        mv.addObject("newApkName", newName);
        mv.addObject("newApkUrl", "/app_" + apkKeyWord + "/" + releasePackageList.get(12) + "/apk/" + newName);
        mv.addObject("apkModiyTime", doPackage.getFileModifyTime(intput_dir + newName));
        mv.addObject("newJobId",releasePackageList.get(12));
      }

      //执行渠道包生成操作
      if (channelPackage) {
        String PYTHON_PACKAGE=parameters.getProperty("PYTHON_PACKAGE");

        //显示channeltab,存入缓存
        request.getSession().setAttribute("channelActive", "channelTabDisplay");

        //判断有没有登录
        if(userNick==null || userNick==""){
//          response.sendRedirect("/pages/login.jspa?redirect_url=" + request.getRequestURL()+"?pid="+pid+"");
          mv = new ModelAndView("/pages/login");
          return mv;
        }

        //存入选择的渠道集合到缓存
        request.getSession().setAttribute("channelArraySelect", channelArraySelect);

        //置打渠道包的动作状态为0 正在打包
        Sql = "select count(*) from channelReleaseStatus where projectId ="+pid;
        int releasePackageCount = doPackage.selectPackage(Sql);
        if(releasePackageCount ==0){
          Sql = "insert into channelReleaseStatus(projectId,creater,createtime,status) values('"+pid+"','" + userNick + "','" + Date + "',0)";
        }else{
          Sql = "update channelReleaseStatus set creater ='"+userNick+"',createtime = '"+Date+"',status = 0 where projectId ="+pid;
        }
        doPackage.executePackage(Sql);

        String output_dir = APK_DIRECTORY+releasePackageList.get(12)+"/output/";
        String tmp_dir = APK_DIRECTORY+releasePackageList.get(12)+"/tmp/";
        String shName = APK_DIRECTORY+releasePackageList.get(12)+"/"+apkKeyWord+".sh";
        //360加固
        String JIAGU=parameters.getProperty("360JIAGU");
        String jiaguSH ="cd "+JIAGU+"\n"+
                "java -jar jiagu.jar -login xiaoji123pt zhangping\n" +
                "java -jar jiagu.jar -config -crashlog -x86\n" +
                "java -jar jiagu.jar -jiagu "+intput_dir+"*elease.apk "+intput_dir+" -autosign\n"+
                "cd "+intput_dir+"\n"+
                "echo 'Txy30330'|sudo -S chmod -R 777 *\n" +
                "echo 'Txy30330'|sudo -S chown -R shenzhaohua:jenkins *\n"+
                "cd "+output_dir+"\n"+
                "echo 'Txy30330'|sudo -S chmod -R 777 *\n"+
                "cd "+PYTHON_PACKAGE+"\n"+
                "echo 'Txy30330'|sudo -S chmod -R 777 *\n";
        doPackage.createFile(shName, jiaguSH);
        Process jiaguProcess = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "sh " + shName + ""});
        try {
          jiaguProcess.waitFor();
        } catch (InterruptedException e) {
          //重置打渠道包的状态为2 打包异常
          Sql = "update channelReleaseStatus set creater ='"+userNick+"',createtime = '"+Date+"',status = 2 where projectId ="+pid;
          doPackage.executePackage(Sql);
          response.sendRedirect("/pages/fail.jspa?timeout=yes");
          return mv;
//          e.printStackTrace();
        }
        //等待360加固,10分钟后自动退出
        String fileName = doPackage.waitJiagu(intput_dir,keyword_apk);
        if(fileName ==""){
          //重置打渠道包的状态为2 打包异常
          Sql = "update channelReleaseStatus set creater ='"+userNick+"',createtime = '"+Date+"',status = 2 where projectId ="+pid;
          doPackage.executePackage(Sql);
          response.sendRedirect("/pages/fail.jspa?timeout=yes");
          return mv;
        }

        //等待生成release包执行完毕
        System.out.println("python "+PYTHON_PACKAGE +"MultiChannelBuildTool.py "+channelArraySelect+" "+intput_dir+" "+output_dir+" "+tmp_dir+" "+PYTHON_PACKAGE);
        Process process = Runtime.getRuntime().exec("python "+PYTHON_PACKAGE +"MultiChannelBuildTool.py "+channelArraySelect+" "+intput_dir+" "+output_dir+" "+tmp_dir+" "+PYTHON_PACKAGE);
        try {
          process.waitFor();
        } catch (InterruptedException e) {
          //重置打渠道包的状态为2 打包异常
          Sql = "update channelReleaseStatus set creater ='"+userNick+"',createtime = '"+Date+"',status = 2 where projectId ="+pid;
          doPackage.executePackage(Sql);
          response.sendRedirect("/pages/fail.jspa?timeout=yes");
          return mv;
        }

//        //等待15秒
//        try
//        {
//          Thread.sleep(15000);
//        }
//        catch (InterruptedException e)
//        {
//          e.printStackTrace();
//        }
        //删除重复的原纪录
        Sql = "select count(*) from channelPackageList where channelListId ="+channelArraySelect;
        int packageExist = doPackage.selectPackage(Sql);
        if(packageExist != 0) {
          Sql = "delete from channelPackageList where channelListId ="+channelArraySelect;
          doPackage.executePackage(Sql);
        }

        String releaseLink ="";
        String packageName ="";
        String photoUrl ="";
        String outPutPath ="";
        String erweima ="";
        Sql = "select content from channelList where id ="+channelArraySelect;
        String channelContent = doPackage.selectStatus(Sql);
        Map channelContentList = JSON.parseObject(channelContent);
        for (Object channelCode : channelContentList.keySet()) {
          if (project.contains("xianjinchaoshi")) {
            packageName = "supermarket-" + channelCode + "-release.apk";
          }else{
            packageName = "app-" + channelCode + "-release.apk";
          }
          releaseLink = "/app_" + apkKeyWord + "/" + releasePackageList.get(12) + "/output/"+packageName;
          erweima = "/app_" + apkKeyWord + "/" + releasePackageList.get(12) + "/" + packageName + ".png";

          photoUrl = LOCATION_ADDRESS + releaseLink.substring(1);
          outPutPath = APK_DIRECTORY + releasePackageList.get(12) + "/"+packageName+".png";
          getPhoto.getLogoQRCode(photoUrl, null, packageName, outPutPath, WEB_ROOT);

            Sql = "insert into channelPackageList(createtime,creater,channelCode,channelName,packageName,packageLink,erweima,mark,jobId,channelListId,projectId) values('" + Date + "','" + userNick + "','" + channelCode + "','" + channelContentList.get(channelCode) + "','" + packageName + "','" + releaseLink + "','" + erweima + "','未上传'," + releasePackageList.get(12) + ",'" + channelArraySelect + "','" + pid + "')";
            doPackage.executePackage(Sql);
        }
        //重置打渠道包的状态为1 打包结束 空闲状态
        Sql = "update channelReleaseStatus set creater ='"+userNick+"',createtime = '"+Date+"',status = 1 where projectId ="+pid;
        doPackage.executePackage(Sql);

      }

      //取渠道包列表
      LinkedHashMap<String,List> releaseApkList = new LinkedHashMap<String,List>();
      Sql = "select cpl.id,cpl.createtime,cpl.creater,cpl.channelCode,cpl.channelName,cpl.packageName,cpl.packageLink,cpl.erweima,cpl.mark,cpl.jobId,cl.listName from channelPackageList cpl left join channelList cl on cpl.channelListId =cl.id  where cpl.projectId ='"+pid+"' order by cpl.id DESC";
      releaseApkList = doPackage.selectList(Sql,11);
      mv.addObject("releaseApkList", releaseApkList);

      //取channel集合
      LinkedHashMap<String,List> channelArrayList = new LinkedHashMap<String,List>();
      Sql = "select id,listName from channelList where appId ='"+appId+"' order by id DESC";
      channelArrayList = doPackage.selectList(Sql,2);
      mv.addObject("channelArrayList", channelArrayList);

      //取渠道包打包状态给前端页面展示
      Sql = "select status from channelReleaseStatus where projectId ="+pid;
      String packageStatus = doPackage.selectStatus(Sql);
      mv.addObject("packageStatus",packageStatus);

      mv.addObject("redirect_url", request.getRequestURL()+"?"+request.getQueryString());
    }
    return mv;
  }

}
