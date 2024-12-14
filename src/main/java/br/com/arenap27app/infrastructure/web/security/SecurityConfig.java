package br.com.arenap27app.infrastructure.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditoriaService")
public class SecurityConfig {
	
    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditoriaService();
    }
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AuthenticationSuccessHandlerImpl();
	}
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.requestMatchers("/images/**", "/css/**", "/js/**", "/public/**", "/sbpay/**").permitAll()
			.requestMatchers("/aluno/**").hasRole(Role.ALUNO.toString())
			.requestMatchers("/professor/**").hasRole(Role.PROFESSOR.toString())
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.failureUrl("/login-error")
				.successHandler(authenticationSuccessHandler())
				.permitAll()
			.and()
				.logout()
				.logoutUrl("/logout")
				.permitAll();
		return http.build();
	}
}