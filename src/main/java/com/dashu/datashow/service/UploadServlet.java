package com.dashu.datashow.service;

/**
 * Created by shenzhaohua on 16/7/16.
 */

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.ListBucketsRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.SimplifiedObjectMeta;
import com.dashu.datashow.controller.packageData;
import com.dashu.datashow.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class UploadServlet extends HttpServlet {
    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "LTAIvUChfXOV5FqW";
    private static String accessKeySecret = "Zb4pBqPtB6TSnUXAdkPe839AG8sjT1";
    private static String bucketName = "gongfuapp";
////测试环境配置
//    private static String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
//    private static String accessKeyId = "LTAIIBcioZqmnUXO";
//    private static String accessKeySecret = "0B4Z584kDblAJvTIXnmCPWa2xx8p0j";
//    private static String bucketName = "treefinance";

    packageData doPackage = new packageData();
    Properties parameters = doPackage.getParameterProperties();
    String WEB_ROOT = parameters.getProperty("WEB_ROOT");

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
        String pid = request.getParameter("pid");
        String channelArraySelect = request.getParameter("channelArraySelect");
        String jobId = request.getParameter("jobId");
        String appUrl ="http://gongfuapp.oss-cn-hangzhou.aliyuncs.com/";
//        request.getSession().removeAttribute("uploadResult");
        String Sql ="";
        String filePath ="";
        String result ="";
        String modifyTime ="";
        String modifyTimeAfter ="";
        String appName ="";
        String mark ="<font color ='red'>上传失败!</font>";
        int resultCount = 0;
        Object groupname = request.getSession().getAttribute("groupname");

        if(groupname==null){
            response.sendRedirect("/pages/login.jspa?redirect_url=getPackage.jspa?pid="+pid+"");
            return;
        }else if(!groupname.toString().contains("upload")){
            response.sendRedirect("/pages/fail.jspa?permission=no");
            return;
        }

        //取渠道包信息
        LinkedHashMap<String, List> channelPackageList = new LinkedHashMap<String, List>();
        Sql = "select channelCode,channelName,packageName,packageLink,id from channelPackageList where projectId ='" + pid + "' and channelListId =" + channelArraySelect +" and jobId ='"+jobId+"'";
        channelPackageList = doPackage.selectList(Sql, 5);
        if(channelPackageList.size()==0){
            response.sendRedirect("/pages/fail.jspa?doChannelPackage=no");
            return;
        }

        //遍历map
        Set<String> keys = channelPackageList.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            List<String> arrayList = channelPackageList.get(key);
            filePath =WEB_ROOT+arrayList.get(2);
            appName = arrayList.get(1);
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            try {
                ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
                listBucketsRequest.setMaxKeys(500);
            /*
             * Upload an object to your bucket
             */
                boolean exists = ossClient.doesObjectExist(bucketName, appName);
                if(exists) {
                    //取未传前的修改时间
                    SimplifiedObjectMeta meta = ossClient.getSimplifiedObjectMeta(bucketName, appName);
                    modifyTime = meta.getLastModified().toString();
                }
                System.out.println("Does object " + appName + " exist? " + exists + "\n");
                ossClient.putObject(new PutObjectRequest(bucketName, appName, new FileInputStream(filePath)));
                boolean existsAfter = ossClient.doesObjectExist(bucketName, appName);
                if(existsAfter) {
                    //取传后的修改时间
                    SimplifiedObjectMeta metaAfter = ossClient.getSimplifiedObjectMeta(bucketName, appName);
                    modifyTimeAfter = metaAfter.getLastModified().toString();
                    if (modifyTime.equals(modifyTimeAfter)) {
                        mark = "<font color ='red'>上传失败!</font>";
                    } else {
                        mark = "<font color ='blue'>上传成功!</font>";
                    }
                }else{
                    mark = "<font color ='red'>上传失败!</font>";
                }

            } catch (OSSException oe) {

            } catch (ClientException ce) {

            }finally {
                ossClient.shutdown();
            }
            //重置上传状态
            if(mark.contains("上传成功")) {
                Sql = "update channelPackageList set mark ='上传成功' where  id =" + arrayList.get(3) + "";
            }else{
                Sql = "update channelPackageList set mark ='上传失败' where  id =" + arrayList.get(3) + "";
            }
            doPackage.executePackage(Sql);
            resultCount ++;

            if(resultCount>=50){
               result = "<tr><th> 共上传包个数:<font color='#4682B4' size =2>" + resultCount+ "</font> 个</th></tr>";
            }else {
                result = result + "</th></tr><tr><th>" + mark + "&nbsp;&nbsp;&nbsp;&nbsp;渠道号:<font color='orange' size =2>" + key + "</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "渠道名:<font color='orange' size =2>" + arrayList.get(0) + "</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;包名:<font color='orange' size =2>" + arrayList.get(1) + "</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;更新时间:<font color='orange' size =2>" + modifyTimeAfter
                        + "</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;下载地址:<a href='" + appUrl + arrayList.get(1) + "'><font color='#4682B4' size =2>" + appUrl + arrayList.get(1) + "</font></a>";
            }
        }
        request.getSession().setAttribute("uploadResult", result);
        response.sendRedirect("/pages/uploadPackage.jspa?pid="+pid+"&channelArraySelect="+channelArraySelect);

    }

}
