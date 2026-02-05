package com.cg.config;

import com.cg.service.CustomUserDetailsService;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;

	public SecurityConfig(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	// Password encoder
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Authentication provider
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider auth = new DaoAuthenticationProvider(userDetailsService);
	    // Use setUserDetailsService instead of setUserDetailsPasswordService
	    auth.setPasswordEncoder(passwordEncoder());
	    return auth;
	}

	// Security rules
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).authenticationProvider(authenticationProvider())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/login","/register-user","/register-doctor","/doctor-registration", "/css/**").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/doctor/**").hasRole("DOCTOR")
						.requestMatchers("/patient/**").hasRole("PATIENT").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/perform_login")
						.defaultSuccessUrl("/redirect", true).failureUrl("/login?error=true").permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true"));

		return http.build();
	}
}
