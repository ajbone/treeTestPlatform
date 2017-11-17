package com.dashu.datashow.service;

/**
 * Created by shenzhaohua on 16/12/7.
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

    /**
     * @category 退出登录的Servlet,注销
     * @author Bird
     */
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);//防止创建Session
        if(session == null){
            response.sendRedirect("/pages/index.jspa");
            return;
        }
        session.removeAttribute("uid");
        session.removeAttribute("userNick");
        session.removeAttribute("discription");
        session.removeAttribute("email");
        session.removeAttribute("groupname");
        session.removeAttribute("photo");
        response.sendRedirect("/pages/index.jspa");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
