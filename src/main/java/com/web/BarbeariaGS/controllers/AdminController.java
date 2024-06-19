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

import com.web.BarbeariaGS.models.Admin;
import com.web.BarbeariaGS.repository.AdminRepo;
import com.web.BarbeariaGS.repository.ClientesRepo;
import com.web.BarbeariaGS.repository.FuncionariosRepo;
import com.web.BarbeariaGS.services.CookieService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {
    
    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private ClientesRepo clientesRepo;

    @Autowired
    private FuncionariosRepo funcionariosRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

   
   
     
     //Rota para página de cadastro de admin
     @GetMapping("/gerenciar/administradores/novo")
     public String novo(HttpServletRequest request, Model model){
         // Verifica se o cookie de usuário existe e está dentro do prazo de validade
         if (CookieService.getCookie(request, "usuarioId") != null) {
            // Verifica se o usuário autenticado é um administrador
            if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
                // Se for administrador, permite o acesso à página de cadastro de administradores
                model.addAttribute("logado", true);
                model.addAttribute("adminCookie", true);
                return "administradores/novo";
            } else {
                // Se não for administrador, redireciona para a página principal
                return "redirect:/";
            }
        } else {
            // Se o cookie não existe ou está expirado, redireciona para a página de login
            return "redirect:/login";
        }
     }

      //Rota para página de gerencia funcionario
     @GetMapping("/gerenciar/administradores")
     public String gerenciar(HttpServletRequest request, Model model, Model modelList){
         // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            model.addAttribute("logado", true);
                model.addAttribute("adminCookie", true);
            List<Admin> administradores = (List<Admin>)adminRepo.findAll();
            modelList.addAttribute("administradores", administradores);
            return "administradores/gerenciar";
    
        } else {
            // Se não for administrador, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }
     }


    //Rota para metodo POST de cadastro de admin
    @PostMapping("/gerenciar/administradores/criar")
    public String criar(Admin admin, @RequestParam String email, @RequestParam String senha, @RequestParam String nome, HttpServletRequest request){
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
            return "redirect:/gerenciar/administradores/novo?errorNomeInvalido=O nome deve conter apenas letras";
        }

        // Verifica se a senha atende a todos os critérios
         if (!temNumero || !temLetra || !temCaractereEspecial) {
        return "redirect:/gerenciar/administradores/novo?errorSenhaInsegura=A senha deve conter pelo menos 1 número, 1 letra e 1 caractere especial";
        }
         // Verifica se a senha ultrapassa 10 caracteres
         if (senha.length() > 10) {
            return "redirect:/gerenciar/administradores/novo?errorSenhaInvalida=Senha deve ter entre 3 e 10 caracteres";
        }

         // Verifica se a senha ultrapassa 10 caracteres
         if (senha.length() < 3) {
            return "redirect:/gerenciar/administradores/novo?errorSenhaInvalida=Senha deve ter entre 3 e 10 caracteres";
        }
        // Verifica se o email ultrapassa 100 caracteres
        if (email.length() > 100) {
            return "redirect:/gerenciar/administradores/novo?errorEmailInvalido=Email não pode ter mais de 100 caracteres";
        }
        // Verifica se o e-mail já está em uso
        if (adminRepo.existsByEmail(email)) {
            // Se o e-mail já está em uso, redireciona de volta para a página de cadastro com uma mensagem de erro
            return "redirect:/gerenciar/administradores/novo?error=emailInUse";
        }
        if(clientesRepo.existsByEmail(email)){
            // Se o e-mail já está em uso na tabela cliente, redireciona de volta para a página de cadastro com uma mensagem de erro
            return "redirect:/gerenciar/administradores/novo?error=emailInUse";
        }

        if(funcionariosRepo.existsByEmail(email)){
            // Se o e-mail já está em uso na tabela cliente, redireciona de volta para a página de cadastro com uma mensagem de erro
            return "redirect:/gerenciar/administradores/novo?error=emailInUse";
        }
       // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se for administrador, permite a criação de um novo administrador
              // Configura a senha do cliente como o hash gerado
            admin.setSenha(bCryptPasswordEncoder.encode(senha));
            adminRepo.save(admin);
            return "redirect:/gerenciar/administradores?cadastroSucesso=true";
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
    @GetMapping("/gerenciar/administradores/{id}")
    public String buscar(@PathVariable int id, Model model, HttpServletRequest request){
        // Impede a edição do administrador com ID 1
    if (id == 1) {
        return "redirect:/gerenciar/administradores?error=adminImutavel";
    }       
          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Se o cookie existe e está dentro do prazo de validade, redireciona para a página principal
            
        Optional<Admin> administrador = adminRepo.findById(id);
        try{
        model.addAttribute("administrador", administrador.get());
        model.addAttribute("logado", true);
        model.addAttribute("adminCookie", true);
        }catch(Exception err){
            return "redirect:/gerenciar/administradores";
        }
        return "administradores/editar";
    
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
     @PostMapping("/gerenciar/administradores/{id}/atualizar")
     public String atualizar(@PathVariable int id, @RequestParam String email,  @RequestParam String senha,  @RequestParam String nome, Admin admin, HttpServletRequest request){
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
    return "redirect:/gerenciar/administradores/{id}?errorNomeInvalido=O nome deve conter apenas letras";
}

// Verifica se a senha atende a todos os critérios
 if (!temNumero || !temLetra || !temCaractereEspecial) {
return "redirect:/gerenciar/administradores/{id}?errorSenhaInsegura=A senha deve conter pelo menos 1 número, 1 letra e 1 caractere especial";
}
// Verifica se o email ultrapassa 100 caracteres
if (email.length() > 100) {
    return "redirect:/gerenciar/administradores/{id}?errorEmailInvalido=Email não pode ter mais de 100 caracteres";
}
// Verifica se o e-mail já está em uso
if (adminRepo.existsByEmail(email) && adminRepo.findByEmailAndIdNot(email, id) != null) {
    // Se o e-mail já está em uso, redireciona de volta para a página de cadastro com uma mensagem de erro
    return "redirect:/gerenciar/administradores/{id}?error=emailInUse";
}
if(clientesRepo.existsByEmail(email)){
    // Se o e-mail já está em uso na tabela cliente, redireciona de volta para a página de cadastro com uma mensagem de erro
    return "redirect:/gerenciar/administradores/{id}?error=emailInUse";
}

if (funcionariosRepo.existsByEmail(email)) {
    // Se o e-mail já está em uso por outro funcionário, redireciona de volta para a página de edição com uma mensagem de erro
    return "redirect:/gerenciar/administradores/{id}?error=emailInUse";
}

 // Verifica se o cookie de usuário existe e está dentro do prazo de validade
    if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Busca o serviço no banco de dados pelo ID
            Optional<Admin> adminOptional = adminRepo.findById(id);
            if (adminOptional.isPresent()) {
                Admin adminExistente = adminOptional.get();
                adminExistente.setNome(nome);
                
                // Verifica se a senha foi alterada
                if (senha != null && !senha.isEmpty() && !senha.equals(adminExistente.getSenha())) {
                    adminExistente.setSenha(bCryptPasswordEncoder.encode(senha));
                } else {
                    adminExistente.setSenha(adminExistente.getSenha());
                }

                // Salva o administrador atualizado no banco de dados
                adminExistente.setEmail(email);
                adminRepo.save(adminExistente);
                // Redireciona para a página de listagem de serviços com mensagem de sucesso
                return "redirect:/gerenciar/administradores?atualizacaoSucesso=true";
            } else {
                // Se o serviço não for encontrado, redireciona de volta à página de listagem de serviços
                return "redirect:/gerenciar/administradores";
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
    @GetMapping("/gerenciar/administradores/{id}/excluir")
    public String excluir(@PathVariable int id, HttpServletRequest request){
       
           // Verifica se o cookie de usuário existe e está dentro do prazo de validade
       if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um administrador
        if (CookieService.getCookie(request, "tipoUsuario").equals("adminCookie")) {
            // Impede a edição do administrador com ID 1
            if (id == 1) {
                return "redirect:/gerenciar/administradores?error=adminImutavel";
            }else{
            adminRepo.deleteById(id);
            return "redirect:/gerenciar/administradores";
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
}