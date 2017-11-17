package com.dashu.datashow.service;

/**
 * Created by shenzhaohua on 16/7/16.
 */

import com.dashu.datashow.controller.Test02;
import com.dashu.datashow.util.TimeUtil;
import com.dashu.datashow.controller.packageData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;


public class AddProjectServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //设置字符编码
    request.setCharacterEncoding("utf-8");
    response.setContentType("text/html;charset=utf-8");
    String Date = TimeUtil.getCurrentTime();
    //从页面获取参数
    String id = request.getParameter("id");
    String platform = request.getParameter("platform");
    String svn = request.getParameter("svn").trim();
    String packagetype = request.getParameter("packagetype");
    String projectname = request.getParameter("projectname").trim();
    String alias = request.getParameter("alias").trim();
    String redirect_url = request.getParameter("redirect_url");
    String platformSelect = request.getParameter("platformSelect");
    Object usrNick = request.getSession().getAttribute("userNick");
    String gitbranches = request.getParameter("gitbranches").trim();
    String appId = request.getParameter("appId");
    String content ="";
    String configPlist ="";
    String configPath ="";
    String apkPart ="";
//    String credentials = "641268f1-09ae-4dd4-ab59-a6ad05f2f605";
//    String credentials = "2b22e99e-1531-453c-b179-21d71cbdae67";

        Object userEmail = request.getSession().getAttribute("email");
    if(userEmail==null || userEmail==""){
        userEmail="empty";
    }
    String email=userEmail.toString();
    //定义文件夹名和文件内容
    packageData doPackage =new packageData();
    Properties parameters = doPackage.getParameterProperties();
    String JENKINS_ADDRESS=parameters.getProperty("JENKINS_ADDRESS");
    String WEB_ROOT=parameters.getProperty("WEB_ROOT");
    String JENKINS_ROOT = parameters.getProperty("JENKINS_ROOT");
    String JENKINS_WORKSPACE =parameters.getProperty("JENKINS_WORKSPACE");
    String credentials = parameters.getProperty("credentials");
    String JENKINS_APP_PATH =JENKINS_WORKSPACE+"app_"+alias;
    String APK_DIRECTORY = WEB_ROOT+"app_"+alias;
    String LOG_DIRECTORY = WEB_ROOT+"log_"+alias;
    String filePath =JENKINS_ROOT+"app_"+alias;
    String filename =filePath+"/config.xml";
    String CREATE_JOB =JENKINS_ADDRESS+"createItem?name=app_"+alias+"  --user shenzh:shenzh --data-binary \"@"+filename+"\" -H \"Content-Type: text/xml\"";
    String BUILD_JOB = JENKINS_ADDRESS + "job/app_" + alias + "/build?token=12345 --user shenzh:shenzh";
    if(platform==null || platform.isEmpty() || platform==""){
        platform = platformSelect;
    }

    //取git名称 判断是否是现金超市的
        if(platform.equals("Android")) {
            apkPart = doPackage.getProjectName(svn);
            if (apkPart.contains("xianjinchaoshi")) {
                apkPart = "supermarket";
            } else if(apkPart.contains("sdk-andriod")){
                apkPart = "GFDServiceApplication";
                credentials ="0cbc05a8-7f43-4627-8948-06b3e728069c";
            }else {
                apkPart = "app";
            }
        }
    content = doPackage.configFile(svn,gitbranches,WEB_ROOT,JENKINS_APP_PATH,APK_DIRECTORY,packagetype,platform,alias,email,"",apkPart,credentials);
//        //定义ios的config.h
//        if(packagetype =="DEV"){
//            configPlist = doPackage.iosconfigFile("YES");
//        }else if(packagetype =="PROD"){
//            configPlist = doPackage.iosconfigFile("NO");
//        }else {
//            configPlist = doPackage.iosconfigFile("YES");
//        }

    int aliasExist = doPackage.selectPackage("select count(*) from project where alias ='" + alias + "'");

            //显示原先值
    if (platform == null  || projectname.length() == 0 || svn.length() == 0 || packagetype  == null || alias.length() == 0 || gitbranches.length() == 0 ) {
        if(platform == null) {
            platform = "kong";
        }
        if(packagetype == null) {
            packagetype = "kong";
        }
            response.sendRedirect("/pages/fail.jspa?platform="+platform+"&projectname="+projectname+"&svn="+svn+"&packagetype="+packagetype+"&alias="+alias+"&aliasExist="+aliasExist+"&gitbranches="+gitbranches);
    } else if (aliasExist >= 1&& id.length()== 0) {
            response.sendRedirect("/pages/fail.jspa?aliasExist="+aliasExist+"");
    } else if(id.length()== 0){
        //创建apk和log存储目录
        doPackage.createDirectory(APK_DIRECTORY);
        doPackage.createDirectory(LOG_DIRECTORY);
        //创建本地文件夹和config.xml文件
        boolean createok = doPackage.createDirectory(filePath);
        if(createok) {
            if (platform.equals("iOS")) {
                //重新定义ios的config.xml内容
                content = doPackage.iosConfigFile(svn,gitbranches,credentials);
                //创建config.h文件
                configPath = APK_DIRECTORY + "/config.h";
                doPackage.createFile(configPath, configPlist);
            }
            doPackage.createFile(filename, content);
            //创建Jenkins的job
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","curl -X POST "+CREATE_JOB});
        }

        //插入数据
        doPackage.executePackage("insert into project(projectname,svn,platform,task,createtime,modify,alias,creater,updater,gitbranches,appId) values('" + projectname + "','" + svn + "','" + platform + "','" + packagetype + "','" + Date + "','" + Date + "','" + alias + "','" + usrNick + "','" + usrNick + "','" + gitbranches + "','" + appId + "')");
        doPackage.executePackage("insert into package_config(user,git,gitbranches,web_root,jenkins_app_path,apk_directory,packagetype,platform,alias,createtime,projectid) values('" + usrNick + "','" + svn + "','" + gitbranches + "','" + WEB_ROOT + "','" + JENKINS_APP_PATH + "','" + APK_DIRECTORY + "','" + packagetype + "','" + platform + "','" + alias + "','" + Date + "','" + id + "')");
        System.out.println("插入成功。");
        response.sendRedirect("/pages/success.jspa?redirect_url="+redirect_url+"");
        //延迟执行ios的job
        if (platform.equals("iOS")&&!doPackage.getProjectName(svn).contains("sdk-ios")) {
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "curl -X GET " + BUILD_JOB + ""});
            //创建pod update脚本
            String shName = APK_DIRECTORY+"/"+alias+".sh";
            String shContent ="cd /Users/Shared/Jenkins/Home/workspace/\n" +
                    "echo 'Txy30330'|sudo -S chmod -R 777 app_"+alias+"\n" +
                    "cd app_"+alias+"\n" +
                    "pod update \n"+
                    "echo 'Txy30330'|sudo -S chmod -R 777 *";
            doPackage.createFile(shName, shContent);
        }
    }else {
//        //更新config.xml
//        doPackage.createFile(filename, content);
//        if (platform.equals("iOS")) {
//            doPackage.createFile(configPath,configPlist);
//        }
//        Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","curl -X POST "+UPDATE_CONFIG});
        //更新表
        doPackage.executePackage("update project set projectname ='" + projectname + "',svn='" + svn + "',platform='" + platform + "',task='" + packagetype + "',modify='" + Date + "',alias='" + alias + "',updater='" + usrNick + "',gitbranches='" + gitbranches + "',appId='" + appId + "' where id=" + id + "");
        doPackage.executePackage("update package_config set git ='"+svn+"',gitbranches='"+gitbranches+"',packagetype='"+packagetype+"',platform='"+platform+"',createtime='"+Date+"',projectid='"+id+"',apk_directory='"+APK_DIRECTORY+"' where alias='" + alias+"'");

        System.out.println("更新成功。");
            response.sendRedirect("/pages/success.jspa?redirect_url="+redirect_url+"");
    }
    }




}
