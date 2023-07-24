package com.snehasish.blog.security;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	Logger logger = LogManager.getLogger(JWTAuthenticationFilter.class);

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTTokenHelper jwtTokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1. get token
//		String requestToken = request.getHeader("Authorization");
		// Bearer 23525223sdgsg.... format
//		logger.info("Token :  { " + requestToken + " }");

		String username = null;

		String token = null;

		// commented for not using jwt token through response, rather will use it
		// through serverside cookie
//		if (requestToken != null && requestToken.startsWith("Bearer")) {
//			token = requestToken.substring(7);
//			try {
//				username = this.jwtTokenHelper.getUsernameFromToken(token);
//			} catch (IllegalArgumentException e) {
//				logger.info("Illegal Argument while fetching the username !!");
//				e.printStackTrace();
//			} catch (ExpiredJwtException e) {
//				logger.info("Given jwt token is expired !!");
//				e.printStackTrace();
////				throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
//			} catch (MalformedJwtException e) {
//				logger.info("Some changed has done in token !! Invalid Token");
//				e.printStackTrace();
//			}
//		} else {
//			logger.info("Jwt token does not begin with 'Bearer' OR JWT token null OR Invalid Header value");
//		}

		// get token from httpOnly Cookie
		token = getTokenFromCookies(request);
		if (token != null && !token.isEmpty()) {
			username = this.jwtTokenHelper.getUsernameFromToken(token);
		}

		// once we get the token, now we validate
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			Boolean isValidatedToken = this.jwtTokenHelper.validateToken(token, userDetails);
			if (isValidatedToken) {

				// set the authentication
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			} else {
				logger.info("Invalid jwt token");
			}
		} else {
			logger.info("username is null or context is not null");
		}
		filterChain.doFilter(request, response);
	}

	private String getTokenFromCookies(HttpServletRequest request) {

		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("jwtToken")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
