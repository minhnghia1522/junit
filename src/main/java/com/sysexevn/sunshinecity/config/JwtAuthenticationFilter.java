package com.sysexevn.sunshinecity.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sysexevn.sunshinecity.dto.EmployeeDto;
import com.sysexevn.sunshinecity.service.IEmployeeService;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	public IEmployeeService service;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getJwtFromRequest(request);
			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				String userName = tokenProvider.getUserFromJWT(jwt);
				EmployeeDto employeeDto = service.getByEmail(userName);
				CustomUserDetails userDetails = CustomUserDetails.builder().username(employeeDto.getEmail())
						.id(employeeDto.getEmployeeId()).password(passwordEncoder(employeeDto.getPassWord()))
						.authorities(employeeDto.getEmployeeRole().stream()
								.map(x -> new SimpleGrantedAuthority(x.getRole())).toList())
						.build();
				if (userDetails != null) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (ExpiredJwtException ex) {
			String isRefreshToken = request.getHeader("isRefreshToken");
			String requestURL = request.getRequestURL().toString();
			if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refresh-token")) {
				allowForRefreshToken(ex, request);
			} else {
				request.setAttribute("exception", ex);
			}
		} catch (Exception ex) {
			log.error("failed on set user authentication", ex);
		}
		filterChain.doFilter(request, response);
	}

	private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		request.setAttribute("claims", ex.getClaims());
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public String passwordEncoder(String text) {
		return new BCryptPasswordEncoder().encode(text);
	}
}
