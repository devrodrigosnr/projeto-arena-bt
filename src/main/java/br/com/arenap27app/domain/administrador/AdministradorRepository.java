package br.com.arenap27app.domain.administrador;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer>{

	public Administrador findByEmail(String username);

}
