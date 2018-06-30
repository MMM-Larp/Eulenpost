package net.gothax.larp.larpweb.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String LOGIN = "/login";
    private static final String ACCESS_DENIED = "/access-denied";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
            .authorizeRequests()
                .antMatchers("/", LOGIN, "/login/auth", ACCESS_DENIED, "/resource/**", "/register", "/privacy").permitAll()
                .antMatchers("/**", "/admin/**", "/mapping/**").hasAnyRole("ADMIN")
                .and()
            .formLogin()
                .loginPage(LOGIN)
                .failureUrl(ACCESS_DENIED)
                .defaultSuccessUrl("/admin")
                .and()
            .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl(LOGIN)
                .and()
            .exceptionHandling()
                .accessDeniedPage(ACCESS_DENIED)
                .and()
            .csrf()
                .disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        final String ROLE_ADMIN = "ADMIN";
        auth.inMemoryAuthentication()
                .withUser("manni")
                .password(passwordEncoder().encode("manfred"))
                .roles(ROLE_ADMIN);
        auth.inMemoryAuthentication()
                .withUser("gryffindor")
                .password(passwordEncoder().encode("gewinnt"))
                .roles(ROLE_ADMIN);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
