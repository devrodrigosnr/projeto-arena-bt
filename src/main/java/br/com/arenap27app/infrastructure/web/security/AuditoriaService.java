package br.com.arenap27app.infrastructure.web.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import br.com.arenap27app.util.SecurityUtils;

@Service
public class AuditoriaService implements AuditorAware<String> {

	@Override
    public Optional<String> getCurrentAuditor() {
    	LoggedUser usuario = SecurityUtils.loggedUser();
        // Lógica para obter o nome do usuário atual
        // Por exemplo, você pode recuperá-lo do contexto de segurança
        return !ObjectUtils.isEmpty(usuario) ? Optional.of(usuario.getUsername()) : null;
    }
}