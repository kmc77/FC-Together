package com.project.together.config;

import com.project.together.config.handler.CustomAccessDeniedHandler;
import com.project.together.config.handler.CustomAuthenticationEntryPoint;
import com.project.together.config.jwt.JwtAuthenticationFilter;
import com.project.together.config.jwt.JwtAuthorizationFilter;
import com.project.together.config.oauth.OAuth2LoginSuccessHandler;
import com.project.together.config.oauth.PrincipalOauth2UserService;
import com.project.together.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserMapper userMapper;

    @Autowired
    @Lazy
    private PrincipalOauth2UserService principalOauth2UserService;
    @Autowired
    @Lazy
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // JwtAuthenticationFilter를 빈으로 등록하지 않음
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManagerBean());
    }

    // JwtAuthorizationFilter를 빈으로 등록하지 않음
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(userMapper);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/**", "/main.html", "/css/**", "/js/**", "/img/**", "/webjars/**", "/static/**", "/**/*.js", "/**/*.css", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.gif").permitAll()
                .antMatchers("/user/**", "/club/**", "/team/**", "/history/**", "/match/**", "/admin/**", "/media/**", "/management/**", "/my/**", "/image/**").permitAll()
                /*.antMatchers("/media/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                .antMatchers("/management/**").hasAnyRole("USER", "MANAGER", "ADMIN")*/
                .antMatchers("/oauth2/authorization/**").permitAll()
                .antMatchers("/error").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler);
    }
}
