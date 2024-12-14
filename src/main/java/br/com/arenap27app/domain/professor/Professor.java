package br.com.arenap27app.domain.professor;

import java.util.List;

import br.com.arenap27app.domain.aula.Aula;
import br.com.arenap27app.domain.usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity	
public class Professor extends Usuario{
	
	@OneToMany(mappedBy = "professor")
	private List<Aula> aulas;
	
}
