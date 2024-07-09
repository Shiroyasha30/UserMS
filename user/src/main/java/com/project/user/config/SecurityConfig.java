package com.project.user.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.project.user.filter.JwtFilter;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtFilter jwtFilter;
	
//	@Autowired 
//	private DataSource dataSource ;
	
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		Function<String, String> encoder = str -> passwordEncoder().encode(str);
		
//		List<UserDetails> userDetailsList = userRepository.findAll().stream()
//				.map(user -> User.builder().passwordEncoder(encoder).username(user.getUsername()).password(user.getPassword()).build()).toList();
		List<UserDetails> userDetailsList = Arrays.asList(
															User.builder().passwordEncoder(encoder).username("user").password("pswd").build(),
															User.builder().passwordEncoder(encoder).username("dummy").password("pswd").build()
														);
		
		return new InMemoryUserDetailsManager(userDetailsList);
//	}
	
//	@Bean 
//	public JdbcUserDetailsManager userDetailsService() {
////		Function<String, String> encoder = str -> passwordEncoder().encode(str);
////		
//////		List<UserDetails> userDetailsList = userRepository.findAll().stream()
//////				.map(user -> User.builder().passwordEncoder(encoder).username(user.getUsername()).password(user.getPassword()).build()).toList();
////		List<UserDetails> userDetailsList = Arrays.asList(
////															User.builder().passwordEncoder(encoder).username("user").password("pswd").build(),
////															User.builder().passwordEncoder(encoder).username("dummy").password("pswd").build()
////														);
//		
//		
//		return new JdbcUserDetailsManager(dataSource);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        
		AntPathRequestMatcher matcher = new AntPathRequestMatcher("/authenticate/**");
        http.authorizeHttpRequests((requests) -> requests.requestMatchers(matcher).permitAll().anyRequest().authenticated());
        http.authenticationManager(authenticationManager);
		http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
		http.csrf((csrf) -> csrf.disable());
		http.headers((headers) -> headers.frameOptions(frameOptions -> frameOptions.disable()));
//		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
	
	@Bean
	public AuthenticationManager authenticationManager(InMemoryUserDetailsManager userDetailsService) {
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService);
		daoProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(daoProvider);
	}
}
