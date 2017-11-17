package com.dashu.datashow.controller;

/**
 * Created by shenzhaohua on 2017/7/4.
 */


import java.io.*;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;

/**
 * This sample demonstrates how to get started with basic requests to Aliyun OSS
 * using the OSS SDK for Java.
 */
public class Test02 {

    /**
     * @Title: main
     * @Description:
     * @param args
     * @return void
     * @author lisr
     * @date Mar 7, 2012 5:36:04 PM
     * @throws
     */
    //用以模糊删除头部为str的文件
    public static boolean delFilesByPath(String path,String str){
        //参数说明---------path:要删除的文件的文件夹的路径---------str:要匹配的字符串的头
        boolean b=false;
        File file = new File(path);
        File[] tempFile = file.listFiles();
        for(int i = 0; i < tempFile.length; i++){
            if(tempFile[i].getName().indexOf(str) !=-1){
                tempFile[i].delete();
                b=true;
            }
        }
        return b;
    }
    public static void main(String[] args) {
        String path="/Users/shenzhaohua/Downloads/";
        String str="11Android";
        if(delFilesByPath(path,str)){
            System.out.println(path+"中包含"+str+"的文件已经全部删除成功!");
        }else{
            System.out.println(path+"中包含"+str+"的文件已经删除失败或该文件夹下不存在这类文件!");
        }
    }
}