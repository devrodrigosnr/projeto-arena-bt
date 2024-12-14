package br.com.arenap27app.domain.pagamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.arenap27app.application.Auditoria;
import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.enums.FormaPagamento;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@SuppressWarnings("serial")
@Setter
@Getter
@Entity
public class Pagamento extends Auditoria{
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "aluno_id")
	private Aluno aluno;
	
	private BigDecimal valorPago;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataPagamento;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate referenciaMesPagamento;
	
	private FormaPagamento formaPagamento;
	
    public String getDataReferenciaMesPagamentoFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return referenciaMesPagamento.format(formatter);
    }
    
    public String getDataPagamentoFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dataPagamento.format(formatter);
    }
}
