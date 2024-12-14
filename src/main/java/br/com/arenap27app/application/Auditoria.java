package br.com.arenap27app.application;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditoria implements Serializable {

	@CreatedBy
	@Column(name = "usuario_criacao")
	private String usuarioCriacao;
	
	@LastModifiedBy
    @Column(name = "usuario_atualizacao")
    private String usuarioAtualizacao;


	@CreatedDate
    @Column(name = "data_criacao")
    private Instant dataCriacao = Instant.now();

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private Instant dataAtualizacao = Instant.now();

}
