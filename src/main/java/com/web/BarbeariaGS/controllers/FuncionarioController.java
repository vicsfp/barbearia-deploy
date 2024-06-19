package com.web.BarbeariaGS.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.BarbeariaGS.models.Admin;
import com.web.BarbeariaGS.models.Agendamento;
import com.web.BarbeariaGS.models.Funcionario;
import com.web.BarbeariaGS.repository.AdminRepo;
import com.web.BarbeariaGS.repository.AgendamentoRepo;
import com.web.BarbeariaGS.repository.ClientesRepo;
import com.web.BarbeariaGS.repository.FuncionariosRepo;
import com.web.BarbeariaGS.services.CookieService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FuncionarioController {
    
    @Autowired
    private FuncionariosRepo funcionariosRepo;

     @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private ClientesRepo clientesRepo;

    @Autowired
    private AgendamentoRepo agendamentosRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

  

     //Rota para página de agenda
     @GetMapping("/funcionarios")
public String agendamentosFuncionario(HttpServletRequest request, Model model) {
    // Verifica se o cookie de usuário existe e está dentro do prazo de validade
    if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("funcionarioCookie")) {
        // Obtém o ID do cliente logado a partir do cookie
        int funcionarioId = Integer.parseInt(CookieService.getCookie(request, "usuarioId"));
        
        // Busca o funcionario pelo ID
        Funcionario funcionario = funcionariosRepo.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));

        List<Funcionario> funcionarios = (List<Funcionario>)funcionariosRepo.findAll();
            model.addAttribute("funcionarios", funcionarios);

       // Busca os agendamentos do funcionario
List<Agendamento> agendamentos = agendamentosRepo.findByFuncionarioOrderByData(funcionario);

// Cria um mapa para armazenar os atributos "podeConcluir" para cada agendamento
Map<Integer, Boolean> podeConcluirMap = new HashMap<>();

for (Agendamento agendamento : agendamentos) {
    // Obtém a data e o horário do agendamento
    LocalDate dataAgendamento = agendamento.getData();
    LocalTime horarioAgendamento = LocalTime.parse(agendamento.getHorario().getHorario(), DateTimeFormatter.ofPattern("HH:mm"));

    // Calcula a data e a hora limite para poder concluir o agendamento
    LocalDateTime dataHoraLimite = LocalDateTime.of(dataAgendamento, horarioAgendamento).plusHours(1);

    // Verifica se a data e a hora atual são após a data e hora limite
    boolean podeConcluir = LocalDateTime.now().isAfter(dataHoraLimite);

    // Adiciona o atributo "podeConcluir" para o agendamento no mapa
    podeConcluirMap.put(agendamento.getId(), podeConcluir);
}

// Adiciona o mapa de atributos "podeConcluir" ao modelo
model.addAttribute("podeConcluirMap", podeConcluirMap);

        // Adiciona os agendamentos ao modelo para serem exibidos na view
        model.addAttribute("agendamentos", agendamentos); 
        model.addAttribute("logado", true);
        model.addAttribute("funcionarioCookie", true);
        // Retorna a página de agendamentos do cliente
        return "/funcionarios/index";
    } else {
        // Se não for funcionario, redireciona para a página principal
        return "redirect:/";
    }
    
}else {
    // Se o cookie não existe ou está expirado, redireciona para a página de login
    return "redirect:/login";
}
}


    //Rota para página de gerencia funcionario
     @GetMapping("/gerenciar/funcionarios")
     public String gerenciar(HttpServletRequest request, Model model, Model modelList){
         // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            model.addAttribute("logado", true);
                model.addAttribute("adminCookie", true);
            List<Funcionario> funcionarios = (List<Funcionario>)funcionariosRepo.findAll();
            modelList.addAttribute("funcionarios", funcionarios);
            return "funcionarios/gerenciar";
    
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
      @GetMapping("/gerenciar/funcionarios/novo")
      public String novo(HttpServletRequest request, Model model){

          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            model.addAttribute("logado", true);
                model.addAttribute("adminCookie", true);
            return "funcionarios/novo";
    
        } else {
            // Se não for administrador, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }

      }

      //Rota para metodo POST de cadastro de funcionario
    @PostMapping("/gerenciar/funcionarios/criar")
    public String criar(Funcionario funcionario, @RequestParam String email,  @RequestParam String senha,  @RequestParam String nome, HttpServletRequest request){
        // Verifica se a senha contém pelo menos 1 número
        boolean temNumero = senha.matches(".*[0-9].*");

        // Verifica se a senha contém pelo menos 1 letra
        boolean temLetra = senha.matches(".*[a-zA-ZçÇ].*");

        // Verifica se a senha contém pelo menos 1 caractere especial
        boolean temCaractereEspecial = senha.matches(".*[@#$%^&+=?!].*");

                // Verifica se o nome contém apenas letras e espaços
        boolean contemApenasLetras = nome.matches("[a-zA-ZçÇ\\s]+");

        // Verifica se o nome contém apenas letras
        if (!contemApenasLetras) {
            return "redirect:/gerenciar/funcionarios/novo?errorNomeInvalido=O nome deve conter apenas letras";
        }

        // Verifica se a senha atende a todos os critérios
         if (!temNumero || !temLetra || !temCaractereEspecial) {
        return "redirect:/gerenciar/funcionarios/novo?errorSenhaInsegura=A senha deve conter pelo menos 1 número, 1 letra e 1 caractere especial";
        }
         // Verifica se a senha ultrapassa 10 caracteres
         if (senha.length() > 10) {
            return "redirect:/gerenciar/funcionarios/novo?errorSenhaInvalida=Senha deve ter entre 3 e 10 caracteres";
        }

         // Verifica se a senha ultrapassa 10 caracteres
         if (senha.length() < 3) {
            return "redirect:/gerenciar/funcionarios/novo?errorSenhaInvalida=Senha deve ter entre 3 e 10 caracteres";
        }
        // Verifica se o email ultrapassa 100 caracteres
        if (email.length() > 100) {
            return "redirect:/gerenciar/funcionarios/novo?errorEmailInvalido=Email não pode ter mais de 100 caracteres";
        }
        // Verifica se o e-mail já está em uso
        if (adminRepo.existsByEmail(email)) {
            // Se o e-mail já está em uso, redireciona de volta para a página de cadastro com uma mensagem de erro
            return "redirect:/gerenciar/funcionarios/novo?error=emailInUse";
        }
        if(clientesRepo.existsByEmail(email)){
            // Se o e-mail já está em uso na tabela cliente, redireciona de volta para a página de cadastro com uma mensagem de erro
            return "redirect:/gerenciar/funcionarios/novo?error=emailInUse";
        }

        if(funcionariosRepo.existsByEmail(email)){
            // Se o e-mail já está em uso na tabela cliente, redireciona de volta para a página de cadastro com uma mensagem de erro
            return "redirect:/gerenciar/funcionarios/novo?error=emailInUse";
        }
        
        
          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
    if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
          
            // Obter o valor do cookie "usuarioId" e convertê-lo para um número inteiro
        String adminIdStr = CookieService.getCookie(request, "usuarioId");
        // Verifica se o adminIdStr não é nulo nem vazio antes de tentar converter para inteiro
        if (adminIdStr == null || adminIdStr.isEmpty()) {
            // Se o cookie não foi encontrado ou está vazio, redirecionar para a página de login
            return "redirect:/login";
        }

        // Converte a string do cookie para um número inteiro
        int adminId;
        try {
            adminId = Integer.parseInt(adminIdStr);
        } catch (NumberFormatException e) {
            // Se ocorrer uma exceção ao converter a string para inteiro, trata o erro de alguma forma apropriada
            // Aqui você pode redirecionar para uma página de erro ou fazer outra ação adequada
            e.printStackTrace();
            return "redirect:/login";
        }

            // Verifica se o admin com o ID fornecido existe
        Optional<Admin> adminOptional = adminRepo.findById(adminId);
        if (adminOptional.isPresent()) {
            funcionario.setAdminCriacao(adminOptional.get());
            // Garante que o adminCriacao não seja alterado durante a atualização
            funcionario.setAdminEdicao(funcionario.getAdminEdicao());
            
            // Configura a senha do cliente como o hash gerado
            funcionario.setSenha(bCryptPasswordEncoder.encode(senha));

            funcionariosRepo.save(funcionario);
            return "redirect:/gerenciar/funcionarios?cadastroSucesso=true";
        } else {
            return "redirect:/gerenciar/funcionarios/novo?error=adminNotFound";
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

     //Rota para alterar cadastro
    @GetMapping("/gerenciar/funcionarios/{id}")
    public String buscar(@PathVariable int id, Model model, HttpServletRequest request){

        
          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            
        Optional<Funcionario> funcionario = funcionariosRepo.findById(id);
        try{
        model.addAttribute("funcionario", funcionario.get());
        model.addAttribute("logado", true);
        model.addAttribute("adminCookie", true);
        }catch(Exception err){
            return "redirect:/gerenciar/funcionarios";
        }
        return "funcionarios/editar";
    
        } else {
            // Se não for administrador, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }

    }

     //Rota para alterar cadastro no banco
     @PostMapping("/gerenciar/funcionarios/{id}/atualizar")
     public String atualizar(@PathVariable int id, @RequestParam String email,  @RequestParam String senha,  @RequestParam String nome, Funcionario funcionario, HttpServletRequest request){
// Verifica se a senha contém pelo menos 1 número
boolean temNumero = senha.matches(".*[0-9].*");

// Verifica se a senha contém pelo menos 1 letra
boolean temLetra = senha.matches(".*[a-zA-ZçÇ].*");

// Verifica se a senha contém pelo menos 1 caractere especial
boolean temCaractereEspecial = senha.matches(".*[@#$%^&+=?!].*");

// Verifica se o nome contém apenas letras e espaços
boolean contemApenasLetras = nome.matches("[a-zA-ZçÇ\\s]+");

// Verifica se o nome contém apenas letras
if (!contemApenasLetras) {
    return "redirect:/gerenciar/funcionarios/{id}?errorNomeInvalido=O nome deve conter apenas letras";
}

// Verifica se a senha atende a todos os critérios
 if (!temNumero || !temLetra || !temCaractereEspecial) {
return "redirect:/gerenciar/funcionarios/{id}?errorSenhaInsegura=A senha deve conter pelo menos 1 número, 1 letra e 1 caractere especial";
}
// Verifica se o email ultrapassa 100 caracteres
if (email.length() > 100) {
    return "redirect:/gerenciar/funcionarios/{id}?errorEmailInvalido=Email não pode ter mais de 100 caracteres";
}
// Verifica se o e-mail já está em uso
if (adminRepo.existsByEmail(email)) {
    // Se o e-mail já está em uso, redireciona de volta para a página de cadastro com uma mensagem de erro
    return "redirect:/gerenciar/funcionarios/{id}?error=emailInUse";
}
if(clientesRepo.existsByEmail(email)){
    // Se o e-mail já está em uso na tabela cliente, redireciona de volta para a página de cadastro com uma mensagem de erro
    return "redirect:/gerenciar/funcionarios/{id}?error=emailInUse";
}

if (funcionariosRepo.existsByEmail(email) && funcionariosRepo.findByEmailAndIdNot(email, id) != null) {
    // Se o e-mail já está em uso por outro funcionário, redireciona de volta para a página de edição com uma mensagem de erro
    return "redirect:/gerenciar/funcionarios/{id}?error=emailInUse";
}


          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
          if (CookieService.getCookie(request, "usuarioId") != null) {
            // Verifica se o usuário autenticado é um administrador
            if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
                // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            // Obter o valor do cookie "usuarioId" e convertê-lo para um número inteiro
    String adminIdStr = CookieService.getCookie(request, "usuarioId");
    // Verifica se o adminIdStr não é nulo nem vazio antes de tentar converter para inteiro
    if (adminIdStr == null || adminIdStr.isEmpty()) {
        // Se o cookie não foi encontrado ou está vazio, redirecionar para a página de login
        return "redirect:/login";
    }
     // Converte a string do cookie para um número inteiro
     int adminId;
     try {
         adminId = Integer.parseInt(adminIdStr);
     } catch (NumberFormatException e) {
         // Se ocorrer uma exceção ao converter a string para inteiro, trata o erro de alguma forma apropriada
         // Aqui você pode redirecionar para uma página de erro ou fazer outra ação adequada
         e.printStackTrace();
         return "redirect:/login";
     }

        if(!funcionariosRepo.exist(id)){
         return "redirect:/gerenciar/funcionarios";
        }
        
       // Verifica se o admin com o ID fornecido existe
Optional<Admin> adminOptional = adminRepo.findById(adminId);
Optional<Funcionario> funcionarioOptional = funcionariosRepo.findById(id);
if (adminOptional.isPresent()) {
    Funcionario funcionarioExistente = funcionarioOptional.get();
    // Atribui o adminCriacao do funcionário existente ao funcionário que está sendo atualizado
    funcionario.setAdminCriacao(funcionarioExistente.getAdminCriacao());
    funcionario.setAdminEdicao(adminOptional.get());

     // Verifica se a senha foi alterada
     if (senha != null && !senha.isEmpty() && !senha.equals(funcionarioExistente.getSenha())) {
        funcionario.setSenha(bCryptPasswordEncoder.encode(senha));
    } else {
        funcionario.setSenha(funcionarioExistente.getSenha());
    }
    // Configura a senha do funcionario como o hash gerado
    funcionariosRepo.save(funcionario);
    return "redirect:/gerenciar/funcionarios";
} else {
    return "redirect:/gerenciar/funcionarios?error=adminNotFound";
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
    @GetMapping("/gerenciar/funcionarios/{id}/excluir")
    public String excluir(@PathVariable int id, HttpServletRequest request){
           // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
           
            funcionariosRepo.deleteById(id);
            return "redirect:/gerenciar/funcionarios";
    
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