package br.com.arenap27app.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.security.core.token.Token;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aluno.AlunoRepository;
import br.com.arenap27app.domain.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class UserPasswordService {
	
	@Autowired
	AlunoRepository usuarioRepository;
	
	PasswordEncoder passwordEncoder;
	
	@SneakyThrows
	public String gerarToken(Usuario usuario) {
		KeyBasedPersistenceTokenService tokenService = getInstanceFor(usuario);
		
		Token token = tokenService.allocateToken(usuario.getEmail());
		
		return token.getKey();
	}

	private KeyBasedPersistenceTokenService getInstanceFor(Usuario usuario) throws Exception {
		KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();
		
		tokenService.setServerSecret(usuario.getSenha());
		tokenService.setServerInteger(16);
		tokenService.setSecureRandom(new SecureRandomFactoryBean().getObject());
		return tokenService;
	}
	
	@SneakyThrows
	public void trocarSenha(String novaSenha, String rawToken) {
		PasswordTokenPublicData publicData = readPublicData(rawToken);
		
		if(isExpired(publicData)) {
			throw new RuntimeException("Token expirado");
		}
		
		Aluno usuario = usuarioRepository.findByEmail(publicData.getEmail());
		KeyBasedPersistenceTokenService tokenService = this.getInstanceFor(usuario);
		tokenService.verifyToken(rawToken);
		usuario.setSenha(StringUtils.encrypt(novaSenha));
		usuarioRepository.save(usuario);
	}

	private boolean isExpired(PasswordTokenPublicData publicData) {
		Instant criadoEm = new Date(publicData.getDataCriacao()).toInstant();
		Instant agora = new Date().toInstant();
		return criadoEm.plus(Duration.ofMinutes(5)).isBefore(agora);
	}

	private PasswordTokenPublicData readPublicData(String rawToken) {
		byte[] bytes = Base64.getDecoder().decode(rawToken);
		String rawTokenDecoded = new String(bytes);
		System.out.println(rawTokenDecoded);
		
		String[] tokenParts = rawTokenDecoded.split(":");
		Long timestamp = Long.parseLong(tokenParts[0]);
		String email = tokenParts[2];
		System.out.println(new Date(timestamp));
		System.out.println(tokenParts[2]);
 		return new PasswordTokenPublicData(email, timestamp);
	}
	

}
