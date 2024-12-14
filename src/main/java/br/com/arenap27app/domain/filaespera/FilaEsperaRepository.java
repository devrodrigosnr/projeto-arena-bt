package br.com.arenap27app.domain.filaespera;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aula.Aula;
import br.com.arenap27app.domain.enums.SituacaoFilaEspera;

public interface FilaEsperaRepository extends JpaRepository<FilaEspera, Integer>{

	List<FilaEspera> findByAulaAndSituacaoFilaEspera(Aula aula, SituacaoFilaEspera pendente);

	FilaEspera findByAlunoAndAulaAndSituacaoFilaEspera(Aluno aluno, Aula aula, SituacaoFilaEspera situacaoFila);
}
