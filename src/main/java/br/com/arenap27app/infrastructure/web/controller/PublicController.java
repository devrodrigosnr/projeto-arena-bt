package br.com.arenap27app.infrastructure.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.arenap27app.application.ValidationException;
import br.com.arenap27app.domain.aluno.Aluno;
import br.com.arenap27app.domain.aluno.AlunoService;
import br.com.arenap27app.domain.enums.SituacaoAluno;
import br.com.arenap27app.util.EmailService;
import br.com.arenap27app.util.UserPasswordService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping(path = "/public")
public class PublicController {
	
	private static final String ALUNO_CADASTRO = "aluno-cadastro";

	@Autowired
	private AlunoService alunoService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserPasswordService userPasswordService;
	
    @GetMapping("/aluno/novo")
	public String novoAluno(Model model) {
    	
    	Aluno aluno = new Aluno(); 
    	
    	model.addAttribute("aluno", aluno);
    	ControllerHelper.setModoEdicao(model, false);
		return ALUNO_CADASTRO;
	}
    
    @PostMapping(path = "/aluno/salvar")
    public String salvarAluno(@ModelAttribute("aluno")@Valid Aluno aluno, Errors errors, Model model) throws ValidationException {
    	
    	try {
            alunoService.validarPreCadastro(aluno.getEmail());
        } catch (ValidationException e) {
            model.addAttribute("mensagemDeErro", e.getMessage());
            ControllerHelper.setModoEdicao(model, false); 
            return ALUNO_CADASTRO;
        }
    	
    	if(!errors.hasErrors()) {
    		try {
    			aluno.setSituacaoAluno(SituacaoAluno.ATIVO);
				alunoService.salvarAluno(aluno);
				model.addAttribute("msg", "Aluno gravado com sucesso");
			} catch (ValidationException e) {
				errors.rejectValue("email", null, e.getMessage());
			}
    	}
    	ControllerHelper.setModoEdicao(model, false); 
        return ALUNO_CADASTRO;
    }
    
    @GetMapping("/recuperar-senha")
    public String recuperarSenha() {
    	return "recuperar-senha";
    }
    
    @PostMapping("/efetuar-reset")
    public String resetarSenha(@ModelAttribute("email") String email, Model model) {
    	Aluno aluno = alunoService.buscarAlunoPorEmail(email);
    	if(!ObjectUtils.isEmpty(aluno)) {
    		String token = userPasswordService.gerarToken(aluno);
    		emailService.enviarEmail(aluno.getEmail(), token);
    		model.addAttribute("msg", "E-mail enviado com sucesso");
    	} else {
    		model.addAttribute("msgErro", "Email não encontrado");
    	}
    	return "recuperar-senha";
    }
    
    
    @GetMapping("/finalizar-recuperar-senha")
    public String exibirPaginaTrocaSenha(@RequestParam("token") String token, Model model) {
    	model.addAttribute("token", token);
    	return "finalizar-recuperar-senha";
    }
    
    @PostMapping("/efetuar-troca")
    public String efetuarReset(@RequestParam("token") String token, @ModelAttribute("novaSenha") String novaSenha, Model model) {
    	userPasswordService.trocarSenha(novaSenha, token);
    	model.addAttribute("msg", "Senha alterada com sucesso");
        return "login";
    }
    

	
}
