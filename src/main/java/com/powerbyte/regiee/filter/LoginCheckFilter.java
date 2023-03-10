package com.powerbyte.regiee.filter;

import com.alibaba.fastjson.JSON;
import com.powerbyte.regiee.common.BeanContext;
import com.powerbyte.regiee.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author 金刚不坏凤凰城
 * @Date 2022/8/6 17:03
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，用于判断请求路径是否匹配
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取本次请求的URL
        String requestURI = request.getRequestURI();

        log.info("拦截到请求:{}", requestURI);

        //不需要过滤的路径
        String[] notFilter = {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

        //判断是否需要过滤
        boolean check = check(notFilter, requestURI);

        if (check) {
            log.info("放行请求:{}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //判断登录状态
        if (request.getSession().getAttribute("employee") != null) {
            log.info("已登录用户,用户ID:{}", request.getSession().getAttribute("employee"));

            Long employeeId = (Long) request.getSession().getAttribute("employee");
            BeanContext.setCurrentId(employeeId);

            filterChain.doFilter(request, response);
            return;
        }

        //如果没登陆，以输出流的方式返回结果
        log.info("未登录用户,返回结果:{}", JSON.toJSONString(R.error("未登录")));
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    public boolean check (String[] urls, String requestURI) {
        for (String s : urls) {
            if (PATH_MATCHER.match(s, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
