package com.project.loancalculator.loan_eligibility_calculator.security;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http.csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (not recommended in production)
         .authorizeHttpRequests(auth -> auth
        	.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() 
             .requestMatchers("/admin/**").hasRole("ADMIN") // Access control for admin
             .requestMatchers("/loans/**").hasAnyRole("ADMIN", "USER")
             // Access control for loans
             .anyRequest().authenticated() // All other endpoints require authentication
         )
         .httpBasic(Customizer.withDefaults()) // Enable HTTP Basic Authentication
         .formLogin(Customizer.withDefaults()) // Enable form-based login
         .sessionManagement(session -> session
             .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
         );
	    return http.build();
	}
	   @Bean
	    public InMemoryUserDetailsManager userDetailsService() {
	        UserDetails admin = User.builder()
	                .username("admin")
	                .password(passwordEncoder().encode("admin123"))
	                .roles("ADMIN")
	                .build();

	        UserDetails user = User.builder()
	                .username("user")
	                .password(passwordEncoder().encode("user123"))
	                .roles("USER")
	                .build();

	        return new InMemoryUserDetailsManager(admin, user);
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	   


}
