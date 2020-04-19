package com.spring.demo.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.spring.demo.filter.VeriticationCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;
import java.util.Properties;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SuccessHandle successHandle;
    @Autowired
    private FailHandle failHandle;
    @Autowired
    private DataSource dataSource;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.cors().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and().formLogin().loginPage("/myLogin.html")
                .loginProcessingUrl("/login").successHandler(successHandle).failureHandler(failHandle).permitAll();*/
        http.cors().disable()
                .authorizeRequests().antMatchers("/app/api/**").hasRole("USER")
                .antMatchers("/admin/api/**").hasRole("ADMIN")
                .antMatchers("/user/api/**","/captcha.jpg").permitAll()
                .anyRequest().authenticated().and().formLogin().loginPage("/login").
                loginProcessingUrl("/auth/form").permitAll().failureHandler(failHandle);
        http.addFilterBefore(new VeriticationCodeFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //inMemoryAuthentication 从内存中获取

        //注入userDetailsService的实现类
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("user").password(new BCryptPasswordEncoder().encode("123456")).roles("USER");
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("admin").password(new BCryptPasswordEncoder().encode("123456")).roles("ADMIN");
    }*/
    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        //InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        if(!manager.userExists("user"))
            manager.createUser(User.withUsername("user").password("123456").roles("USER").build());
        if(!manager.userExists("admin"))
            manager.createUser(User.withUsername("admin").password("123456").roles("ADMIN").build());
        return manager;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public Producer producer(){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","150");
        properties.setProperty("kaptcha.image.height","50");
        properties.setProperty("kaptcha.textproducer.char","0123456789");
        properties.setProperty("kaptcha.textproducer.length","4");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}

