package com.project.together.config;

import com.project.together.config.jwt.JwtTokenFilter;
import com.project.together.config.jwt.JwtTokenProvider;
import com.project.together.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {
    SecurityService service;
    JwtTokenFilter filter;
    JwtTokenProvider provide;

    public SecurityConfig(JwtTokenFilter filter, JwtTokenProvider provide, SecurityService service) {
        this.filter = filter;
        this.provide = provide;
        this.service = service;
    }

    /*
     * 비밀번호를 암호화하는 Bean 입니다.
     * 해당 인코더를 설정하지 않으면 만들어준 시큐리티서비스(service)에서 비밀번호 암호화 할 때 오류가 발생 합니다.
     * */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * 따로 무슨 행동을 하지 않아도 허용 해 주는 주소 모음 입니다.
     * */
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/");
    }

    /*
     * 시큐리티설정, jwt 설정을 하여 줍니다.
     * */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //#1. 권한이 필요한 대상입니다.
        http.authorizeRequests()
                .antMatchers("/test").authenticated()
                .antMatchers("/test2").authenticated()
        ;

        //#2. 세션을 쓰지 않습니다.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //#3. JWT 필터를 쓰겠다고 설정하여 줍니다.
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        //#4. 사용자의 로그인 행동에 대한 정의 입니다.
        http.formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, auth) -> {  //로그인 성공시 행동을 정의 합니다.
                    String ip = request.getRemoteAddr();
                    String user_id = auth.getName();

                    System.out.println("login ok : " + ip + "" + user_id);

                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Content-Type", "application/download; UTF-8");
                    String token = provide.tokenIssuance(user_id, auth.getAuthorities().stream().map(arg -> arg.getAuthority()).collect(Collectors.toList()));
                    response.getWriter().write("{\"result\" : \"" + token + "\" }");

                })
                .failureHandler((request, response, auth) -> {  //로그인 실패시 행동을 정의 합니다.
                    String ip = request.getRemoteAddr();
                    String user_id = request.getParameter("username");

                    System.out.println("login fail : " + ip + "" + user_id);

                    response.sendRedirect("/");
                }).permitAll();

        //#5.csrf 설정을 off 합니다.
        http.csrf().disable();

        return http.build();
    }


  /*  @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable(); //일반 사용자에 대해 Session을 저장하지 않으므로 csrf을 disable 처리함.

        http
                .authorizeRequests()
                .antMatchers("/admin").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/swagger-ui.html").hasRole("ADMIN")
                .antMatchers("/schedule/insert", "/schedule/update", "/schedule/delete").hasRole("ADMIN")
                .antMatchers("/comment/insert", "/comment/update", "/comment/delete").hasRole("ADMIN")
                .anyRequest().permitAll();

        http
                .formLogin()
                .loginPage("/admin")
                .loginProcessingUrl("/admin/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);

        http
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/admin")
                .invalidateHttpSession(true);
*/
}
