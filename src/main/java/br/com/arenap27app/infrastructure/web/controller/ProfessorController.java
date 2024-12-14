package br.com.arenap27app.infrastructure.web.controller;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.arenap27app.application.ValidationException;
import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aluno.AlunoService;
import br.com.arenap27app.domain.aula.Aula;
import br.com.arenap27app.domain.enums.FormaPagamento;
import br.com.arenap27app.domain.enums.SituacaoAluno;
import br.com.arenap27app.domain.enums.SituacaoAlunoAulaExperimental;
import br.com.arenap27app.domain.enums.SituacaoPreCadastro;
import br.com.arenap27app.domain.enums.TipoPlano;
import br.com.arenap27app.domain.pagamento.Pagamento;
import br.com.arenap27app.domain.pagamento.PagamentoService;
import br.com.arenap27app.domain.planos.Planos;
import br.com.arenap27app.domain.planos.PlanosService;
import br.com.arenap27app.domain.precadastro.PreCadastroAluno;
import br.com.arenap27app.domain.precadastro.PreCadastroAlunoService;
import br.com.arenap27app.domain.professor.Professor;
import br.com.arenap27app.domain.professor.ProfessorRepository;
import br.com.arenap27app.domain.professor.ProfessorService;
import br.com.arenap27app.util.SecurityUtils;
import jakarta.validation.Valid;


@Controller
@RequestMapping(path = "/professor")
public class ProfessorController {
	
	private static final String TIPO_PLANOS = "tipoPlanos";

	private static final String PLANOS_CADASTRO = "planos-cadastro";

	private static final String PLANOS = "planos";

	@Autowired
	private ProfessorRepository professorRepository;
	
	@Autowired
	private ProfessorService professorService;
	
	@Autowired
	private PreCadastroAlunoService preCadastroAlunoService;
	
	@Autowired
	private AlunoService alunoService;
	
	@Autowired
	private PlanosService planosService;
	
	@Autowired
	private PagamentoService pagamentoService;
	
	@GetMapping(path = "/home")
	public String home() {
		return "professor-home";
	}
	
	@GetMapping(path = "/agendamento")
	public String professorAgendamento(Model model) {
		Aula aula = new Aula(); 
    	model.addAttribute("aula", aula);
    	
		List<Professor> professores = professorService.buscarProfessoresAtivos();

		model.addAttribute("professores", professores);
		return "professor-painel-aulas";
	}
	
	@GetMapping("/editar")
	public String editar(Model model) {
		Integer professorId = SecurityUtils.loggedProfessor().getId();
		
		Professor professor = professorRepository.findById(professorId).orElseThrow();
		model.addAttribute("professor", professor);
		ControllerHelper.setModoEdicao(model, true);
		return "professor-cadastro";
	}
	
	@GetMapping(path = "/preCadastro-aluno")
	public String preCadastrarAluno(Model model) {
		
    	PreCadastroAluno aluno = new PreCadastroAluno(); 
    	
    	model.addAttribute("aluno", aluno);
    	ControllerHelper.setModoEdicao(model, false);
		return "professor-precadastro-aluno";
	}
	
	
    @PostMapping(path = "/preCadastro/salvar")
    public String salvarAluno(@ModelAttribute("aluno")@Valid PreCadastroAluno aluno, Errors errors, Model model) {
    	
    	if(!errors.hasErrors()) {
    		try {
    			aluno.setSituacaoPreCadastro(SituacaoPreCadastro.PENDENTE);
    			preCadastroAlunoService.salvarPreCadastroAluno(aluno);
				model.addAttribute("msg", "Aluno gravado com sucesso");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
    	}
    	ControllerHelper.setModoEdicao(model, false); 
        return "professor-precadastro-aluno";
    }
    
	@GetMapping(path = "/aluno-experimental-cadastro")
	public String cadastrarAlunoExperimental(Model model) {
		
    	Aluno alunoExperimental = new Aluno(); 
    	
    	model.addAttribute("alunoExperimental", alunoExperimental);
    	ControllerHelper.setModoEdicao(model, false);
		return "professor-cadastro-aluno-experimental";
	}
	
	@PostMapping(path = "/alunoExperimental/salvar")
    public String salvarAlunoExperimental(@ModelAttribute("alunoExperimental")Aluno alunoExperimental, Errors errors, Model model) {
    	
    	if(!errors.hasErrors()) {
    		try {
    			alunoExperimental.setSenha("1");
    			alunoExperimental.setCpf("00000000000");
    			alunoExperimental.setSituacaoAlunoAulaExperimental(SituacaoAlunoAulaExperimental.PENDENTE);
    			alunoExperimental.setSituacaoAluno(SituacaoAluno.EXPERIMENTAL);
    			alunoService.salvarAluno(alunoExperimental);
				model.addAttribute("msg", "Aluno gravado com sucesso");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
    	}
    	ControllerHelper.setModoEdicao(model, false); 
        return "professor-cadastro-aluno-experimental";
    }
	
	@GetMapping(path="/cadastrar/planos")
	public String cadastrarPlanos(Model model) {
		ControllerHelper.setModoEdicao(model, false);
		List<TipoPlano> tipoPlanos = Arrays.asList(TipoPlano.values());
		Planos plano = new Planos();
		model.addAttribute("plano", plano);
		model.addAttribute(TIPO_PLANOS, tipoPlanos);
		return PLANOS_CADASTRO;
	}
	
	@PostMapping(path = "/plano/salvar")
	public String salvarPlano(@ModelAttribute("plano") Planos plano, Errors errors, Model model) {
		planosService.salvarPlano(plano);
		ControllerHelper.setModoEdicao(model, true);
		List<TipoPlano> tipoPlanos = Arrays.asList(TipoPlano.values());
		model.addAttribute(TIPO_PLANOS, tipoPlanos);
		return PLANOS_CADASTRO; 
	}
	
	@GetMapping(path="/visualizar/alunos")
	public String visualizarListaDeAlunos(Model model) {
		List<PreCadastroAluno> alunosPreCadastro = preCadastroAlunoService.buscarAlunosPreCadastro(SituacaoPreCadastro.PENDENTE);
		List<Aluno> alunosExperimental = alunoService.buscarAlunosAulaExperimental(SituacaoAluno.EXPERIMENTAL);
		List<Aluno> alunos = alunoService.buscarAlunos();
		Collections.sort(alunos, Comparator.comparing(Aluno::getNome));
		for (Aluno aluno : alunos) {
			if(aluno.getPlano() != null && aluno.getPlano().getTipoPlano().equals(TipoPlano.MENSALISTA)) {
				if(alunoService.isAlunoPendentePagamento(aluno)) {
					aluno.setSituacaoAluno(SituacaoAluno.PENDENTE_PAGAMENTO);
				} else {
					aluno.setSituacaoAluno(SituacaoAluno.ATIVO);
				}
			}
		}
		List<Planos> planos = planosService.buscarPlanos();
		model.addAttribute("alunosExperimental", alunosExperimental);
		model.addAttribute("alunosPreCadastro", alunosPreCadastro);
		model.addAttribute("alunos", alunos);
		model.addAttribute(PLANOS, planos);
		return "visualizar-alunos";
	}
	
	@GetMapping(path="/cadastro/aluno/{id}")
    public String visualizarAluno(@PathVariable("id") Integer alunoId, Model model) {
		Aluno aluno = alunoService.buscarAlunoByID(alunoId);
		List<Planos> planos = planosService.buscarPlanos();
		model.addAttribute("aluno", aluno);
		model.addAttribute(PLANOS, planos);
		ControllerHelper.setModoEdicao(model, true);
		return "alterar-cadastro-aluno";
	}
	
	@PostMapping(path="/alterar/cadastro-aluno/salvar")
	public String salvarAlteracaoCadastroAluno(@ModelAttribute("aluno") Aluno aluno, @RequestParam("planoId") String plano, Model model) {
		Planos planoBd = !plano.isEmpty() ? planosService.buscarPlanoById(Integer.parseInt(plano)) : null;
		aluno.setPlano(planoBd);
		model.addAttribute(PLANOS, plano);
		List<Aluno> alunos = alunoService.buscarAlunos();
		try {
			alunoService.salvarAluno(aluno);
		} catch (ValidationException e) {
			e.printStackTrace();
		}
		model.addAttribute("alunos", alunos);
		model.addAttribute("msg", "Aluno alterado com sucesso!");
		visualizarListaDeAlunos(model);
		return "visualizar-alunos";
	}
	
	@GetMapping(path = "/cadastro/plano/{id}")
	public String visualizarPlano(@PathVariable("id") Integer planoId, Model model) {
		Planos plano = planosService.buscarPlanoById(planoId);
		model.addAttribute("plano", plano);
		ControllerHelper.setModoEdicao(model, true);
		List<TipoPlano> tipoPlanos = Arrays.asList(TipoPlano.values());
		model.addAttribute(TIPO_PLANOS, tipoPlanos);
		return PLANOS_CADASTRO;
	}
	
	@GetMapping(path="/visualizar/planos")
	public String visualizarListaDePlsnos(Model model) {
		List<Planos> planos = planosService.buscarPlanos();
		model.addAttribute(PLANOS, planos);
		return "visualizar-planos";
	}
	
	@GetMapping(path = "/financeiro/aluno/{id}")
	public String visualizarFinanceiroAluno(@PathVariable("id") Integer alunoId, Model model) {
		Aluno aluno = alunoService.buscarAlunoByID(alunoId);
		List<Pagamento> pagamentos = pagamentoService.buscarPagamentosDoAluno(aluno);
		model.addAttribute("aluno", alunoId);
		model.addAttribute("pagamentos", pagamentos);
		return "financeiro-aluno";
	}
	
	@GetMapping(path = "/financeiro/novoPagamento/{id}")
	public String novoPagamento(@PathVariable("id") Integer alunoId, Model model) {
		List<FormaPagamento> formaPagamentoList = Arrays.asList(FormaPagamento.values());
		Pagamento pagamento = new Pagamento();
		model.addAttribute("aluno", alunoId);
		model.addAttribute("formaPagamentoList", formaPagamentoList);
		model.addAttribute("pagamento", pagamento);
		return "financeiro-novo-pagamento-aluno";
	}
	
	@PostMapping(path = "/pagamento/aluno/salvar")
	public String salvarPagamento(@ModelAttribute("pagamento") Pagamento pagamento, @RequestParam("alunoId") Integer alunoId, Model model) {
		Aluno aluno = alunoService.buscarAlunoByID(alunoId);
		pagamento.setAluno(aluno);
		pagamentoService.efetuarPagamentoParaAluno(pagamento);
//		model.addAttribute("aluno", aluno);
		return "financeiro-aluno";
	}
}
