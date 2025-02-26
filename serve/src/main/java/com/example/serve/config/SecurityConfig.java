package com.example.serve.config;

import com.example.serve.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author wu
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,proxyTargetClass = true)
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

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/doc.html",
                        "/webjars/**",
                        "/v2/api-docs/**",
                        "/swagger-resources/**")
                        .permitAll()
                .requestMatchers("/api/student/**").hasRole("STUDENT")
                .requestMatchers("/api/personal/**").hasRole("STUDENT")
                .requestMatchers("/api/dormitory/**").hasRole("STUDENT")
                .requestMatchers("/api/payment/**").hasRole("STUDENT")
                .requestMatchers("/api/class/**").hasRole("STUDENT")
                .requestMatchers("/api/arrive/**").hasRole("STUDENT")
                .anyRequest()
                .authenticated()
        );

        // 配置过滤器的执行顺序
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
