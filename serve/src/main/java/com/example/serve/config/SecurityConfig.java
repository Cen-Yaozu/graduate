package com.example.serve.config;

import com.example.serve.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author wu
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfig {

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 关闭csrf
        http.csrf().disable();
        http.cors();

        // 添加调试日志，打印请求路径和权限信息
        System.out.println("配置Security过滤链...");
        
        // 设置session管理策略为无状态（JWT不需要session）
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests(auth -> {
            try {
                auth.requestMatchers("/login",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/doc.html",
                        "/webjars/**",
                        "/v2/api-docs/**",
                        "/swagger-resources/**",
                        // 添加激活相关的API路径，使它们不需要认证
                        "/api/verify-student",
                        "/api/send-verification",
                        "/api/activate-account",
                        "/api/verify-code",
                        "/api/reset-password",
                        "/api/send-verify-code",
                        // 添加所有可能的静态资源访问路径
                        "/uploads/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated();

                System.out.println("Security权限配置完成，静态资源路径 '/uploads/**' 已配置为公开访问");
            } catch (Exception e) {
                System.out.println("Security配置异常: " + e.getMessage());
                e.printStackTrace();
            }
        });

        // 添加JWT过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
