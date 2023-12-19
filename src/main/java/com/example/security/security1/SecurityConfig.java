package com.example.security.security1;

import com.example.security.properties.MySecurityProperties;
import com.example.security.model.UserRole;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * FORM 방식
 * 서버가 기동 될 때 spring security 초기화 작업 및 보안 설정
 * 별도의 설정을 하지 않아도 웹 보안 가능이 현재 시스템에 연동되어 작동
 *
 */
@Log4j2
@Configuration
@EnableWebSecurity(debug = true)
@EnableConfigurationProperties(MySecurityProperties.class)
@RequiredArgsConstructor
public class SecurityConfig {

    private final MySecurityProperties securityProperties;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
            .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // authorization + login
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login").permitAll()
                .requestMatchers("/users/**").hasRole(UserRole.USER.name())
                .requestMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
                .requestMatchers("/logout").permitAll()
                .requestMatchers("/blog/**").permitAll()
                .anyRequest().authenticated()
            )
            // loginPage 를 구현할 경우, default security login 페이지 사용 불가
            .formLogin(formLogin -> formLogin
                .loginPage("/login").permitAll()
            );

        // logout
        http
            .logout(formLogout -> formLogout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .addLogoutHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession();
                    session.invalidate();
                    try {
                        response.sendRedirect("/login");
                    } catch (IOException e) {
                        log.error(e);
                    }
                })
                .deleteCookies("remember-me")
            );
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User
            .withUsername(securityProperties.getName())
            .password(securityProperties.getPassword())
            .authorities(new SimpleGrantedAuthority(securityProperties.getRole().name()))
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}
