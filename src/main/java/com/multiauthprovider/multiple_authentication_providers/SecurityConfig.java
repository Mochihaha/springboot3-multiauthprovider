package com.multiauthprovider.multiple_authentication_providers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean // no @Override because not extending another class
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class); //Stackoverflow error
        auth.inMemoryAuthentication()
                .withUser("memuser")
                .password(passwordEncoder.encode("password"))
                .roles("USER");

        auth.authenticationProvider(new CustomPalindromeAuthenticationProvider());
        return auth.build();
    }

//     Error creating authenticationManager bean (name of Spring beans follow the method name annotated with @Bean)
//    @Bean // no @Override because not extending another class
//    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception { // Cannot apply these customisations to already built object
//        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        auth.inMemoryAuthentication()
//                .withUser("memuser")
//                .password(passwordEncoder.encode("password"))
//                .roles("USER");
//
//        auth.authenticationProvider(new CustomPalindromeAuthenticationProvider());
//        return auth.build();
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}