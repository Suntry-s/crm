package com.sun.crm.web.filter;

import com.sun.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("进入验证登录过的过滤器");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String path = request.getServletPath();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            //放行资源
            chain.doFilter(req,resp);
        }else {
            //验证是否登录过
            if (user!=null ) {
                chain.doFilter(req,resp);
            }else {
                //重定向到登录页面
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }





    }
}
