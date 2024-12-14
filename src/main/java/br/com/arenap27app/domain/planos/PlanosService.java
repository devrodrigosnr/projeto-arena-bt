package br.com.arenap27app.domain.planos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanosService {
	
	@Autowired
	private PlanosRepository repository;

	public void salvarPlano(Planos plano) {
		repository.save(plano);
	}

	public List<Planos> buscarPlanos() {
		return repository.findAll();
	}

	public Planos buscarPlanoById(Integer planoId) {
		return repository.findById(planoId).orElseThrow();
	}

}
