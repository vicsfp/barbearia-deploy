package com.web.BarbeariaGS.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.web.BarbeariaGS.models.Admin;
import com.web.BarbeariaGS.models.Cliente;
import com.web.BarbeariaGS.models.Funcionario;
import com.web.BarbeariaGS.repository.AdminRepo;
import com.web.BarbeariaGS.repository.ClientesRepo;
import com.web.BarbeariaGS.repository.FuncionariosRepo;
import com.web.BarbeariaGS.services.CookieService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class LoginController {

     @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private ClientesRepo clienteRepo;

    @Autowired
    private FuncionariosRepo funcionariosRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //Rota para página de login
    @GetMapping("/login")
    public String index(HttpServletRequest request){
         // Verifica se o cookie de usuário existe e está dentro do prazo de validade
         if (CookieService.getCookie(request, "usuarioId") != null) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            return "redirect:/";
        } else {
            // Se o cookie não existe ou está expirado, redireciona para a página de login
            return "login/index";
        }
    }
    
     // Rota para realizar login
    @PostMapping("/logar")
    public String logar(Model model, String email, String senha, Admin administrador, Cliente cliente, Funcionario funcionario, String lembrar, HttpServletResponse response) throws UnsupportedEncodingException {

 // Verifica se é um admin
 Admin adm = this.adminRepo.findByEmail(administrador.getEmail());
 String senha2 = adminRepo.findSenhaByEmail(email);
 if (adm != null && bCryptPasswordEncoder.matches(senha, senha2)) {
     int tempoLogado = (60 * 60); // 1 hora logado por padrão
     if (lembrar != null) {
         tempoLogado = (60 * 60 * 24 * 365); // 1 ano logado
     }
     CookieService.setCookie(response, "usuarioId", String.valueOf(adm.getId()), tempoLogado);
     CookieService.setCookie(response, "usuarioNome", URLEncoder.encode(adm.getNome(), "UTF-8"), tempoLogado);
     CookieService.setCookie(response, "usuarioEmail", URLEncoder.encode(adm.getEmail(), "UTF-8"), tempoLogado);
     CookieService.setCookie(response, "tipoUsuario", "adminCookie", tempoLogado);
     return "redirect:/?loginSucesso=true";
 }

 // Verifica se é um cliente
 Cliente client = this.clienteRepo.findByEmail(cliente.getEmail());
 senha2 = clienteRepo.findSenhaByEmail(email);
 if (client != null && bCryptPasswordEncoder.matches(senha, senha2)) {
     int tempoLogado = (60 * 60); // 1 hora logado por padrão
     if (lembrar != null) {
         tempoLogado = (60 * 60 * 24 * 365); // 1 ano logado
     }
     CookieService.setCookie(response, "usuarioId", String.valueOf(client.getId()), tempoLogado);
     CookieService.setCookie(response, "usuarioNome", URLEncoder.encode(client.getNome(), "UTF-8"), tempoLogado);
     CookieService.setCookie(response, "usuarioEmail", URLEncoder.encode(client.getEmail(), "UTF-8"), tempoLogado);
     CookieService.setCookie(response, "tipoUsuario", "clienteCookie", tempoLogado);
     // Após o cadastro bem-sucedido
     return "redirect:/clientes?loginSucesso=true";
 }

 // Verifica se é um admin
 Funcionario func = this.funcionariosRepo.findByEmail(funcionario.getEmail());
 senha2 = funcionariosRepo.findSenhaByEmail(email);
 if (func != null && bCryptPasswordEncoder.matches(senha, senha2)) {
     int tempoLogado = (60 * 60); // 1 hora logado por padrão
     if (lembrar != null) {
         tempoLogado = (60 * 60 * 24 * 365); // 1 ano logado
     }
     CookieService.setCookie(response, "usuarioId", String.valueOf(func.getId()), tempoLogado);
     CookieService.setCookie(response, "usuarioNome", URLEncoder.encode(func.getNome(), "UTF-8"), tempoLogado);
     CookieService.setCookie(response, "usuarioEmail", URLEncoder.encode(func.getEmail(), "UTF-8"), tempoLogado);
     CookieService.setCookie(response, "tipoUsuario", "funcionarioCookie", tempoLogado);
     return "redirect:/funcionarios?loginSucesso=true";
 }

 // Caso nenhum tipo de usuário seja autenticado
 model.addAttribute("erro", "Email ou senha inválidos");
 return "login/index";

    }

      //Rota para deslogar
      @GetMapping("/sair")
      public String sair(HttpServletResponse response, HttpServletRequest request){    
          CookieService.setCookie(response, "usuarioId", "", 0); 
          CookieService.setCookie(response, "usuarioEmail", "", 0); 
          CookieService.setCookie(response, "usuarioNome", "", 0); 
          CookieService.setCookie(response, "tipoUsuario", "", 0); 

          // Redirecionar para a página inicial com o parâmetro saidaSucesso=true
        return "redirect:/?saidaSucesso=true";
      }

}