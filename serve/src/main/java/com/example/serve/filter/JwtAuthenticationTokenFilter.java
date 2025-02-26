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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
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
        String token = request.getHeader("Authorization");
        // 如果请求头中没有token，也可以从请求参数中获取
        if (!StringUtils.hasText(token)) {
            token = request.getParameter("token");
        }

        if (!StringUtils.hasText(token)) {
            sendErrorResponse(response, 401, "未提供token或token为空");
            return;
        }

        // 去掉Bearer前缀如果存在
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证token格式
        if (!isValidJwtFormat(token)) {
            sendErrorResponse(response, 401, "无效的token格式");
            return;
        }

        // 校验令牌
        LoginUser loginUser = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String loginUserString = claims.getSubject();
            if (loginUserString == null) {
                sendErrorResponse(response, 401, "token中未包含用户信息");
                return;
            }
            // 把字符串转换成User对象
            loginUser = JSON.parseObject(loginUserString, LoginUser.class);
            for (GrantedAuthority authority : loginUser.getAuthorities()) {
                System.out.println("校验令牌" + authority);
            }
        } catch (Exception e) {
            sendErrorResponse(response, 401, "token校验失败: " + e.getMessage());
            return;
        }

        // 把验证完信息放到springSecurtty的上下文
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null,
                loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private boolean isValidJwtFormat(String token) {
        if (token == null) {
            return false;
        }
        // JWT token 应该包含两个点号，分隔成三部分
        return token.split("\\.").length == 3;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        if (!response.isCommitted()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(status);
            PrintWriter out = response.getWriter();
            out.write(String.format("{\"code\":%d,\"msg\":\"%s\"}", status, message));
            out.flush();
            out.close();
        }
    }
}
