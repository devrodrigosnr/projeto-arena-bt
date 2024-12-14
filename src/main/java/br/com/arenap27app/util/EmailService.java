package br.com.arenap27app.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	public void enviarEmail(String email, String token) {
		var message = new SimpleMailMessage();
		message.setFrom("noreply@arenap27.com");
		message.setTo(email);
		message.setSubject("Recuperação de Senha");
		String urlToken = gerarUrlComToken(token);
		message.setText(urlToken);
		mailSender.send(message);
		
	}
	
	@Value("${app.base-url}")
    private String baseUrl;

	
	public String gerarUrlComToken(String token) {
        return baseUrl + "/public/finalizar-recuperar-senha?token=" + token;
    }

}
