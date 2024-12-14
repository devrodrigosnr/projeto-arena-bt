package br.com.arenap27app.infrastructure.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.arenap27app.domain.administrador.AdministradorRepository;
import br.com.arenap27app.domain.aluno.AlunoRepository;
import br.com.arenap27app.domain.professor.ProfessorRepository;
import br.com.arenap27app.domain.usuario.Usuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private AdministradorRepository administradorRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	    Usuario usuario = findUserByEmail(username);

	    if (usuario == null) {
	        throw new UsernameNotFoundException(username);
	    }

	    return new LoggedUser(usuario);
	}

	private Usuario findUserByEmail(String email) {
	    Usuario usuario = alunoRepository.findByEmail(email);
	    if (usuario == null) {
	        usuario = professorRepository.findByEmail(email);
	    }
	    if (usuario == null) {
	        usuario = administradorRepository.findByEmail(email);
	    }
	    return usuario;
	}
}
