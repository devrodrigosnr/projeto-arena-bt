package br.com.arenap27app.domain.professor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfessorService {
	
	@Autowired
	private ProfessorRepository professorRepository;
	
	public List<Professor> buscarProfessoresAtivos(){
		return professorRepository.findAll();
	}
	
	

}
