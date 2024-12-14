package br.com.arenap27app.domain.usuario;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import br.com.arenap27app.application.Auditoria;
import br.com.arenap27app.util.StringUtils;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public class Usuario extends Auditoria implements Serializable {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "O nome não pode ser vazio.")
	private String nome;
	
	@NotBlank(message = "O e-mail não pode ser vazio.")
	private String email;
	
	@NotBlank(message = "O telefone não pode ser vazio.")
	private String telefone;
	
	@NotBlank(message = "O CPF não pode ser vazio.")
	private String cpf;

	@NotBlank(message = "A senha não pode ser vazia.")
	private String senha;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataNascimento;
	
	public void encryptPassword() {
		this.senha = StringUtils.encrypt(this.senha);
	}
	
	

}
