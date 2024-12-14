package br.com.arenap27app.domain.filaespera;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aula.Aula;
import br.com.arenap27app.domain.enums.SituacaoFilaEspera;

@Service
public class FilaEsperaService {
	
	@Autowired
	FilaEsperaRepository repository;

	public void colocarAlunoNaFila(Aluno aluno, Aula aula) {
		FilaEspera fila = new FilaEspera();
		fila.setAluno(aluno);
		fila.setAula(aula);
		fila.setSituacaoFilaEspera(SituacaoFilaEspera.PENDENTE);
		repository.save(fila);
	}

	public List<FilaEspera> recuperarFilaEsperaParaAula(Aula aula) {
		return repository.findByAulaAndSituacaoFilaEspera(aula, SituacaoFilaEspera.PENDENTE);
	}

	public void removerAlunoFilaEspera(FilaEspera alunoFilaEspera, Aluno alunoFila, Aula aula) {
		FilaEspera fila = alunoFilaEspera;
		fila.setAluno(alunoFila);
		fila.setAula(aula);
		fila.setSituacaoFilaEspera(SituacaoFilaEspera.CHAMADA);
		repository.save(fila);
	}

	public FilaEspera buscarAlunoPorAulaESituacaoNaFilaDeEspera(Aluno aluno, Aula aula, SituacaoFilaEspera situacaoFila) {
		return repository.findByAlunoAndAulaAndSituacaoFilaEspera(aluno, aula, situacaoFila);
	}

	@Transactional
	public void sairDaFilaDeEspera(FilaEspera filaEspera) {
		filaEspera.setSituacaoFilaEspera(SituacaoFilaEspera.CANCELADA);
		repository.save(filaEspera);
	}

}
