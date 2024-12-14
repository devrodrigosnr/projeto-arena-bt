package br.com.arenap27app.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordTokenPublicData {
	
	private final String email;
	
	private final Long dataCriacao;

}
