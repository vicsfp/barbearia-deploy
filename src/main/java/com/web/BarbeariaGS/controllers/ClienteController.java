package com.web.BarbeariaGS.controllers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.BarbeariaGS.models.Agendamento;
import com.web.BarbeariaGS.models.Cliente;
import com.web.BarbeariaGS.models.Funcionario;
import com.web.BarbeariaGS.models.Servico;
import com.web.BarbeariaGS.repository.AdminRepo;
import com.web.BarbeariaGS.repository.AgendamentoRepo;
import com.web.BarbeariaGS.repository.ClientesRepo;
import com.web.BarbeariaGS.repository.FuncionariosRepo;
import com.web.BarbeariaGS.repository.ServicoRepo;
import com.web.BarbeariaGS.services.CookieService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;




@Controller
public class ClienteController {

     @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private ClientesRepo clientesRepo;

    @Autowired
    private FuncionariosRepo funcionariosRepo;

    @Autowired
    private AgendamentoRepo agendamentosRepo;


    @Autowired
    private ServicoRepo servicosRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Rota para página de agenda do cliente
@GetMapping("/clientes")
public String agendamentosCliente(HttpServletRequest request, Model model) {
    // Verifica se o cookie de usuário existe e está dentro do prazo de validade
    if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um cliente
        if (CookieService.getCookie(request, "tipoUsuario").equals("clienteCookie")) {
            // Obtém o ID do cliente logado a partir do cookie
            int clienteId = Integer.parseInt(CookieService.getCookie(request, "usuarioId"));
            
            // Busca o cliente pelo ID
            Cliente cliente = clientesRepo.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            // Busca os agendamentos do cliente
            List<Agendamento> agendamentos = agendamentosRepo.findByCliente(cliente);

            // Cria um mapa para armazenar os atributos "podeDesmarcar" para cada agendamento
            Map<Integer, Boolean> podeDesmarcarMap = new HashMap<>();

            // Verifica se o botão "Desmarcar" deve ser exibido para cada agendamento
            for (Agendamento agendamento : agendamentos) {
                LocalDateTime dataHoraAgendamento = LocalDateTime.of(agendamento.getData(), LocalTime.parse(agendamento.getHorario().getHorario()));
                
                // Calcula a data e hora limite para desmarcar (10 minutos antes do agendamento)
                LocalDateTime dataHoraLimite = dataHoraAgendamento.minusMinutes(10);
                
                // Verifica se a data e hora atual estão antes da data e hora limite
                boolean podeDesmarcar = LocalDateTime.now().isBefore(dataHoraLimite);
                
                // Adiciona o atributo "podeDesmarcar" para o agendamento no mapa
                podeDesmarcarMap.put(agendamento.getId(), podeDesmarcar);
            }

            List<Servico> servicos = (List<Servico>)servicosRepo.findAll();
        model.addAttribute("servicos", servicos);

        List<Funcionario> funcionarios = (List<Funcionario>)funcionariosRepo.findAll();
            model.addAttribute("funcionarios", funcionarios);
            
            // Adiciona o mapa de atributos "podeDesmarcar" ao modelo
            model.addAttribute("podeDesmarcarMap", podeDesmarcarMap);

            // Adiciona os agendamentos ao modelo para serem exibidos na view
            model.addAttribute("agendamentos", agendamentos);
            model.addAttribute("logado", true);
            model.addAttribute("clienteCookie", true);
            
            // Retorna a página de agendamentos do cliente
            return "/clientes/index";
        } else {
            // Se não for cliente, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }
}


     //Rota para página de cadastro de cliente
     @GetMapping("/clientes/novo")
     public String novo(){
            return "clientes/novo";

     }

     //Rota para metodo POST de cadastro de cliente
    @PostMapping("/clientes/criar")
    public String criar(Cliente cliente, @RequestParam String email, @RequestParam String senha, @RequestParam String nome){
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
            return "redirect:/clientes/novo?errorNomeInvalido=O nome deve conter apenas letras";
        }

        // Verifica se a senha atende a todos os critérios
         if (!temNumero || !temLetra || !temCaractereEspecial) {
        return "redirect:/clientes/novo?errorSenhaInsegura=A senha deve conter pelo menos 1 número, 1 letra e 1 caractere especial";
        }

         // Verifica se a senha ultrapassa 10 caracteres
         if (senha.length() > 10) {
            return "redirect:/clientes/novo?errorSenhaInvalida=Senha deve ter entre 3 e 10 caracteres";
        }

        // Verifica se a senha ultrapassa 10 caracteres
        if (senha.length() < 3) {
            return "redirect:/clientes/novo?errorSenhaInvalida=Senha deve ter entre 3 e 10 caracteres";
        }

        // Verifica se o email ultrapassa 100 caracteres
        if (email.length() > 100) {
            return "redirect:/clientes/novo?errorEmailInvalido=Email não pode ter mais de 100 caracteres";
        }
         // Verifica se o e-mail já está em uso
         if (clientesRepo.existsByEmail(email)) {
            // Se o e-mail já está em uso, redireciona de volta para a página de cadastro com uma mensagem de erro
            return "redirect:/clientes/novo?error=emailInUse";
        }

        if (adminRepo.existsByEmail(email)) {
            // Se o e-mail já está em uso na tabela administradores, redireciona de volta para a página de cadastro com uma mensagem de erro
            return "redirect:/clientes/novo?error=emailInUse";
        }

        if (funcionariosRepo.existsByEmail(email)) {
            // Se o e-mail já está em uso na tabela administradores, redireciona de volta para a página de cadastro com uma mensagem de erro
            return "redirect:/clientes/novo?error=emailInUse";
        }

        // Configura a senha do cliente como o hash gerado
        cliente.setSenha(bCryptPasswordEncoder.encode(senha));

        // Salva o cliente
        clientesRepo.save(cliente);
        // Após o cadastro bem-sucedido
        return "redirect:/login?cadastroSucesso=true";
        
    }
}