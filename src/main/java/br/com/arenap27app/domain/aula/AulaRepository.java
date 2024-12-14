package br.com.arenap27app.domain.aula;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.arenap27app.domain.enums.SituacaoAula;

public interface AulaRepository extends JpaRepository<Aula, Integer>{
	
	// Método para buscar aulas por data
    List<Aula> findByData(LocalDate data);
    
    List<Aula> findByDataAndSituacao(LocalDate data, SituacaoAula situacao);
    
    List<Aula> findByDataAndSituacaoIn(LocalDate data, List<SituacaoAula> situacoes);

}
