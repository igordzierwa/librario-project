package com.agh.librario;

import com.agh.librario.Auth.CustomLogoutSuccessHandler;
import com.agh.librario.Auth.JsonObjectAuthenticationFilter;
import com.agh.librario.Auth.RestAuthenticationFailureHandler;
import com.agh.librario.Auth.RestAuthenticationSuccessHandler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.cors().and().csrf().disable().httpBasic().disable().
                authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers(HttpMethod.GET, "/rest/test_tylko_zalogowany").hasAuthority("admin")
                .antMatchers(HttpMethod.GET, "/welcome").hasAuthority("normal_user")
                .antMatchers("/rest/**").permitAll()
                .antMatchers("/resources/**", "/register").permitAll()
                .antMatchers(HttpMethod.GET, "/rest/books/**").anonymous()
                .antMatchers(HttpMethod.GET, "/rest/login").anonymous()
                .antMatchers(HttpMethod.GET, "/rest/books").anonymous()
                .antMatchers(HttpMethod.POST, "/rest/login").anonymous()
                .antMatchers(HttpMethod.GET, "/login").anonymous()
                .antMatchers(HttpMethod.POST, "/login").anonymous()
                .antMatchers("/admin/users/**").permitAll()
                .antMatchers("/admin/**").hasAuthority("admin")
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/check/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/**").permitAll()

                .anyRequest().authenticated()
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)            //custom auth
                .exceptionHandling()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint())                         //do wystawiania 403/401
                .and()
                .logout()
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));                                        //skąd może przyjść zapytanie
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE", "PATCH"));       //metody jakie mogą przyjść
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "X-Requested-With",
                "accept", "Origin", "Access-Control-Request-Method",
                "Access-Control-Request-Headers"));
        configuration.addExposedHeader("Access-Control-Allow-Origin");                              //jakie headery ma zwrócić
        configuration.addExposedHeader("Access-Control-Allow-Credentials");
        configuration.setAllowCredentials(true);                                                    //włączenie tokenów
        configuration.setMaxAge(3600L);                                                             //czas życia tokena
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JsonObjectAuthenticationFilter authenticationFilter() throws Exception {
        JsonObjectAuthenticationFilter filter = new JsonObjectAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new RestAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new RestAuthenticationFailureHandler());
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }


}

