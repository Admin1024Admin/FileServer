package xin.l024.fileserver.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/login","/static/**","/bootstrap3.3.7/**","/login-error","/error","/css/**","/images/**").permitAll()//这些都可以无权限访问
                .antMatchers("/file/**").hasRole("admin")//users请求下的必须有admin角色才可以访问
                .and()
                .formLogin().usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/") //登录成功返回的请求
                .loginPage("/login")  //跳转登录页面的请求
                .failureUrl("/login-error");//登录失败的请求
        //退出返回的页面
        http.logout().logoutSuccessUrl("/");
    }

    /**
     * 认证信息管理
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("admin");
    }
}