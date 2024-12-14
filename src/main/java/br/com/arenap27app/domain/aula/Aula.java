package br.com.arenap27app.domain.aula;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import br.com.arenap27app.application.Auditoria;
import br.com.arenap27app.domain.agendamento.Agendamento;
import br.com.arenap27app.domain.enums.SituacaoAula;
import br.com.arenap27app.domain.professor.Professor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Entity	
public class Aula extends Auditoria{
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalTime horario;
	
	private LocalDate data;
	
	@ManyToOne
	private Professor professor;
	
	private Long vagas;
	
	private Long duracao;
	
	private SituacaoAula situacao;
	
    @OneToMany(mappedBy = "aula")
    private List<Agendamento> agendamentos;
    
    @Transient
    private boolean checkedIn;
    
    @Transient
    private boolean isLotada;
    
    @Transient
    private boolean isSairFilaEspera;
    
    @Transient
    @Getter
    @Setter
    private String agendamentoXVagas;

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
    
 // Método para retornar a data formatada no formato "dd/MM/yyyy"
    public String getDataFormatada() {
        if (data != null) {
            return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            return "";
        }
    }
}
