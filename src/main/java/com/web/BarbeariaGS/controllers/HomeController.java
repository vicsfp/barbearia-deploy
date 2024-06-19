package com.web.BarbeariaGS.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.web.BarbeariaGS.repository.AdminRepo;
import com.web.BarbeariaGS.repository.ClientesRepo;
import com.web.BarbeariaGS.repository.FuncionariosRepo;
import com.web.BarbeariaGS.services.CookieService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

     @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private ClientesRepo clienteRepo;

    @Autowired
    private FuncionariosRepo funcionariosRepo;
    
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) throws UnsupportedEncodingException{
         // Verifica se o cookie de usuário existe e está dentro do prazo de validade
    if (CookieService.getCookie(request, "usuarioEmail") != null) {
        // Se o cookie existe e está dentro do prazo de validade, verifica se é admin ou cliente
        String userEmail = CookieService.getCookie(request, "usuarioEmail");
        if (userEmail != null) {
            userEmail = URLDecoder.decode(userEmail, "UTF-8");
                }
        if (adminRepo.existsByEmail(userEmail)) {
            // Se o usuário for um admin, define o atributo "logado" como true e "admin" como true
            model.addAttribute("logado", true);
            model.addAttribute("adminCookie", true);
            model.addAttribute("clienteCookie", false);
            model.addAttribute("funcionarioCookie", false);
        } else if (clienteRepo.existsByEmail(userEmail)) {
            // Se o usuário for um cliente, define o atributo "logado" como true e "admin" como false
            model.addAttribute("logado", true);
            model.addAttribute("adminCookie", false);
            model.addAttribute("clienteCookie", true);
            model.addAttribute("funcionarioCookie", false);
        } else if (funcionariosRepo.existsByEmail(userEmail)) {
            // Se o usuário for um cliente, define o atributo "logado" como true e "admin" como false
            model.addAttribute("logado", true);
            model.addAttribute("adminCookie", false);
            model.addAttribute("clienteCookie", false);
            model.addAttribute("funcionarioCookie", true);
        } 
    } 
    // Após o cadastro bem-sucedido
     return "home/index";
    }

}