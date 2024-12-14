package br.com.arenap27app.domain.filaespera;

import java.time.Instant;

import br.com.arenap27app.application.Auditoria;
import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aula.Aula;
import br.com.arenap27app.domain.enums.SituacaoFilaEspera;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity	
public class FilaEspera extends Auditoria{
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name = "aluno_id")
	private Aluno aluno;
	
	@ManyToOne
    @JoinColumn(name = "aula_id")
	private Aula aula;
	
	private SituacaoFilaEspera situacaoFilaEspera;
	
    @Column(name = "dataHoraInclusaoFila")
    private Instant dataHoraInclusaoFila = Instant.now();
}
