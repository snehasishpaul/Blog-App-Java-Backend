package com.snehasish.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.snehasish.blog.security.CustomUserDetailService;
import com.snehasish.blog.security.JWTAuthenticationEntryPoint;
import com.snehasish.blog.security.JWTAuthenticationFilter;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity
public class SecurityConfig {

	public static final String[] PUBLIC_URLS = { "/api/v1/auth/**", "/v3/api-docs", "/v2/api-docs",
			"/swagger-resources/**", "/swagger-ui/**", "/webjars/**" };

	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Autowired
	private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationFilter;

	// WebSecurityConfigurerAdapter deprecated
	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * 
	 * http.csrf().disable().authorizeHttpRequests().antMatchers(PUBLIC_URLS).
	 * permitAll().antMatchers(HttpMethod.GET)
	 * .permitAll().anyRequest().authenticated().and().exceptionHandling()
	 * .authenticationEntryPoint(this.jwtAuthenticationEntryPoint).and().
	 * sessionManagement() .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	 * 
	 * http.addFilterBefore(this.jwtAuthenticationFilter,
	 * UsernamePasswordAuthenticationFilter.class); }
	 * 
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception {
	 * auth.userDetailsService(this.customUserDetailService).passwordEncoder(this.
	 * passwordEncoder()); }
	 * 
	 * @Bean
	 * 
	 * @Override public AuthenticationManager authenticationManagerBean() throws
	 * Exception { return super.authenticationManagerBean(); }
	 * 
	 */

	// Configuration without WebSecurityConfigurerAdapter (latest)
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		/*
		 * SPRING BOOT 2.7.12 Version
		 * 
		 * http.csrf().disable().authorizeHttpRequests().antMatchers(PUBLIC_URLS).
		 * permitAll().antMatchers(HttpMethod.GET)
		 * .permitAll().anyRequest().authenticated().and().exceptionHandling()
		 * .authenticationEntryPoint(this.jwtAuthenticationEntryPoint).and().
		 * sessionManagement() .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		 * 
		 * http.addFilterBefore(this.jwtAuthenticationFilter,
		 * UsernamePasswordAuthenticationFilter.class);
		 * 
		 * http.authenticationProvider(daoAuthenticationProvider());
		 * 
		 * DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();
		 * 
		 * return defaultSecurityFilterChain;
		 */

		/* SPRING BOOT 3.1.0 Version */
		http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
				.authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_URLS).permitAll()
						.requestMatchers(HttpMethod.GET).permitAll().anyRequest().authenticated())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		// .requestMatchers(HttpMethod.GET).permitAll()

		http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.customUserDetailService);
		provider.setPasswordEncoder(passwordEncoder());

		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public FilterRegistrationBean coresFilter() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedOriginPattern("*");
		corsConfiguration.addAllowedHeader("Authorization");
		corsConfiguration.addAllowedHeader("Content-Type");
		corsConfiguration.addAllowedHeader("Accept");
		corsConfiguration.addAllowedMethod("GET");
		corsConfiguration.addAllowedMethod("POST");
		corsConfiguration.addAllowedMethod("DELETE");
		corsConfiguration.addAllowedMethod("PUT");
		corsConfiguration.addAllowedMethod("OPTIONS");
		corsConfiguration.setMaxAge(3600L);

		source.registerCorsConfiguration("/**", corsConfiguration);

		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));

//		bean.setOrder(-110);

		return bean;
	}
}
