package br.com.arenap27app.domain.aluno;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import br.com.arenap27app.application.ValidationException;
import br.com.arenap27app.domain.enums.SituacaoAluno;
import br.com.arenap27app.domain.enums.SituacaoAlunoAulaExperimental;
import br.com.arenap27app.domain.enums.SituacaoPreCadastro;
import br.com.arenap27app.domain.enums.TipoPlano;
import br.com.arenap27app.domain.pagamento.Pagamento;
import br.com.arenap27app.domain.pagamento.PagamentoService;
import br.com.arenap27app.domain.precadastro.PreCadastroAluno;
import br.com.arenap27app.domain.precadastro.PreCadastroAlunoRepository;

@Service
public class AlunoService {
	
	@Autowired
	private AlunoRepository repository;
	
	@Autowired
	private PreCadastroAlunoRepository preCadastroRepository;
	
	@Autowired
	private PagamentoService pagamentoService;
	
	@Transactional
	public void salvarAluno(Aluno aluno) throws ValidationException {
		
		if(!isEmailJaCadastrado(aluno.getEmail(), aluno.getId())){
			throw new ValidationException("E-mail já cadastrado");
		}
		
		if(aluno.getId() != null) {
			Aluno alunoDB = repository.findById(aluno.getId()).orElseThrow();
			aluno.setSenha(alunoDB.getSenha());
		}else {
			aluno.encryptPassword();
		}
		
		PreCadastroAluno alunoPre = preCadastroRepository.findByEmail(aluno.getEmail());
		
		if(!ObjectUtils.isEmpty(alunoPre)) {
			alunoPre.setSituacaoPreCadastro(SituacaoPreCadastro.REALIZADO);
			preCadastroRepository.save(alunoPre);
		}
		repository.save(aluno);
	}
	
	public boolean isEmailJaCadastrado(String email, Integer id) {
		Aluno aluno = repository.findByEmail(email);
		
		if(aluno != null) {
			if(id == null) {
				return false;
			}
			
			if(!aluno.getId().equals(id)) {
				return false;
			}
		}
 		return true;
	}
	
	public boolean isPreCadastroRealizado(String email) {
		PreCadastroAluno alunoPre = preCadastroRepository.findByEmail(email);
		return !ObjectUtils.isEmpty(alunoPre);
	}
	
	public void validarPreCadastro(String email) throws ValidationException {
		
		if(!isPreCadastroRealizado(email)) {
			throw new ValidationException("Não existe pré-cadastro realizado para o aluno informado. Por favor, contate o professor");
		}
	}

	public Aluno buscarAlunoByID(Integer alunoId) {
		return repository.findById(alunoId).orElseThrow();
	}

	public List<Aluno> buscarAlunosMensalistasAptos(TipoPlano tipoPlano, SituacaoAluno situacaoAluno) {
		return repository.findByPlanoTipoPlanoAndSituacaoAluno(tipoPlano, situacaoAluno);
	}

	public List<Aluno> buscarAlunosAulaExperimentalAptos(SituacaoAlunoAulaExperimental situacao) {
		return repository.findBySituacaoAlunoAulaExperimental(situacao);
	}

	public List<Aluno> buscarAlunos() {
		return repository.findAllBySituacaoAlunoNot(SituacaoAluno.EXPERIMENTAL);
	}
	
	public boolean isAlunoPendentePagamento(Aluno aluno) {
		Pagamento ultimoPagamento = pagamentoService.encontrarUltimoPagamento(aluno.getPagamentos());
		int diaVencimento = aluno.getPlano().getDataVencimento();
		LocalDate dataAtual = LocalDate.now();
		LocalDate dataVencimento = LocalDate.of(dataAtual.getYear(), dataAtual.getMonth(), diaVencimento);
		return ultimoPagamento == null || ultimoPagamento.getReferenciaMesPagamento().isBefore(dataVencimento) || dataVencimento.isAfter(dataAtual);
	}

	public List<Aluno> buscarAlunosAulaExperimental(SituacaoAluno situacaoExperimental) {
		return repository.findBySituacaoAluno(situacaoExperimental);
	}
	
	public Aluno buscarAlunoPorEmail(String email) {
		return repository.findByEmail(email);
	}
}
