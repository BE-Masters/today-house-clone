package com.example.be_study.security;

import com.example.be_study.common.jwt.JwtAuthenticationFilter;
import com.example.be_study.common.jwt.JwtService;
import com.example.be_study.common.jwt.JwtTokenUtil;
import com.example.be_study.service.oauth.OauthServerTypeConverter;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtService jwtService;

    private final static String[] PERMIT_ALL = {
            "/api/v1/sign-up/**",
            "/login",
            "/api/v1/oauth/kakao.html",
            "/oauth2/callback/kakao",
            "/api/v1/mail", "/api/v1/mail/**"
    };


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.PATCH.name()
                )
                .allowCredentials(true)
                .exposedHeaders("*");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new OauthServerTypeConverter());
    }

    public WebSecurityConfig(JwtTokenUtil jwtTokenUtil, JwtService jwtService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtService = jwtService;
    }

    public HttpSecurity addExceptionHandling(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.exceptionHandling((authenticationManager) -> authenticationManager
                .authenticationEntryPoint(new WebAuthenticationEntryPoint())
        );
    }

    @Bean
    @Profile("local")
    public SecurityFilterChain localSecurityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안 함(토큰 방식 사용)
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests // 권한 설정
                        .requestMatchers(PERMIT_ALL).permitAll()
                        //.anyRequest().hasAnyRole("BASIC_USER", "ADMIN")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil, jwtService), UsernamePasswordAuthenticationFilter.class);

        return addExceptionHandling(http).build();
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안 함(토큰 방식 사용)
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests // 권한 설정
                        .requestMatchers(PERMIT_ALL).permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil, jwtService), UsernamePasswordAuthenticationFilter.class);
        return addExceptionHandling(http).build();
    }

}
