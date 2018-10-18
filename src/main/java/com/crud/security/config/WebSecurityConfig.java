package com.crud.security.config;

import com.crud.model.User;
//import com.crud.security.config.auth.AuthenticationProvider;
import com.crud.security.service.UserDetailsServiceImpl;
import com.crud.service.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Principal;

@Configuration
@EnableWebSecurity
//@EnableOAuth2Sso

//@SpringBootApplication
//@RestController
//@ComponentScan({"com.crud.service","com.crud.security"})

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler myAuthenticationFailureHandler(){
        return new MySimpleUrlAuthenticationFailureHandler();
    }

    @Bean
    public LogoutSuccessHandler myLogoutSuccessHandler(){
        return new MySimpleLogoutSuccessHandler();
    }

//    @Autowired
//    private AuthenticationProvider authenticationProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private OAuth2UserService oAuth2UserService;


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
//                .authenticationProvider(authenticationProvider)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                ;
    }

//    @Bean
//    public FilterRegistrationBean oauth2ClientFilterRegistration(
//            OAuth2ClientContextFilter filter) {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(filter);
//        registration.setOrder(-100);
//        return registration;
//    }
//    @Bean
//    public AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
//
//    }

//    @RequestMapping("/main")
//    public Principal user(Principal principal) {
//        return principal;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/login","/login-error","/logout","/console/**")
//                .permitAll()
//                .anyRequest().authenticated()
//                .and().formLogin()
//                .loginPage("/login")


//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/","login","/logout").permitAll()
//                .antMatchers("/","/login","/logout").permitAll()
////                .antMatchers("/admin/**").hasRole("admin")
////                .antMatchers("/user/**").hasAnyRole("admin","user")
//                .anyRequest().authenticated()
//				.and()
//                .formLogin()
//                .loginPage("/login")
////                .loginProcessingUrl("/loginAction")
//////                .successHandler(myAuthenticationSuccessHandler())
//////                .failureHandler(myAuthenticationFailureHandler())
////                .and()
////                .logout()
////                .logoutUrl("/logout")
////                .logoutSuccessUrl("/login?logout")
////                .invalidateHttpSession(true)
////                .logoutSuccessHandler(myLogoutSuccessHandler())
////                .permitAll()
//        ;

//version worked ====
                http
                .csrf().disable()
//                .anonymous().
                .authorizeRequests().antMatchers("/","/login","/logout").permitAll()
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/user/**").hasAnyRole("admin","user")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginAction")
                .successHandler(myAuthenticationSuccessHandler())
                .failureHandler(myAuthenticationFailureHandler())
                .and()
                .logout().logoutUrl("/logout").logoutSuccessHandler(myLogoutSuccessHandler()).permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied")

                .and()
                .oauth2Login()
                .loginPage("/login")
                .authorizationEndpoint()
                .baseUri("/oauth/login")
                .and()
                .successHandler(myAuthenticationSuccessHandler())
                .failureHandler(myAuthenticationFailureHandler())
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                ;
//version worked ====

//        http.headers().frameOptions().disable();

//        http.apply(new SpringSocialConfigurer())
//                .signupUrl("/signup");

//                .antMatchers("/*","/login*")
//                .authorizeRequests()
//                .antMatchers("/login**","/webjars/**","/error**").permitAll()
//                .antMatchers("/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .logout().logoutSuccessUrl("/login").permitAll();
//                .and()
//                .authorizeRequests().antMatchers("/login**").permitAll()
//                .and()
//                .oauth2Login();
//                .loginPage("/login");
//                .loginProcessingUrl("/loginAction")
//                .successHandler(myAuthenticationSuccessHandler())
//                .permitAll()
//                .failureHandler(myAuthenticationFailureHandler())
//                .permitAll()
//                .and()
//                .logout().logoutSuccessHandler(myLogoutSuccessHandler()).permitAll()
//                .and()
//                .exceptionHandling().accessDeniedPage("/accessDenied");



//                .antMatchers("/admin/**").hasRole("admin")
//                .antMatchers("/main/**").hasAnyRole("admin","user")
//                .and()
//                .authorizeRequests().antMatchers("/login**").permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/loginAction")
//                .successHandler(myAuthenticationSuccessHandler())
//                .permitAll()
//                .failureHandler(myAuthenticationFailureHandler())
//                .permitAll()
//                .and()
//                .logout().logoutSuccessHandler(myLogoutSuccessHandler()).permitAll()
//                .and()
//                .exceptionHandling().accessDeniedPage("/accessDenied");
    }



//    @Bean
//    public PrincipalExtractor principalExtractor(UserService userService) {
//        return map -> {
//            String login = (String) map.get("email");
//            User newUser = null;
//            try {
//                ResponseEntity<User> entity = userService.getByLogin(login);
//                newUser = entity.getBody();
//            }
//            catch (HttpClientErrorException ex) {
//                if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
//                    newUser = new User(
//                            (String) map.get("name"),
//                            (String) map.get("email"),
//                            "",
//                            ""
//                    );
//                    userService.save(newUser);
//                }
//            }
//            return newUser;
//        };
//    }
}