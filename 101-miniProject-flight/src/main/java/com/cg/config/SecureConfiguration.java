package com.cg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecureConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                                .password(encoder.encode("admin"))
                                .roles("ADMIN")
                                .build();

        UserDetails user = User.withUsername("user")
                               .password(encoder.encode("user"))
                               .roles("USER")
                               .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    // Role-based Success Handler logic
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            var roles = authentication.getAuthorities();
            String redirectUrl = "/home/user-index"; // Default for USER
            
            for (var role : roles) {
                if (role.getAuthority().equals("ROLE_ADMIN")) {
                    redirectUrl = "/home/admin-index"; // Redirect for ADMIN
                    break;
                }
            }
            response.sendRedirect(request.getContextPath() + redirectUrl);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. Allow public access to login paths
            		.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
            		
                .requestMatchers("/home/", "/home/login", "/public/**", "/css/**").permitAll()
                
                // 2. Role-specific URL protection
                .requestMatchers("/home/admin-index", "/home/create", "/home/delete/**").hasRole("ADMIN")
                .requestMatchers("/home/user-index").hasRole("USER")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/home/login")         
                .loginProcessingUrl("/home/login") // This MUST match th:action in HTML
                .successHandler(myAuthenticationSuccessHandler()) // Use the Role-based redirect
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/home/logout")
                .logoutSuccessUrl("/home/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
