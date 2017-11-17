package com.dashu.datashow.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by shenzhaohua on 17/2/27.
 */

import java.sql.*;

public class DataBase {
//    public String url = "jdbc:mysql://192.168.51.99:3306/carmedbd02?useUnicode=true&characterEncoding=UTF-8";
    public String name = "com.mysql.jdbc.Driver";
    public String user = "dbviewer";
    public String password = "dbviewer";

    public Connection conn = null;
    public PreparedStatement pst = null;

    public DataBase(String sql,String dbhost,String user,String password,String tableName) {
        try {
            String url ="jdbc:mysql://"+dbhost+"/"+tableName+"?useUnicode=true&characterEncoding=UTF-8";
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
