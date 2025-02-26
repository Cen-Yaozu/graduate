package com.example.serve.filter;

import com.alibaba.fastjson.JSON;
import com.example.serve.pojo.LoginUser;
import com.example.serve.tools.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * 每个servlet只执行一次
 *
 * @author wu
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        // 放行登录请求和Swagger相关请求
        if (uri.contains("/login") ||
            uri.contains("/v3/api-docs") ||
            uri.contains("/swagger-ui") ||
            uri.contains("/doc.html") ||
            uri.contains("/webjars") ||
            uri.contains("/v2/api-docs") ||
            uri.contains("/swagger-resources")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 验证token
        //String token = request.getHeader("Authorization");
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlZWY2YzlhYzUxYTM0N2U5Yjk1MGJhZjhjZmViZWU3ZCIsInN1YiI6IntcImFjY291bnROb25FeHBpcmVkXCI6dHJ1ZSxcImFjY291bnROb25Mb2NrZWRcIjp0cnVlLFwiY3JlZGVudGlhbHNOb25FeHBpcmVkXCI6dHJ1ZSxcImVuYWJsZWRcIjp0cnVlLFwibGlzdFwiOltcIlJPTEVfU1RVREVOVFwiXSxcInBhc3N3b3JkXCI6XCJ7bm9vcH0xMjM0NTZcIixcInVzZXJcIjp7XCJpZFwiOjEsXCJwYXNzd29yZFwiOlwie25vb3B9MTIzNDU2XCIsXCJzdHVkZW50TnVtYmVyXCI6XCIyMTQwNzMwMDFcIn0sXCJ1c2VybmFtZVwiOlwiMjE0MDczMDAxXCJ9IiwiaXNzIjoid3UiLCJpYXQiOjE3NDA0ODY0MTksImV4cCI6MTc0MDU3MjgxOX0.52n7g0XA4EJwFVWl6fnalwcIulSFOfswwXLjz2cWd04";
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("token为空");
        }
        // 校验令牌
        LoginUser loginUser = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String loginUserString = claims.getSubject();
            // 把字符串转换成User对象
            loginUser = JSON.parseObject(loginUserString, LoginUser.class);
            for (GrantedAuthority authority:loginUser.getAuthorities()){
                System.out.println("校验令牌"+authority);
            }
        } catch (Exception e) {
            if (!response.isCommitted()) {
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.write("token校验失败");
                out.flush();
                out.close();
            }
            return;
        }
        // 把验证完信息放到springSecurtty的上下文
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
