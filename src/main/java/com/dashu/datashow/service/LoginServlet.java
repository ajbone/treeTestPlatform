package com.dashu.datashow.service;

/**
 * Created by shenzhaohua on 16/12/5.
 */
import java.io.IOException;
import java.util.Arrays;
import com.dashu.datashow.controller.packageData;
import com.dashu.datashow.util.TimeUtil;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet  extends HttpServlet {
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
        //从页面获取参数
        String username = request.getParameter("username");
        char[] password = request.getParameter("pwd").toCharArray();
        String remember = request.getParameter("remember");
        String redirect_url = request.getParameter("redirect_url");
        String userInfo[] = userInfo(username).split("\\|");

        if (!exists(username)){
            response.sendRedirect("/pages/fail.jspa?username=notExist");
        }else if (check(username,password)){
            request.getSession().setAttribute("uid", userInfo[0]);
            request.getSession().setAttribute("userNick", userInfo[1]);
            request.getSession().setAttribute("discription", userInfo[3]);
            request.getSession().setAttribute("email", userInfo[4]);
            request.getSession().setAttribute("groupname", userInfo[5]);
            request.getSession().setAttribute("photo", userInfo[6]);
            response.sendRedirect("/pages/success.jspa?redirect_url="+redirect_url+"");
        }else{
            response.sendRedirect("/pages/fail.jspa?pwd=fail");
        }

    }

    /*
    * 判断指定用户名的用户是否存在
    *
    * @return：如果存在返回true，不存在或者查询失败返回false
    */
    public static boolean exists(String username) {
        int userExist = 0;
        packageData doPackage = new packageData();

        String sql = "select count(*) from user where username = '" + username + "';";// 定义查询语句
        userExist = doPackage.selectPackage(sql);
        try {
            if (userExist > 0) {
                return true;// 返回true
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;// 如果发生异常返回false
    }

    /*
    * 验证用户名和密码是否正确 使用Commons Lang组件转义字符串避免SQL注入
    *
    * @return：如果正确返回true，错误返回false
    */
    public static boolean check(String username, char[] password) {
        String result ="";
        username = StringEscapeUtils.escapeSql(username);// 将用户输入的用户名转义
        packageData doPackage = new packageData();
        String sql = "select password from user where username = '" + username + "';";// 定义查询语句
        try {
            result = doPackage.selectStatus(sql);
            char[] queryPassword = result.toCharArray();// 将查询到得密码转换成字符数组
            if (Arrays.equals(password, queryPassword)) {// 如果密码相同则返回true
                Arrays.fill(password, '0');// 清空传入的密码
                Arrays.fill(queryPassword, '0');// 清空查询的密码
                return true;
            } else {// 如果密码不同则返回false
                Arrays.fill(password, '0');// 清空传入的密码
                Arrays.fill(queryPassword, '0');// 清空查询的密码
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;// 如果发生异常返回false
    }


    /*
    * 验证用户名和密码是否正确 使用Commons Lang组件转义字符串避免SQL注入
    *
    * @return：如果正确返回true，错误返回false
    */
    public String userInfo(String username) {
        String result ="";
        packageData doPackage = new packageData();
        String sql = "select uid,usernick,password,discription,email,groupname,photo,username from user where username = '" + username + "';";// 定义查询语句
        try {
            result = doPackage.selectMachineInfo(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    * 保存用户输入的注册信息
    *
    * @return：如果保存成功返回true，保存失败返回false
    */
//    public static boolean save(User user) {
//        QueryRunner runner = new QueryRunner();// 创建QueryRunner对象
//        String sql = "insert into tb_user (username, password, email) values (?, ?, ?);";// 定义查询语句
//        Connection conn = getConnection();// 获得连接
//        Object[] params = { user.getUsername(), user.getPassword(), user.getEmail() };// 获得传递的参数
//        try {
//            int result = runner.update(conn, sql, params);// 保存用户
//            if (result > 0) {// 如果保存成功返回true
//                return true;
//            } else {// 如果保存失败返回false
//                return false;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            DbUtils.closeQuietly(conn);// 关闭连接
//        }
//        return false;// 如果发生异常返回false
//    }
}

