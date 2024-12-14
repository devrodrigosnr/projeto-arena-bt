package br.com.arenap27app.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.arenap27app.application.ValidationException;
import br.com.arenap27app.domain.agendamento.AgendamentoService;
import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aluno.AlunoRepository;
import br.com.arenap27app.domain.aluno.AlunoService;
import br.com.arenap27app.util.SecurityUtils;
import jakarta.validation.Valid;

@Controller
@RequestMapping(path = "/aluno")
public class AlunoController {
	
	@Autowired
	private AlunoRepository alunoRepository;
	
	@Autowired
	private AlunoService alunoService;
	
	@Autowired
	private AgendamentoService agendamentoService;
	
	@GetMapping(path = "/home")
	public String home(Model model) {
		Integer alunoId = SecurityUtils.loggedAluno().getId();
		Aluno aluno = alunoRepository.findById(alunoId).orElseThrow();
		int qtdAulasRealizadasMes = agendamentoService.verificaQuantidadeDeAulasRealizadasNoMes(aluno);
		model.addAttribute("qtdAulasRealizadasMes", qtdAulasRealizadasMes);
		return "aluno-home";
	}
	
	@GetMapping(path = "/agendamento")
	public String alunoAgendamento() {
		return "aluno-agendamento";
	}
	
	@GetMapping("/editar")
	public String editar(Model model) {
		Integer alunoId = SecurityUtils.loggedAluno().getId();
		
		Aluno aluno = alunoRepository.findById(alunoId).orElseThrow();
		model.addAttribute("aluno", aluno);
		ControllerHelper.setModoEdicao(model, true);
		return "aluno-cadastro";
	}
	
    @PostMapping(path = "/salvar")
    public String salvarAluno(@ModelAttribute("aluno")@Valid Aluno aluno, Errors errors, Model model) {
    	
    	if(!errors.hasErrors()) {
    		try {
    			Aluno alunoDb = alunoRepository.findById(aluno.getId()).orElseThrow();
    			
    			alunoDb.setNome(aluno.getNome());
    			alunoDb.setCpf(aluno.getCpf());
    			alunoDb.setEmail(aluno.getEmail());
    			alunoDb.setDataNascimento(aluno.getDataNascimento());
    			alunoDb.setTelefone(aluno.getTelefone());
    			alunoDb.setTelefoneEmergencia(aluno.getTelefoneEmergencia());
    			alunoDb.setBairro(aluno.getBairro());
    			
				alunoService.salvarAluno(alunoDb);
				model.addAttribute("msg", "Aluno gravado com sucesso");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
    	}
    	ControllerHelper.setModoEdicao(model, true); 
        return "aluno-cadastro";
    }

}
