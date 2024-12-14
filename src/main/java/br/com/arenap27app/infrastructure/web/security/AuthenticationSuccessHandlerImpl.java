package br.com.arenap27app.infrastructure.web.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import br.com.arenap27app.util.SecurityUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler{
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		Role role = SecurityUtils.loggedUser().getRole();
		
		if (role == Role.ALUNO) {
			response.sendRedirect("aluno/home");
		
		} else if (role == Role.PROFESSOR) {
			response.sendRedirect("professor/home");
		
		} else if (role == Role.ADMINISTRADOR) {
			response.sendRedirect("administrador/home");
		
		} else {
			throw new IllegalStateException("Erro na autenticação");
		}
	}

}
