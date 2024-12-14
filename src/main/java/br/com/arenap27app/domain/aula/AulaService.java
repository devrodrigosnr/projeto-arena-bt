package br.com.arenap27app.domain.aula;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.arenap27app.domain.enums.SituacaoAula;

@Service
public class AulaService {
	
	@Autowired
	private AulaRepository aulaRepository;
	
	public List<Aula> buscarAulas(){
		return aulaRepository.findAll();
	}
	
	@Transactional
	public void salvarAula(Aula aula) {
		if(aula.getId() == null) {
			aula.setSituacao(SituacaoAula.CRIADA);
		}
		aulaRepository.save(aula);
	}

	public Aula buscarAulaByID(Integer aulaId) {
		return aulaRepository.findById(aulaId).orElseThrow();
	}
	
	public List<Aula> buscarAulaPorData(LocalDate data) {
		return aulaRepository.findByData(data);
	}
	
	public List<Aula> buscarAulaPorDataESituacao(LocalDate data, SituacaoAula situacao) {
		return aulaRepository.findByDataAndSituacao(data, situacao);
	}

	@Transactional
	public void liberarAula(Integer aulaId) {
		Aula aula = aulaRepository.findById(aulaId).orElseThrow();
		aula.setSituacao(SituacaoAula.LIBERADA);
		aulaRepository.save(aula);
	}
	
	public List<Aula> buscarAulaPorDataESituacaoLiberadaFinalizada(LocalDate data, List<SituacaoAula> situacoes) {
		return aulaRepository.findByDataAndSituacaoIn(data, situacoes);
	}

}
