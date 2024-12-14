package br.com.arenap27app.domain.administrador;

import br.com.arenap27app.domain.usuario.Usuario;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity	
public class Administrador extends Usuario {

}
