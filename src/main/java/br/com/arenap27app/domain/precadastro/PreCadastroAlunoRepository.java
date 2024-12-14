package br.com.arenap27app.domain.precadastro;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.arenap27app.domain.enums.SituacaoPreCadastro;

public interface PreCadastroAlunoRepository extends JpaRepository<PreCadastroAluno, Integer>{
	
	public PreCadastroAluno findByEmail(String email);

	public List<PreCadastroAluno> findBySituacaoPreCadastro(SituacaoPreCadastro situacaoPre);

}
