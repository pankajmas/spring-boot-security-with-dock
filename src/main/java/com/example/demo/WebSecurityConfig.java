package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.jwt.AuthEntryPointJwt;
import com.example.demo.security.jwt.AuthTokenFilter;
import com.example.demo.security.jwt.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsServiceImpl userDetailsService;

	private final AuthEntryPointJwt unauthorizedHandler;

	@Bean
	AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthEntryPointJwt unauthorizedHandler) {
		this.userDetailsService = userDetailsService;
		this.unauthorizedHandler = unauthorizedHandler;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeRequests(requests -> requests
                .antMatchers("/api/auth/**").permitAll().antMatchers("/api/users/**")
                .hasAnyRole("USER", "MODERATOR", "ADMIN").antMatchers("/api/test/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/signin", "/api/signup").hasAnyRole("USER", "MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/singleuser/**").hasAnyRole("USER", "MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/patch_user/**").hasAnyRole("USER", "MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/users/update_user/**").hasAnyRole("USER", "MODERATOR", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/users_delete/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/fetchall").hasRole("ADMIN"));

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
