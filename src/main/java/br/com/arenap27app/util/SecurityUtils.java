package br.com.arenap27app.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.arenap27app.domain.administrador.Administrador;
import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.professor.Professor;
import br.com.arenap27app.infrastructure.web.security.LoggedUser;

public class SecurityUtils {

	private static final String NAO_EXISTE_UM_USUARIO_LOGADO = "Não existe um usuário logado";

	public static LoggedUser loggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication instanceof AnonymousAuthenticationToken) {
			return null;
		}
		
		return (LoggedUser) authentication.getPrincipal();
	}
	
	public static Aluno loggedAluno() {
		LoggedUser loggedUser = loggedUser();
		
		if(loggedUser == null) {
			throw new IllegalStateException(NAO_EXISTE_UM_USUARIO_LOGADO);
		}
		
		if(!(loggedUser.getUsuario() instanceof Aluno)) {
			throw new IllegalStateException("O usuário logado não é um aluno!");
		}
		
		return (Aluno) loggedUser.getUsuario();
	}
	
	public static Professor loggedProfessor() {
		LoggedUser loggedUser = loggedUser();
		
		if(loggedUser == null) {
			throw new IllegalStateException(NAO_EXISTE_UM_USUARIO_LOGADO);
		}
		
		if(!(loggedUser.getUsuario() instanceof Professor)) {
			throw new IllegalStateException("O usuário logado não é um Professor!");
		}
		
		return (Professor) loggedUser.getUsuario();
	}
	
	public static Administrador loggedAdministrador() {
		LoggedUser loggedUser = loggedUser();
		
		if(loggedUser == null) {
			throw new IllegalStateException(NAO_EXISTE_UM_USUARIO_LOGADO);
		}
		
		if(!(loggedUser.getUsuario() instanceof Administrador)) {
			throw new IllegalStateException("O usuário logado não é um Administrador!");
		}
		
		return (Administrador) loggedUser.getUsuario();
	}
}
