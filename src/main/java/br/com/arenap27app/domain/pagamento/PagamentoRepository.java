package br.com.arenap27app.domain.pagamento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.arenap27app.domain.aluno.Aluno;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer>{

	List<Pagamento> findByAluno(Aluno aluno);

}
