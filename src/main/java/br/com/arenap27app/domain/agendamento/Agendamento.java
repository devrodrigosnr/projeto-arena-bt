package br.com.arenap27app.domain.agendamento;

import br.com.arenap27app.application.Auditoria;
import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aula.Aula;
import br.com.arenap27app.domain.enums.SituacaoAgendamento;
import br.com.arenap27app.domain.enums.SituacaoPresenca;
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
@Entity	
public class Agendamento extends Auditoria{
	
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
	
	private SituacaoAgendamento situacaoAgendamento;
	
	private SituacaoPresenca situacaoPresenca;

}
