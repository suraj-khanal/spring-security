package com.suraj.springsecurity.basic;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class BasicAuthSecurityConfiguration {

	@Bean
	SecurityFilterChain SecurityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
		http.authorizeHttpRequests(auth -> {		
			auth
				.requestMatchers(mvcMatcherBuilder.pattern( "/users")).hasRole("USER")
				.requestMatchers(mvcMatcherBuilder.pattern( "/admin/**")).hasRole("ADMIN")
				.anyRequest().authenticated();
		});
		
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// http.formLogin();
		http.httpBasic();
		http.csrf().disable();
		http.headers().frameOptions().sameOrigin();
		return http.build();

	}
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		var user = User.withUsername("suraj")
//				.password("{noop}hello")
//				.roles("USER")
//				.build();
//				
//		var admin = User.withUsername("admin")
//				.password("{noop}hello")
//				.roles("ADMIN")
//				.build();
//				
//				
//		return new InMemoryUserDetailsManager(user, admin);
//	}
	
	@Bean
	public DataSource dataSource() {
		
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
				.build();
	}
	
	
	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource) {
		
		var user = User.withUsername("suraj")
				//.password("{noop}hello")
				.password("hello")
				.passwordEncoder(str -> passwordEncoder().encode(str))
				.roles("USER")
				.build();
				
		var admin = User.withUsername("admin")
				//.password("{noop}hello")
				.password("hello")
				.passwordEncoder(str -> passwordEncoder().encode(str))
				.roles("ADMIN", "USER")
				.build();
		
		var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.createUser(user);
		jdbcUserDetailsManager.createUser(admin);
				
		return jdbcUserDetailsManager;
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {	
		return new BCryptPasswordEncoder();
	}
	
}






















