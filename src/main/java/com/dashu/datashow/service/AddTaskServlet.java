package com.dashu.datashow.service;

/**
 * Created by shenzhaohua on 16/7/16.
 */

import com.dashu.datashow.controller.Test02;
import com.dashu.datashow.controller.packageData;
import com.dashu.datashow.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;


public class AddTaskServlet extends HttpServlet {

    packageData doPackage =new packageData();
    Properties parameters = doPackage.getParameterProperties();
    String JENKINS_ADDRESS  =parameters.getProperty("JENKINS_ADDRESS");

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
        String taskId = request.getParameter("taskId");
        String taskName = request.getParameter("taskName");
        String taskAlias = request.getParameter("taskAlias");
        String git = request.getParameter("git");
        String gitbranches = request.getParameter("gitbranches");
        String redirect_url = request.getParameter("redirect_url");
        String action = request.getParameter("action");
        Object userNick = request.getSession().getAttribute("userNick");
        String addressInput = request.getParameter("addressInput");
        String isdelete = request.getParameter("isdelete");
        String did = request.getParameter("did");
        String content = "";

        Object userEmail = request.getSession().getAttribute("email");
        if (userEmail == null || userEmail == "") {
            userEmail = "empty";
        }
//    String email=userEmail.toString();
        //定义文件夹名和文件内容
        String JENKINS_ROOT = parameters.getProperty("JENKINS_ROOT");
        String filePath = JENKINS_ROOT + "task_" + taskAlias;
        String filename = filePath + "/config.xml";
        String CREATE_JOB = JENKINS_ADDRESS + "createItem?name=task_" + taskAlias + "  --user shenzh:shenzh --data-binary \"@" + filename + "\" -H \"Content-Type: text/xml\"";
        String UPDATE_CONFIG = JENKINS_ADDRESS + "job/task_" + taskAlias + "/config.xml --user shenzh:shenzh --data-binary \"@" + filename + "\"";

        if (isdelete == null || isdelete.isEmpty() || isdelete == "") {
            isdelete = "no";
        }

        packageData doPackage = new packageData();
        content = doPackage.taskConfigFile(git, gitbranches, action, addressInput);

        Object user = request.getSession().getAttribute("uid");
        if (user == null) {
            response.sendRedirect("/pages/login.jspa?redirect_url=/pages/taskList.jspa");
        } else {

            if (isdelete.equals("yes")) {
                String creater = doPackage.selectStatus("select creater from task_list where taskId =" + taskId);
                if (Integer.parseInt(String.valueOf(user)) != 1 && !creater.equals(userNick)) {
                    response.sendRedirect("/pages/fail.jspa?permission=no");
                    return;
                } else {
                    //删除jenkins的job
                    taskAlias = doPackage.selectStatus("select taskAlias from task_list where taskId = "+did);
                    String DELETE_CONFIG = JENKINS_ADDRESS + "job/task_" + taskAlias + "/doDelete --user shenzh:shenzh";
                    Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "curl -X POST " + DELETE_CONFIG});
                    doPackage.executePackage("delete from task_list where taskId ='" + did + "'");
                    System.out.println("删除成功。");
                    response.sendRedirect("/pages/success.jspa?redirect_url=/pages/taskList.jspa");
                }
            } else {

                int taskAliasExist = doPackage.selectPackage("select count(*) from task_list where taskAlias ='" + taskAlias + "'");

                //显示原先值
                if (taskName.length() == 0 || addressInput.length() == 0 || taskAlias.length() == 0 || action.length() == 0 ||git.length() == 0 ||gitbranches.length() == 0) {

                    response.sendRedirect("/pages/fail.jspa?taskName=" + taskName + "&addressInput=" + addressInput + "&taskAlias=" + taskAlias + "&action=" + action+ "&git=" + git+ "&git_branches=" + gitbranches + "&taskAliasExist=" + taskAliasExist);
                } else if (taskAliasExist >= 1 && taskId.length() == 0) {
                    response.sendRedirect("/pages/fail.jspa?taskAliasExist=" + taskAliasExist + "");
                } else if (taskId.length() == 0) {
                    //创建本地文件夹和config.xml文件
                    boolean createok = doPackage.createDirectory(filePath);
                    if (createok) {
                        doPackage.createFile(filename, content);
                    }
                    //创建Jenkins的job
                    Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "curl -X POST " + CREATE_JOB});

                    //插入数据
                    doPackage.executePackage("insert into task_list(createtime,updatetime,creater,updater,taskName,taskAlias,git,gitbranches,action,addressInput) values('" + Date + "','" + Date + "','" + userNick + "','" + userNick + "','" + taskName + "','" + taskAlias + "','" + git + "','" + gitbranches + "','" + action + "','" + addressInput + "')");
                    response.sendRedirect("/pages/success.jspa?redirect_url=" + redirect_url + "");
                } else {
                    //更新config.xml
                    doPackage.createFile(filename, content);
                    Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "curl -X POST " + UPDATE_CONFIG});
                    //更新表
                    doPackage.executePackage("update task_list set updatetime ='" + Date + "',updater='" + userNick + "',taskName='" + taskName + "',taskAlias='" + taskAlias + "',git='" + git + "',gitbranches ='" + gitbranches + "',action='" + action + "',addressInput='" + addressInput + "' where taskId=" + taskId + "");
                    System.out.println("更新成功。");
                    response.sendRedirect("/pages/success.jspa?redirect_url=" + redirect_url + "");
                }
            }
        }
    }




}
