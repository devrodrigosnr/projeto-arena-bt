package br.com.arenap27app.domain.planos;

import java.math.BigDecimal;
import java.util.List;

import br.com.arenap27app.application.Auditoria;
import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.enums.TipoPlano;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@SuppressWarnings("serial")
@Getter
@Setter
@Entity
public class Planos extends Auditoria{
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nome;
	
	private BigDecimal valor;
	
	private Integer frequenciaMaxima;
	
	private TipoPlano tipoPlano;
	
	private Integer dataVencimento;
	
	@OneToMany(mappedBy = "plano")
	private List<Aluno> alunos;
}
