package com.dashu.datashow.service;

/**
 * Created by shenzhaohua on 16/7/16.
 */

import com.dashu.datashow.controller.CreateGraph;
import com.dashu.datashow.controller.packageData;
import com.dashu.datashow.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;


public class SendMailServlet extends HttpServlet {

      String sql ="";
      String Graph_Title ="";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //设置字符编码
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String Date = TimeUtil.getCurrentTime();
        String TwoWeek = TimeUtil.getAdjustTime(Calendar.DATE, -23).substring(0, 10) + " 00:00:00";

        String content_Graph = "";

        packageData report = new packageData();
        String redirect_url = request.getParameter("redirect_url");

        //从页面获取参数
        String userNick =request.getParameter("usernick");
        String toAddressInputTxt = request.getParameter("toaddressInput").trim();
        String toAddressInput = request.getParameter("toaddressInput").trim();
        String[] toAddressSelect = request.getParameterValues("toaddressSelect");
        String[] project = request.getParameterValues("project");
        String total = request.getParameter("total");
        String jindu = request.getParameter("jindu");
        String description = request.getParameter("description");
        String startDate = request.getParameter("startdate");
        String ccInput = request.getParameter("ccInput");
        String test1 = request.getParameter("test1");
        String test2 = request.getParameter("test2");
        String test3 = request.getParameter("test3");
        String test4 = request.getParameter("test4");

        String bugGraph = test1 + "," + test2 + "," + test3 + "," + test4;

        String projectid = "";
        String projectName = "";
        String toAddressTmp = "";
        String toAddressTotal = "";

        if (toAddressSelect != null) {
            for (int i = 0; i < toAddressSelect.length; i++) {
                toAddressTmp = toAddressTmp + toAddressSelect[i] + ",";
            }
        }

        if (project == null) {
            projectid = "999999";
        } else {
            for (int i = 0; i < project.length; i++) {
                String projectTmp[] = project[i].split("\\|");
                if (i == 0) {
                    projectName = projectTmp[0];
                    projectid = projectTmp[1];
                } else {
                    projectName = projectName + "," + projectTmp[0];
                    projectid = projectid + "," + projectTmp[1];
                }
            }
        }

//        if (userNameEmail == null || userNameEmail.isEmpty() || userNameEmail == "") {
//            userNameEmail = "admin@car-me.com|admin";
//        }

//        String userName[] = userNameEmail.split("\\|");

        String Graph_BugStatus = "";
        String Graph_Dev = "";
        String Graph_Severity = "";
        String Graph_Everyday = "";
        String[] toAddress = new String[]{};
        String[] AddressInput = toAddressInput.split(",");
        String[] ccList = new String[]{};

        if (startDate == null) {
            startDate = TimeUtil.getAdjustTime(Calendar.DATE, -30);
        }else {
            startDate =startDate.replace("T"," ");
        }
//        System.out.println("startDate="+startDate);
        //拼接发送邮件地址
        if (toAddressInput.length() == 0) {

            toAddress = toAddressSelect;
            toAddressTotal = toAddressTmp;

        } else {
            toAddress = AddressInput;
            toAddressTotal = toAddressInputTxt;
        }

        CreateGraph Graph_add = new CreateGraph();

        //未解决bug归属分布图生成
        sql = "select realname as bugReportStatus,count(bi.updated_by) as bugNum FROM bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id LEFT JOIN bf_test_user bu ON bi.assign_to = bu.id   where pr.id in (" + projectid + ")  and bug_status in('Active') AND bi.created_at between '" + startDate + "' and '" + Date + "' group by bi.assign_to";
        Graph_Title = "<b>※未解决bug归属分布※</b>(查询时段:"+startDate+"~~"+Date+") 传送门:http://192.168.5.104/bugfree/index.php/bug/list/" + projectid + "";
        Graph_Dev = Graph_add.CreateGraphDo(sql, Graph_Title,"");

        //未解决bug严重程度分布图生成
        sql = "select severity as bugSeverity ,count(bi.severity) as bugNum FROM bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id  where pr.id in (" + projectid + ")   and bug_status in ('Active') AND bi.updated_at between '" + startDate + "' and '" + Date + "' group by bi.severity";
        Graph_Title = "<b>※未解决bug严重程度分布※</b>(查询时段:"+startDate+"~~"+Date+") 传送门:http://192.168.5.104/bugfree/index.php/bug/list/" + projectid + "";
        Graph_Severity = Graph_add.CreateGraphDo(sql, Graph_Title,"severity");

        //每日新增bug分布图生成
        sql = "select Date(bi.created_at) as bugNewDate, COUNT(Date(bi.created_at))  as bugNum from bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id  where pr.id in (" + projectid + ")  and bi.created_at between '" + TwoWeek + "' and '" + Date + "' group by Date(bi.created_at)";
        Graph_Title = "<b>※每日新增bug分布图(最近一个月)</b>※ 传送门:http://192.168.5.104/bugfree/index.php/bug/list/" + projectid + "";
        Graph_Everyday = Graph_add.CreateGraphDo(sql, Graph_Title,"");

        //总bug状态分布
        sql = "SELECT bi.bug_status as bugResolutionStatus, COUNT(bi.bug_status)  as bugNum FROM bf_bug_info bi LEFT JOIN bf_product pr ON pr.id = bi.product_id  where pr.id in(" + projectid + ")   AND bi.created_at between '" + startDate + "' and '" + Date + "' group by bi.bug_status";
        Graph_Title = "<b>※各平台未解决bug分布※</b>(查询时段:"+startDate+"~~"+Date+") 传送门:http://192.168.5.104/bugfree/index.php/bug/list/" + projectid + "";
        Graph_BugStatus = Graph_add.CreateGraphDo(sql, Graph_Title,"");

        //生成日报

        //        String subject = projectName + "_项目日报_" + Date + "_" + userName[1];
        String subject = projectName + "_项目日报_" + Date + "_" + userNick;
        String content = "";
        String content_Description = "<table style =\"width:900px; border:1px;\"align=\"left\"><tr style=\"background:#e1e1e1;\" align=\"center\"><td><b><font color=\"#FF0000\" size=\"4\">" + subject + "</font></b></td></tr>\n" +
                "<tr  align=\"left\"><td>\n" +
                "<table width=900px>\n" +
                "<tr style=\"background:#e1e1e1;\"><td style =\"width:100px;\" align=\"center\"><font color =\"#0080FF\"><b>测试进度</b></font></td><td style =\"width:800px\">" + jindu + "</td></tr>\n" +
                "<tr style=\"background:#e1e1e1;\"><td style =\"width:100px\" align=\"center\"><font color =\"#0080FF\"><b>质量打分</b></font></td><td style =\"width:800px\">" + total + "</td></tr>\n" +
                "<tr style=\"background:#e1e1e1;\"><td style =\"width:100px\" align=\"center\"><font color =\"#0080FF\"><b>质量描述</b></font></td><td style =\"width:800px\">" + description + "</td></tr></table>\n" +
                "</tr>";


        if (test1 != null && Graph_Dev != "") {
            content_Graph = content_Graph + Graph_Dev;
        }
        if (test2 != null && Graph_Severity != "") {
            content_Graph = content_Graph + Graph_Severity;
        }
        if (test3 != null && Graph_Everyday != "") {
            content_Graph = content_Graph + Graph_Everyday;
        }
        if (test4 != null && Graph_BugStatus != "") {
            content_Graph = content_Graph + Graph_BugStatus;
        }

        //抄送
        if(ccInput!=null) {
            ccList = ccInput.split(",");
        }

        content = content_Description + content_Graph + "</table>";
        //发送方的邮箱地址
//        String fromAddress = userName[0];
        String fromAddress = "alarm@treefinance.com.cn";
        String password ="dashu123";

        MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
        mailInfo.setMailServerHost("smtp.exmail.qq.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUserName(fromAddress);
        mailInfo.setPassword(password);
        mailInfo.setFromAddress(fromAddress);
//        mailInfo.setToAddress(userName[0]);
        mailInfo.setToAddress("shenzhaohua@treefinance.com.cn");
        mailInfo.setSubject(subject);
        mailInfo.setContent(content);

        mailInfo.setReceivers(toAddress);
        boolean flag = false;
        if (project != null) {
            if (ccInput == ""||ccInput==null ||ccInput.isEmpty()) {
                flag = MultiMailsender.sendMailtoMultiReceiver(mailInfo);
                ccInput ="";
            }else {
                mailInfo.setCcs(ccList);
                flag = MultiMailsender.sendMailtoMultiCC(mailInfo);
            }
            if (flag) {
                System.out.println("发送html格式mail 成功！");

                report.executePackage("insert into emaillist(createname,createtime,project,toaddress,cc,quality,title,description,buglist,isdraft) values('" + userNick+ "','" + Date + "','" + subject + "','" + toAddressTotal + "','" + ccInput + "','" + total + "','" + jindu + "','" + description + "','" + bugGraph + "','0')");

                response.sendRedirect("/pages/success.jspa?redirect_url="+redirect_url+"");
            } else {
                System.out.println("发送html格式mail 失败！");
                response.sendRedirect("/pages/fail.jspa?projectName=" + projectName  + "&toAddress=" + toAddressTotal + "&ccList=" + ccInput + "&report="+flag+"");
                return;
            }

        }else{
            response.sendRedirect("/pages/fail.jspa?projectName=" + projectName+ "&report="+flag+"");
        }
    }

}
