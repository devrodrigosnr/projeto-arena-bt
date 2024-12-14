package br.com.arenap27app.domain.precadastro;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import br.com.arenap27app.application.ValidationException;
import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aluno.AlunoRepository;
import br.com.arenap27app.domain.enums.SituacaoPreCadastro;

@Service
public class PreCadastroAlunoService {
	
	@Autowired
	PreCadastroAlunoRepository preCadastroAlunoRepository;
	
	@Autowired
	AlunoRepository alunoRepository;

	@Transactional
	public void salvarPreCadastroAluno(PreCadastroAluno aluno) throws ValidationException {
		
		if(!isEmailJaCadastrado(aluno.getEmail(), aluno.getId())){
			throw new ValidationException("Já existe um aluno cadastrado com esse e-mail");
		}
		preCadastroAlunoRepository.save(aluno);
	}
	
	public boolean isEmailJaCadastrado(String email, Integer id) {
		PreCadastroAluno alunoPreCadastro = preCadastroAlunoRepository.findByEmail(email);
		Aluno aluno = alunoRepository.findByEmail(email);
		
		if(!ObjectUtils.isEmpty(aluno) ||  !ObjectUtils.isEmpty(alunoPreCadastro)) {
			if(id == null) {
				return false;
			}
			
			if(!aluno.getId().equals(id)) {
				return false;
			}
		}
 		return true;
	}

	public List<PreCadastroAluno> buscarAlunosPreCadastro(SituacaoPreCadastro situacaoPre) {
		return preCadastroAlunoRepository.findBySituacaoPreCadastro(situacaoPre);
	}
}
