package com.company.kanban.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.company.kanban.exceptions.InvalidLoginResponse;
import com.google.gson.Gson;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AuthenticationException authException) throws IOException, ServletException {
	
		InvalidLoginResponse loginResponse = new InvalidLoginResponse();
		String jsonLoginResponse = new Gson().toJson(loginResponse); //This Google dependency convert a String to Json 
		
		httpServletResponse.setContentType("application/json");
		httpServletResponse.setStatus(401);
		httpServletResponse.getWriter().print(jsonLoginResponse);
		
	}
	
}
