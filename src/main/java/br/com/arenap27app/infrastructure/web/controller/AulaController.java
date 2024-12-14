package br.com.arenap27app.infrastructure.web.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.arenap27app.application.ValidationException;
import br.com.arenap27app.domain.agendamento.Agendamento;
import br.com.arenap27app.domain.agendamento.AgendamentoService;
import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aluno.AlunoService;
import br.com.arenap27app.domain.aula.Aula;
import br.com.arenap27app.domain.aula.AulaService;
import br.com.arenap27app.domain.enums.SituacaoAgendamento;
import br.com.arenap27app.domain.enums.SituacaoAluno;
import br.com.arenap27app.domain.enums.SituacaoAlunoAulaExperimental;
import br.com.arenap27app.domain.enums.SituacaoAula;
import br.com.arenap27app.domain.enums.SituacaoFilaEspera;
import br.com.arenap27app.domain.enums.SituacaoPresenca;
import br.com.arenap27app.domain.enums.TipoPlano;
import br.com.arenap27app.domain.filaespera.FilaEspera;
import br.com.arenap27app.domain.filaespera.FilaEsperaService;
import br.com.arenap27app.domain.professor.Professor;
import br.com.arenap27app.domain.professor.ProfessorService;
import br.com.arenap27app.util.SecurityUtils;
import jakarta.validation.Valid;

@Controller
@RequestMapping(path = "/aula")
public class AulaController {
	
	private static final String PROFESSOR_PAINEL_AULAS = "professor-painel-aulas";

	private static final String PROFESSOR_VISUALIZAR_AULA = "professor-visualizar-aula";

	private static final String ALUNOS_AGENDADOS = "alunosAgendados";
	
	private static final String PROFESSORES = "professores";

	@Autowired
	private AulaService aulaService;
	
	@Autowired
	private ProfessorService professorService;
	
	@Autowired
	private AgendamentoService agendamentoService;
	
	@Autowired
	private AlunoService alunoService;
	
	@Autowired
	private FilaEsperaService filaEsperaService;
	
	@GetMapping(path = "/home")
	public String home() {
		return "professor-home";
	}
	
	@GetMapping(path = "/agendamento")
	public String professorAgendamento() {
		return PROFESSOR_PAINEL_AULAS;
	}
	
	@GetMapping("/aluno/buscar")
	public String buscarAulaAluno(@RequestParam("date") String date, Model model) {
		Aluno aluno = SecurityUtils.loggedAluno();
		
		if(!ObjectUtils.isEmpty(aluno.getPlano())){
			List<Aula> aulas = aulaService.buscarAulaPorDataESituacao(LocalDate.parse(date), SituacaoAula.LIBERADA);
			Collections.sort(aulas, Comparator.comparing(Aula::getHorario));
			for (Aula aula : aulas) {
				int qtdAgendamentosRealizados = agendamentoService.verificarQuantidadeDeVagasDisponiveisParaAula(aula);
				String agendamentosXVagas = qtdAgendamentosRealizados + "/" + aula.getVagas();
				aula.setAgendamentoXVagas(agendamentosXVagas);
				List<FilaEspera> listfilaEspera = filaEsperaService.recuperarFilaEsperaParaAula(aula);
				listfilaEspera.sort(Comparator.comparing(FilaEspera::getDataHoraInclusaoFila));
				FilaEspera filaEspera = filaEsperaService.buscarAlunoPorAulaESituacaoNaFilaDeEspera(aluno, aula, SituacaoFilaEspera.PENDENTE);
				if(!ObjectUtils.isEmpty(filaEspera)) {
					aula.setSairFilaEspera(true);
					
				int posicaoAluno = listfilaEspera.stream()
                            .map(FilaEspera::getAluno)
                            .map(Aluno::getId)
                            .collect(Collectors.toList())
                            .indexOf(aluno.getId());
				model.addAttribute("posicaoAluno", posicaoAluno + 1);
					
				}
				
				if(agendamentoService.validarSeTemVagaDisponivel(aula)) {
					aula.setLotada(true);
				}
				for (Agendamento agendamento : aula.getAgendamentos()) {
					if(agendamento.getSituacaoAgendamento().equals(SituacaoAgendamento.AGENDADO) && agendamento.getAluno().equals(aluno))
						aula.setCheckedIn(true);
				}
			}
			model.addAttribute("aulas", aulas);
		}
		return "aluno-agendamento";
	}
	
	@GetMapping("/professor/buscar")	
	public String buscarAulaProfessor(@RequestParam("date") String date, Model model) {
		List<Aula> aulas = aulaService.buscarAulaPorData(LocalDate.parse(date));
		List<Aluno> alunosAgendados = new ArrayList<>();
		for (Aula aula : aulas) {
			for (Agendamento agendamento : aula.getAgendamentos()) {
				if(agendamento.getSituacaoAgendamento().equals(SituacaoAgendamento.AGENDADO) && aula.getId().equals(agendamento.getAula().getId())) {
					alunosAgendados.add(agendamento.getAluno());
					
				}
			}
		}
		Collections.sort(aulas, Comparator.comparing(Aula::getHorario));
		model.addAttribute("aulas", aulas);
		List<Professor> professores = professorService.buscarProfessoresAtivos();
		model.addAttribute(PROFESSORES, professores);
		return PROFESSOR_PAINEL_AULAS;
	}
	
	@GetMapping("/professor/adicionar")
	public String teste(Model model) {	
		List<Professor> professores = professorService.buscarProfessoresAtivos();
		model.addAttribute(PROFESSORES, professores);
		return PROFESSOR_PAINEL_AULAS;
	}
	
    @PostMapping(path = "/salvar")
    public String salvarAula(@ModelAttribute("aula")@Valid Aula aula, Errors errors, Model model, 
    		@RequestParam("dataHoraSelecionada") String dataHoraSelecionada) {
    	LocalDate data = LocalDate.parse(dataHoraSelecionada);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = data.format(formatter);
        aula.setData(LocalDate.parse(dataFormatada, formatter));
		aulaService.salvarAula(aula);
		model.addAttribute("msg", "Aula gravada com sucesso");
		List<Professor> professores = professorService.buscarProfessoresAtivos();
		model.addAttribute(PROFESSORES, professores);
        return PROFESSOR_PAINEL_AULAS;
    }
    
    @PostMapping(path = "/editar")
    public String editarAula(@ModelAttribute("aula")@Valid Aula aula, Errors errors, Model model) {
    	aula.setSituacao(SituacaoAula.LIBERADA);
		aulaService.salvarAula(aula);
		model.addAttribute("msg", "Aula atualizada com sucesso");
		List<Professor> professores = professorService.buscarProfessoresAtivos();
		model.addAttribute(PROFESSORES, professores);
        return PROFESSOR_PAINEL_AULAS;
    }
    
    @PostMapping(path = "/checkin")
    public ResponseEntity<?> agendarAula(@RequestParam("aulaId") Integer aulaId) {
    	Aluno aluno = SecurityUtils.loggedAluno();
    	Aula aula = aulaService.buscarAulaByID(aulaId);
    	List<SituacaoAula> situacoes = Arrays.asList(SituacaoAula.LIBERADA, SituacaoAula.FINALIZADA);
    	List<Aula> aulas = aulaService.buscarAulaPorDataESituacaoLiberadaFinalizada(aula.getData(), situacoes);
    	Optional<Agendamento> agendamentoJaRealizado = agendamentoService.validarAgendamentoRealizadoParaData(aluno, aulas);
    	if(agendamentoService.validarSeTemVagaDisponivel(aula)) {
    		return ResponseEntity.badRequest().body("Número de vagas completo para esta aula.");
    	}
        if (agendamentoJaRealizado.isPresent()) {
            return ResponseEntity.badRequest().body("Você já fez check-in para esta data.");
        }
        
        if(agendamentoService.isLimiteDeAulasNoMesAtingido(aluno)) {
        	return ResponseEntity.badRequest().body("Quantidade de aulas realizadas no mês excede o limite do plano");
        }
        
    	Agendamento agendamento = new Agendamento();
    	agendamento.setAluno(aluno);
    	agendamento.setAula(aula);
    	agendamento.setSituacaoAgendamento(SituacaoAgendamento.AGENDADO);
    	agendamentoService.realizarCheckin(agendamento);
    	return ResponseEntity.ok().build();	
    }

    @PostMapping(path = "/desmarcar")
    public ResponseEntity<?> desmarcarAula(@RequestParam("aulaId") Integer aulaId) {
    	Aluno aluno = SecurityUtils.loggedAluno();
    	Aula aula = aulaService.buscarAulaByID(aulaId);
    	Agendamento agendamento = agendamentoService.buscarAgendamentoPorAlunoAulaESituacao(aluno, aula, SituacaoAgendamento.AGENDADO);
    	agendamento.setSituacaoAgendamento(SituacaoAgendamento.DESMARCADO);
    	agendamentoService.desmarcarAgendamento(agendamento);
    	
    	List<FilaEspera> filaEspera = filaEsperaService.recuperarFilaEsperaParaAula(aula);
        filaEspera.sort(Comparator.comparing(FilaEspera::getDataHoraInclusaoFila));
    	
    	if(!CollectionUtils.isEmpty(filaEspera)) {
    		for (FilaEspera alunoFila : filaEspera) {
    			Aluno alunoFilaEspera = alunoFila.getAluno();
    			
        		List<SituacaoAula> situacoes = Arrays.asList(SituacaoAula.LIBERADA, SituacaoAula.FINALIZADA);
            	List<Aula> aulas = aulaService.buscarAulaPorDataESituacaoLiberadaFinalizada(aula.getData(), situacoes);
            	Optional<Agendamento> agendamentoJaRealizado = agendamentoService.validarAgendamentoRealizadoParaData(alunoFilaEspera, aulas);
            	
            	if(!agendamentoJaRealizado.isPresent()) {
	        		Agendamento agendamentoFila = new Agendamento();
	        		agendamento.setAluno(alunoFilaEspera);
	        		agendamento.setAula(aula);
	        		agendamento.setSituacaoAgendamento(SituacaoAgendamento.AGENDADO);
	        		agendamentoService.agendarAlunoFilaEspera(agendamentoFila);
	        		filaEsperaService.removerAlunoFilaEspera(alunoFila, alunoFilaEspera, aula);
	        		break; // Termina o loop após agendar o próximo aluno da fila
            	}
			}
    	}
    	return ResponseEntity.ok().build();
    }
    
    @PostMapping("/liberar")
    public ResponseEntity<String> liberarAula(@RequestParam("aulaId") Integer aulaId, Model model) {
        try {
            aulaService.liberarAula(aulaId);
            model.addAttribute("msg", "Aula liberada com sucesso!");
            return ResponseEntity.ok("Aula liberada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao liberar aula: " + e.getMessage());
        }
    }
    
    @GetMapping("/professor/visualizar-aula/{id}")
    public String visualizarAula(@PathVariable("id") Integer aulaId, Model model) {
    	Aula aula = aulaService.buscarAulaByID(aulaId);
		int qtdAgendamentosRealizados = agendamentoService.verificarQuantidadeDeVagasDisponiveisParaAula(aula);
		String agendamentosXVagas = qtdAgendamentosRealizados + "/" + aula.getVagas();
		aula.setAgendamentoXVagas(agendamentosXVagas);
    	
		List<Aluno> alunosAgendados = new ArrayList<>();
		for (Agendamento agendamento : aula.getAgendamentos()) {
			if(agendamento.getSituacaoAgendamento().equals(SituacaoAgendamento.AGENDADO) && aula.getId().equals(agendamento.getAula().getId())) {
				alunosAgendados.add(agendamento.getAluno());
				setPresencaAluno(agendamento);
			}
		}
		List<Aluno> alunosDisponiveis = new ArrayList<>();
		List<Aluno> alunosMensalistasAptos = alunoService.buscarAlunosMensalistasAptos(TipoPlano.MENSALISTA, SituacaoAluno.ATIVO);
		List<Aluno> alunosExperimental = alunoService.buscarAlunosAulaExperimentalAptos(SituacaoAlunoAulaExperimental.PENDENTE);
		for (Aluno aluno : alunosExperimental) {
			aluno.setNome(aluno.getNome() + "(Exp)");
			alunosDisponiveis.add(aluno);
		}
		alunosDisponiveis.addAll(alunosMensalistasAptos);
	    model.addAttribute("aula", aula);
	    model.addAttribute(ALUNOS_AGENDADOS, alunosAgendados);
	    model.addAttribute("alunosDisponiveis", alunosDisponiveis);
        return PROFESSOR_VISUALIZAR_AULA;
    }

	private void setPresencaAluno(Agendamento agendamento) {
		if(agendamento.getSituacaoPresenca() != null && agendamento.getSituacaoPresenca().name().contains("PRESENTE")) {
			agendamento.getAluno().setIsPresente(true);
		} else {
			agendamento.getAluno().setIsPresente(false);
		}
	}
    
    @PostMapping(path = "/finalizar")
    public String finalizarAula(@RequestParam("aulaId") Integer aulaId, Model model) {
    	Aula aula = aulaService.buscarAulaByID(aulaId);
    	aula.setSituacao(SituacaoAula.FINALIZADA);
    	aulaService.salvarAula(aula);
    	model.addAttribute("msgSucesso", "Aula finalizada com sucesso!");
    	visualizarAula(aulaId, model);
    	return PROFESSOR_VISUALIZAR_AULA;
    }
    
    @PostMapping("/confirmar-presenca")
    public ResponseEntity<String> confirmarAulaAluno(@RequestParam("aulaId") Integer aulaId,
    		@RequestParam("alunoId") Integer alunoId, @RequestParam("presenca") String presenca, Model model) {
    	Aula aula = aulaService.buscarAulaByID(aulaId);
    	
		for (Agendamento agendamento : aula.getAgendamentos()) {
			if(agendamento.getAluno().getId().equals(alunoId) 
					&& agendamento.getSituacaoAgendamento().equals(SituacaoAgendamento.AGENDADO) 
					&& aula.getId().equals(agendamento.getAula().getId())) {
				if(presenca.contains("presente")) {
					agendamento.setSituacaoPresenca(SituacaoPresenca.PRESENTE);
					agendamento.getAluno().setIsPresente(true);
				} else {
					agendamento.setSituacaoPresenca(SituacaoPresenca.AUSENTE);
					agendamento.getAluno().setIsPresente(false);
				}
				agendamentoService.realizarCheckin(agendamento);
			}
		}
		
		List<Aluno> alunosAgendados = new ArrayList<>();
		for (Agendamento agendamento : aula.getAgendamentos()) {
			if(agendamento.getSituacaoAgendamento().equals(SituacaoAgendamento.AGENDADO) && aula.getId().equals(agendamento.getAula().getId())) {
				alunosAgendados.add(agendamento.getAluno());
			}
		}
		
	    model.addAttribute("aula", aula);
	    model.addAttribute(ALUNOS_AGENDADOS, alunosAgendados);	
	    return ResponseEntity.ok().body("Presença confirmada com sucesso.");
    	
    }
    
    @PostMapping(path = "/agendar-mensalista")
    public String agendarMensalista(@RequestParam("aulaId") Integer aulaId, @RequestParam("alunoId") Integer alunoId, Model model) {
    	Aula aula = aulaService.buscarAulaByID(aulaId);
    	Aluno aluno = alunoService.buscarAlunoByID(alunoId);
    	
    	List<SituacaoAula> situacoes = Arrays.asList(SituacaoAula.LIBERADA, SituacaoAula.FINALIZADA);
    	List<Aula> aulas = aulaService.buscarAulaPorDataESituacaoLiberadaFinalizada(aula.getData(), situacoes);
    	Optional<Agendamento> agendamentoJaRealizado = agendamentoService.validarAgendamentoRealizadoParaData(aluno, aulas);
    	if(agendamentoService.validarSeTemVagaDisponivel(aula)) {
    		model.addAttribute("msgErro", "Número de vagas completo para esta aula");
    		model.addAttribute("aula", aula);
        	visualizarAula(aulaId, model);
    		return PROFESSOR_VISUALIZAR_AULA;
    	}
    	
        if (agendamentoJaRealizado.isPresent()) {
        	visualizarAula(aulaId, model);
        	model.addAttribute("msgErro", "O aluno selecionado já fez checkin para esta data!");
        	model.addAttribute("aula", aula);
        	visualizarAula(aulaId, model);
        	return PROFESSOR_VISUALIZAR_AULA;
        }
        
        List<Aluno> alunosAgendados = new ArrayList<>();
        
		for (Agendamento agendamento : aula.getAgendamentos()) {
			if(agendamento.getSituacaoAgendamento().equals(SituacaoAgendamento.AGENDADO) && aula.getId().equals(agendamento.getAula().getId())) {
				alunosAgendados.add(agendamento.getAluno());
				setPresencaAluno(agendamento);
				
			}
		}
        
    	Agendamento agendamento = new Agendamento();
    	agendamento.setAluno(aluno);
    	agendamento.setAula(aula);
    	agendamento.setSituacaoAgendamento(SituacaoAgendamento.AGENDADO);
    	agendamentoService.realizarCheckin(agendamento);
    	model.addAttribute("msgSucesso", "Agendamento realizado com sucesso para o aluno " + aluno.getNome());
    	visualizarAula(aulaId, model);
    	alunosAgendados.add(agendamento.getAluno());
    	model.addAttribute(ALUNOS_AGENDADOS, alunosAgendados);
    	return PROFESSOR_VISUALIZAR_AULA;
    }
    
    @PostMapping(path = "/entrarNaFila")
    public ResponseEntity<?> entrarNaFila(@RequestParam("aulaId") Integer aulaId) {
    	Aluno aluno = SecurityUtils.loggedAluno();
    	Aula aula = aulaService.buscarAulaByID(aulaId);
   		filaEsperaService.colocarAlunoNaFila(aluno, aula);
   		return ResponseEntity.ok().build();
    }
    
    @PostMapping(path = "/sairDaFila")
    public ResponseEntity<?> sairDaFila(@RequestParam("aulaId") Integer aulaId) {
    	Aluno aluno = SecurityUtils.loggedAluno();
    	Aula aula = aulaService.buscarAulaByID(aulaId);
    	FilaEspera filaEspera = filaEsperaService.buscarAlunoPorAulaESituacaoNaFilaDeEspera(aluno, aula, SituacaoFilaEspera.PENDENTE);
   		filaEsperaService.sairDaFilaDeEspera(filaEspera);
   		return ResponseEntity.ok().build();
    }
}
