package com.web.BarbeariaGS.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.BarbeariaGS.models.Servico;
import com.web.BarbeariaGS.repository.ServicoRepo;
import com.web.BarbeariaGS.services.CookieService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ServicoController {

    @Autowired
    private ServicoRepo servicoRepo;

     //Rota para página de gerencia serviço
     @GetMapping("/gerenciar/servicos")
     public String gerenciar(HttpServletRequest request, Model model, Model modelList){
         // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            model.addAttribute("logado", true);
            model.addAttribute("adminCookie", true);
            List<Servico> servicos = (List<Servico>)servicoRepo.findAll();
            modelList.addAttribute("servicos", servicos);
            return "servicos/gerenciar";
    
        } else {
            // Se não for administrador, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }
     }

      //Rota para página de cadastro de funcionario
      @GetMapping("/gerenciar/servicos/novo")
      public String novo(HttpServletRequest request, Model model){

          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            model.addAttribute("logado", true);
            model.addAttribute("adminCookie", true);
            return "servicos/novo";
    
        } else {
            // Se não for administrador, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }

      }

      @PostMapping("/gerenciar/servicos/criar")
      public String criar(Servico servico, @RequestParam String descricao, @RequestParam String nome, @RequestParam double preco, HttpServletRequest request) {
          // Verifica se o nome contém apenas letras e espaços
          boolean contemApenasLetras = nome.matches("[a-zA-ZçÇ\\s]+");
      
          // Verifica se o nome contém apenas letras e não ultrapassa 45 caracteres
          if (!contemApenasLetras || nome.length() > 45) {
              return "redirect:/gerenciar/servicos/novo?errorNomeInvalido=O nome deve conter apenas letras e ter no máximo 45 caracteres";
          }
      
          // Verifica se o preço é um número positivo
          if (preco <= 0) {
              return "redirect:/gerenciar/servicos/novo?errorPrecoInvalido=O preço deve ser um número positivo";
          }

      
          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
          if (CookieService.getCookie(request, "usuarioId") != null) {
              // Verifica se o usuário autenticado é um administrador
              if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
                
                  // Se for administrador, permite a criação de um novo serviço
                  servico.setDescricao(descricao);
                  servico.setNome(nome);
                  servico.setPreco(preco);
                  servicoRepo.save(servico);
                  return "redirect:/gerenciar/servicos";
              } else {
                  // Se não for administrador, redireciona para a página principal
                  return "redirect:/";
              }
          } else {
              // Se o cookie não existe ou está expirado, redireciona para a página de login
              return "redirect:/login";
          }
      }
      

     //Rota para alterar cadastro
    @GetMapping("/gerenciar/servicos/{id}")
    public String buscar(@PathVariable int id, Model model, HttpServletRequest request){

        
          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            
        Optional<Servico> servicos = servicoRepo.findById(id);
        try{
        model.addAttribute("servico", servicos.get());
        }catch(Exception err){
            return "redirect:/gerenciar/servicos";
        }
        model.addAttribute("logado", true);
        model.addAttribute("adminCookie", true);
        return "servicos/editar";
    
        } else {
            // Se não for administrador, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }

    }

    @PostMapping("/gerenciar/servicos/{id}/atualizar")
public String atualizar(@PathVariable int id, @RequestParam String descricao, @RequestParam String nome, @RequestParam double preco, HttpServletRequest request) {
    // Verifica se o nome contém apenas letras e espaços
    boolean contemApenasLetras = nome.matches("[a-zA-ZçÇ\\s]+");

    // Verifica se o nome contém apenas letras e não ultrapassa 45 caracteres
    if (!contemApenasLetras || nome.length() > 45) {
        return "redirect:/gerenciar/servicos/{id}?errorNomeInvalido=O nome deve conter apenas letras e ter no máximo 45 caracteres";
    }

    // Verifica se o preço é um número positivo
    if (preco <= 0) {
        return "redirect:/gerenciar/servicos/{id}?errorPrecoInvalido=O preço deve ser um número positivo";
    }

    // Verifica se o cookie de usuário existe e está dentro do prazo de validade
    if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Busca o serviço no banco de dados pelo ID
            Optional<Servico> servicoOptional = servicoRepo.findById(id);
            if (servicoOptional.isPresent()) {
                Servico servico = servicoOptional.get();
                // Atualiza os atributos do serviço com os valores fornecidos
                servico.setDescricao(descricao);
                servico.setNome(nome);
                servico.setPreco(preco);
                // Salva o serviço atualizado no banco de dados
                servicoRepo.save(servico);
                // Redireciona para a página de listagem de serviços com mensagem de sucesso
                return "redirect:/gerenciar/servicos?atualizacaoSucesso=true";
            } else {
                // Se o serviço não for encontrado, redireciona de volta à página de listagem de serviços
                return "redirect:/gerenciar/servicos";
            }
        } else {
            // Se não for administrador, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }
}

      //Rota para excluir cadastro
    @GetMapping("/gerenciar/servicos/{id}/excluir")
    public String excluir(@PathVariable int id, HttpServletRequest request){
           // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
           
            servicoRepo.deleteById(id);
            return "redirect:/gerenciar/servicos";
    
        } else {
            // Se não for administrador, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }
       
    }
}
