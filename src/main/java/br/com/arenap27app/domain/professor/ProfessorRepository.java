package br.com.arenap27app.domain.professor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Integer>{

	public Professor findByEmail(String username);

}
