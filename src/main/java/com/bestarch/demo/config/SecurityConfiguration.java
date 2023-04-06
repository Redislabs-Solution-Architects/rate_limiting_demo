package com.bestarch.demo.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAuthenticationProvider provider;

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http
//        .csrf().disable()
//        .authorizeRequests()
//        .anyRequest().authenticated()
//        .and()
//        .formLogin()
//        .loginProcessingUrl("/perform_login")
//        .defaultSuccessUrl("/dashboard");

        http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/assets/**", "/register**").permitAll()
        .antMatchers(HttpMethod.POST, "/customer**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/perform_login").permitAll()
        .defaultSuccessUrl("/prospects")
        ;

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception   {
        auth.authenticationProvider(provider);
    }

}