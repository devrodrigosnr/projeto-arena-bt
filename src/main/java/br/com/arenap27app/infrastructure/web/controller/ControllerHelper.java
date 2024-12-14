package br.com.arenap27app.infrastructure.web.controller;

import org.springframework.ui.Model;

public class ControllerHelper {
	
	public static void setModoEdicao(Model model, boolean isEdicao) {
		model.addAttribute("modoEdicao", isEdicao);
	}

}
