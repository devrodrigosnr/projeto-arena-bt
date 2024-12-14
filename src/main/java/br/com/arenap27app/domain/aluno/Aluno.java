package br.com.arenap27app.domain.aluno;

import java.time.format.DateTimeFormatter;
import java.util.List;

import br.com.arenap27app.domain.agendamento.Agendamento;
import br.com.arenap27app.domain.enums.SituacaoAluno;
import br.com.arenap27app.domain.enums.SituacaoAlunoAulaExperimental;
import br.com.arenap27app.domain.pagamento.Pagamento;
import br.com.arenap27app.domain.planos.Planos;
import br.com.arenap27app.domain.usuario.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity	
public class Aluno extends Usuario{
	

	@ManyToOne
	@JoinColumn(name = "plano_id")
	private Planos plano;
	
	private String telefoneEmergencia;
	
	private String bairro;
	
    @OneToMany(mappedBy = "aluno")
    private List<Agendamento> agendamentos;
    
    private SituacaoAlunoAulaExperimental situacaoAlunoAulaExperimental;
    
    private SituacaoAluno situacaoAluno;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Pagamento> pagamentos;
    
    @Transient
    private boolean isPresente;
    
    public boolean isPresente() {
        return isPresente;
    }

    public void setIsPresente(boolean isPresente) {
        this.isPresente = isPresente;
    }
    
    public String getDataNascimentoFormatada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return getDataNascimento().format(formatter);
    }
}
