package br.com.arenap27app.domain.aluno;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.arenap27app.domain.enums.SituacaoAluno;
import br.com.arenap27app.domain.enums.SituacaoAlunoAulaExperimental;
import br.com.arenap27app.domain.enums.TipoPlano;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {
	
	public Aluno findByEmail(String email);

	public List<Aluno> findByPlanoAndSituacaoAlunoAulaExperimental(TipoPlano tipoPlano,
			SituacaoAlunoAulaExperimental situacao);

	public List<Aluno> findByPlanoAndSituacaoAluno(TipoPlano tipoPlano, SituacaoAluno situacaoAluno);

	public List<Aluno> findByPlanoTipoPlanoAndSituacaoAluno(TipoPlano tipoPlano, SituacaoAluno situacaoAluno);

	public List<Aluno> findByPlanoTipoPlanoAndSituacaoAlunoAulaExperimental(TipoPlano tipoPlano,
			SituacaoAlunoAulaExperimental situacao);

	public List<Aluno> findBySituacaoAlunoAulaExperimental(SituacaoAlunoAulaExperimental situacao);

	public List<Aluno> findBySituacaoAluno(SituacaoAluno situacaoExperimental);

	public List<Aluno> findAllBySituacaoAlunoNot(SituacaoAluno experimental);
}
