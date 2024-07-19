package com.multiauthprovider.multiple_authentication_providers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   PasswordEncoder passwordEncoder) throws Exception {
        http.httpBasic(withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(customAuthenticationProvider())
                .authenticationProvider(inMemoryAuthenticationProvider(passwordEncoder));

        return http.build();
    }

    @Bean
    AuthenticationProvider customAuthenticationProvider() {
        return new CustomPalindromeAuthenticationProvider();
    }

    @Bean
    AuthenticationProvider inMemoryAuthenticationProvider(
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider inMemoryAuthenticationProvider = new DaoAuthenticationProvider();
        UserDetails userDetails = User
                .withUsername("memuser")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        inMemoryAuthenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(userDetails));
        inMemoryAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return inMemoryAuthenticationProvider;

    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            PasswordEncoder passwordEncoder) {
//        // in-memory authentication
//        DaoAuthenticationProvider inMemoryAuthenticationProvider = new DaoAuthenticationProvider();
//        UserDetails userDetails = User
//                .withUsername("memuser")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .build();
//        inMemoryAuthenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(userDetails));
//        inMemoryAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//
//        return new ProviderManager(inMemoryAuthenticationProvider, new CustomPalindromeAuthenticationProvider());
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}