package br.com.arenap27app.domain.precadastro;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.hibernate.validator.constraints.NotBlank;

import br.com.arenap27app.domain.enums.SituacaoPreCadastro;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Entity
public class PreCadastroAluno implements Serializable {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "O nome não pode ser vazio.")
	private String nome;
	
	@NotBlank(message = "O e-mail não pode ser vazio.")
	private String email;
	
	private LocalDate dataNascimento;
	
	private SituacaoPreCadastro situacaoPreCadastro;
	
	public String getDataNascimentoFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return getDataNascimento().format(formatter);
    }
}
