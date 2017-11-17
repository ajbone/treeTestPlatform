package com.dashu.datashow.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenzhaohua on 16/7/20.
 */
public class CreateGraph {
    static DBHelper db1 = null;
    static ResultSet ret = null;


    public String CreateGraphDo (String sql,String Graph_Title,String type){
        //各平台未解决bug分布图生成
        double Platform_Max = 0;
        double BugNum = 0;
        String Graph_Create ="";
        String Graph_column = "";
        String Graph_rank = "";

//连接数据库查数据
        db1 = new DBHelper(sql);//创建DBHelper对象
        List xList = new ArrayList<>();
        List yList = new ArrayList<>();

        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                if(type.equals("severity")) {
                    if(ret.getString(1).equals("1")){
                        xList.add("致命");
                    }else if(ret.getString(1).equals("2")){
                        xList.add("严重");
                    }if(ret.getString(1).equals("3")){
                        xList.add("一般");
                    }if(ret.getString(1).equals("4")){
                        xList.add("轻微");
                    }if(ret.getString(1).equals("5")){
                        xList.add("微小");
                    }if(ret.getString(1).equals("6")){
                        xList.add("遗留");
                    }
                } else {
                    xList.add(ret.getString(1));
                }
                yList.add(ret.getString(2));

            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < yList.size(); i++) {
            if (Platform_Max <= Double.parseDouble(yList.get(i).toString())) {
                Platform_Max = Double.parseDouble(yList.get(i).toString());
            }
        }
//        System.out.println("Platform_Max =" + Platform_Max);

        for (int i = 0; i < yList.size(); i++) {
            BugNum = Double.parseDouble(yList.get(i).toString());
            Graph_column = Graph_column + "<td style =\"width:60px; height:220px;\" valign=\"bottom\"><font size=\"2\">" + yList.get(i) + "</font><table style=\"background:#0080FF;overflow:hidden;bottom:3px;height:" + Math.round((BugNum / Platform_Max) * 200) + "px;width:40px;\"><tr><td>&nbsp;</td></tr></table></td>";
            Graph_rank = Graph_rank + "<td width=\"60\"><font size=\"2\">" + xList.get(i) + "</font></td>";
        }

        Graph_Create = "<tr style=\"background:#e1e1e1;\" align=\"left\"><td style =\"width:900px\">"+Graph_Title+"</td></tr><tr style=\"background:#e1e1e1;\" align=\"left\"><td style =\"width:500px\"><table style=\"position:relative;width:200px;height:220px;border:0px solid; border-color:#e1e1e1 #e1e1e1 #e1e1e1 #e1e1e1;background:#e1e1e1;\"><tr align=\"center\">"
                + Graph_column + "</tr><tr align=\"center\">" + Graph_rank + "</tr></table></td></tr>";
        if (yList.size() ==0) {
            Graph_Create ="";
        }
            return Graph_Create;

    }
    public String ccList (String sql){
        db1 = new DBHelper(sql);//创建DBHelper对象
        String eamilList = "";

        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                eamilList =eamilList+ret.getString(1)+",";
            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eamilList;

    }
}
