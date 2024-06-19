package com.web.BarbeariaGS.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.regex.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.BarbeariaGS.models.Horario;
import com.web.BarbeariaGS.repository.HorarioRepo;
import com.web.BarbeariaGS.services.CookieService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HorarioController {
    
    @Autowired
    private HorarioRepo horarioRepo;

     // Expressão regular para validar horário no formato HH:MM (de 00:00 a 23:59)
     private static final String HORARIO_REGEX = "^([01]\\d|2[0-3]):([0-5]\\d)$";

     //Rota para página de gerencia serviço
     @GetMapping("/gerenciar/horarios")
     public String gerenciar(HttpServletRequest request, Model model, Model modelList){
         // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            model.addAttribute("logado", true);
            model.addAttribute("adminCookie", true);
            List<Horario> horarios = (List<Horario>)horarioRepo.findAll();
            modelList.addAttribute("horarios", horarios);
            return "horarios/gerenciar";
    
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
      @GetMapping("/gerenciar/horarios/novo")
      public String novo(HttpServletRequest request, Model model){

          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            model.addAttribute("logado", true);
            model.addAttribute("adminCookie", true);
            return "horarios/novo";
    
        } else {
            // Se não for administrador, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }

      }

      @PostMapping("/gerenciar/horarios/criar")
      public String criar(Horario horario, @RequestParam String horario2, HttpServletRequest request) {
         // Verifica se o horário fornecido corresponde ao formato HH:MM
        if (!Pattern.matches(HORARIO_REGEX, horario2)) {
            return "redirect:/gerenciar/horarios/novo?errorHorarioInvalido=O horário deve estar no formato HH:MM";
        }

          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
          if (CookieService.getCookie(request, "usuarioId") != null) {
              // Verifica se o usuário autenticado é um administrador
              if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
                  // Se for administrador, permite a criação de um novo serviço
                  horario.setHorario(horario2);
                  horarioRepo.save(horario);
                  return "redirect:/gerenciar/horarios";
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
    @GetMapping("/gerenciar/horarios/{id}")
    public String buscar(@PathVariable int id, Model model, HttpServletRequest request){

        
          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            
        Optional<Horario> horarios = horarioRepo.findById(id);
        try{
        model.addAttribute("horario", horarios.get());
        }catch(Exception err){
            return "redirect:/gerenciar/horarios";
        }
        model.addAttribute("logado", true);
        model.addAttribute("adminCookie", true);
        return "horarios/editar";
    
        } else {
            // Se não for administrador, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }

    }

    @PostMapping("/gerenciar/horarios/{id}/atualizar")
public String atualizar(@PathVariable int id, @RequestParam String horario2, HttpServletRequest request) {
   // Verifica se o horário fornecido corresponde ao formato HH:MM
   if (!Pattern.matches(HORARIO_REGEX, horario2)) {
    return "redirect:/gerenciar/horarios/{id}?errorHorarioInvalido=O horário deve estar no formato HH:MM";
}

    // Verifica se o cookie de usuário existe e está dentro do prazo de validade
    if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Busca o serviço no banco de dados pelo ID
            Optional<Horario> horarioOptional = horarioRepo.findById(id);
            if (horarioOptional.isPresent()) {
                Horario horario = horarioOptional.get();
                horario.setHorario(horario2);
                // Salva o serviço atualizado no banco de dados
                horarioRepo.save(horario);
                // Redireciona para a página de listagem de serviços com mensagem de sucesso
                return "redirect:/gerenciar/horarios?atualizacaoSucesso=true";
            } else {
                // Se o serviço não for encontrado, redireciona de volta à página de listagem de serviços
                return "redirect:/gerenciar/horarios";
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
    @GetMapping("/gerenciar/horarios/{id}/excluir")
    public String excluir(@PathVariable int id, HttpServletRequest request){
           // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
           
            horarioRepo.deleteById(id);

            return "redirect:/gerenciar/horarios";
    
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
