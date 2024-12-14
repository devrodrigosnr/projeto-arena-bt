package br.com.arenap27app.domain.pagamento;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.arenap27app.domain.aluno.Aluno;
import jakarta.transaction.Transactional;

@Service
public class PagamentoService {
	
	@Autowired
	private PagamentoRepository repository;

	@Transactional
	public void efetuarPagamentoParaAluno(Pagamento pagamento) {
		repository.save(pagamento);
	}

	public List<Pagamento> buscarPagamentosDoAluno(Aluno aluno) {
		return repository.findByAluno(aluno);
	}
	
	 public Pagamento encontrarUltimoPagamento(List<Pagamento> pagamentos) {
	        // Ordenar os pagamentos por data de pagamento em ordem decrescente
		 List<Pagamento> pagamentosOrdenados = pagamentos.stream()
	                .sorted(Comparator.comparing(Pagamento::getReferenciaMesPagamento).reversed())
	                .toList();

	        // Retornar o primeiro pagamento da lista, que será o último pagamento
		 return pagamentosOrdenados.stream().findFirst().orElse(null);
	}

}
