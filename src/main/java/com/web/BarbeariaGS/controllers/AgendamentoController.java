package com.web.BarbeariaGS.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.BarbeariaGS.models.Agendamento;
import com.web.BarbeariaGS.models.Cliente;
import com.web.BarbeariaGS.models.Funcionario;
import com.web.BarbeariaGS.models.Horario;
import com.web.BarbeariaGS.models.Servico;
import com.web.BarbeariaGS.repository.AgendamentoRepo;
import com.web.BarbeariaGS.repository.ClientesRepo;
import com.web.BarbeariaGS.repository.FuncionariosRepo;
import com.web.BarbeariaGS.repository.HorarioRepo;
import com.web.BarbeariaGS.repository.ServicoRepo;
import com.web.BarbeariaGS.services.CookieService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AgendamentoController {

    @Autowired
    private AgendamentoRepo agendamentoRepo;

    @Autowired
    private FuncionariosRepo funcionariosRepo;
    
    @Autowired
    private ClientesRepo clienteRepo;

    @Autowired
    private HorarioRepo horarioRepo;

    @Autowired
    private ServicoRepo servicoRepo;

    @GetMapping("/clientes/novo/agendamento")
    public String index(Model model,Model modelList, HttpServletRequest request,
                        @RequestParam("funcionarioId") int funcionarioId,
                        @RequestParam("servicoId") int servicoId,
                        @RequestParam(name = "dataAgendamento", required = false) Date dataAgendamento) {
        // Verifica se o cookie de usuário existe e está dentro do prazo de validade
        if (CookieService.getCookie(request, "usuarioId") != null) {
            // Verifica se o usuário autenticado é um cliente
            if (CookieService.getCookie(request, "tipoUsuario").equals("clienteCookie")) {
                
                
                // Busca o funcionário pelo ID
                Funcionario funcionario = funcionariosRepo.findById(funcionarioId)
                        .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    
                // Busca o serviço pelo ID
                Servico servico = servicoRepo.findById(servicoId)
                        .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
    
    model.addAttribute("servico", servico);
    List<Object[]> horarios = agendamentoRepo.findHorariosVagosByFuncionarioAndData(funcionarioId, dataAgendamento);
    // Verifica se foi fornecida uma data de agendamento
     model.addAttribute("funcionario", funcionario);
        modelList.addAttribute("horarios", horarios);
        System.out.println(dataAgendamento);
        model.addAttribute("dataAgendamento", dataAgendamento); // Adiciona a data para uso na view
        model.addAttribute("logado", true);
        model.addAttribute("clienteCookie", true);

    
                return "clientes/agendamento";
            } else {
                // Se não for cliente, redireciona para a página principal
                return "redirect:/";
            }
        } else {
            // Se o cookie não existe ou está expirado, redireciona para a página de login
            return "redirect:/login";
        }
    }

    @GetMapping("/clientes/novo/agendamento/selecao-horarios")
public String abrirModalSelecaoHorarios(Model model, @RequestParam("funcionarioId") int funcionarioId,
                                        @RequestParam("servicoId") int servicoId,
                                        @RequestParam("dataAgendamento") String dataAgendamento) {
    // Busca o funcionário pelo ID
    Funcionario funcionario = funcionariosRepo.findById(funcionarioId)
            .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

    // Busca o serviço pelo ID
    Servico servico = servicoRepo.findById(servicoId)
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

    // Converte a data de String para LocalDate
    LocalDate data = LocalDate.parse(dataAgendamento);

  // Formatar a data para o formato "dia/mês/ano" (DD/MM/YYYY)
  DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  DateTimeFormatter formatterSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  String dataFormatada = LocalDate.parse(dataAgendamento, formatterEntrada).format(formatterSaida);

    // Busca os horários vagos para o funcionário e data
    List<Object[]> horarios = agendamentoRepo.findHorariosVagosByFuncionarioAndData(funcionarioId, Date.valueOf(data));

     // Filtra os horários que já passaram, se a data de agendamento for hoje
    if (data.equals(LocalDate.now())) {
        LocalTime now = LocalTime.now();
        horarios = horarios.stream()
                           .filter(horario -> LocalTime.parse(horario[1].toString()).isAfter(now))
                           .collect(Collectors.toList());
    }

    model.addAttribute("funcionario", funcionario);
    model.addAttribute("servico", servico);
    model.addAttribute("horarios", horarios);
    model.addAttribute("dataAgendamento", data);
    model.addAttribute("dataFormatada", dataFormatada);
    model.addAttribute("logado", true);
    model.addAttribute("clienteCookie", true);

    return "clientes/selecaoHorarios"; // Nome da sua view do modal de seleção de horários
}




@PostMapping("/clientes/novo/agendamento/selecao-horarios/criar")
public String criarAgendamento(Model model, HttpServletRequest request,
                               @RequestParam("funcionarioId") int funcionarioId,
                               @RequestParam("servicoId") int servicoId,
                               @RequestParam("dataAgendamento") String dataAgendamento,
                               @RequestParam("horarioId") int horarioId) {
    // Recupera o ID do cliente do cookie
    int clienteId = Integer.parseInt(CookieService.getCookie(request, "usuarioId"));
    // Verifica se o cookie de usuário existe e está dentro do prazo de validade
    if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um cliente
        if (CookieService.getCookie(request, "tipoUsuario").equals("clienteCookie")) {
             // Busca o cliente pelo ID (assumindo que exista um repository para clientes)
            Cliente cliente = clienteRepo.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            // Busca o funcionário pelo ID
            Funcionario funcionario = funcionariosRepo.findById(funcionarioId)
                    .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

            // Busca o horário pelo ID
            Horario horario = horarioRepo.findById(horarioId)
                    .orElseThrow(() -> new RuntimeException("Horário não encontrado"));

            // Busca o serviço pelo ID
            Servico servico = servicoRepo.findById(servicoId)
                    .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

            // Cria uma instância de Agendamento com os dados
            Agendamento agendamento = new Agendamento();
            agendamento.setFuncionario(funcionario);
            agendamento.setServico(servico);
            agendamento.setData(LocalDate.parse(dataAgendamento));
            agendamento.setHorario(horario);
            agendamento.setCliente(cliente);

            // Salva o agendamento no banco de dados
            agendamentoRepo.save(agendamento);

            // Redireciona para outra página após o sucesso
            return "redirect:/clientes";
        } else {
            // Se não for cliente, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }
}

@GetMapping("/funcionarios/agendamentos")
public String getAgendamentosPorData(@RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                     Model model, HttpServletRequest request) {

    // Verifica se o cookie de usuário existe e está dentro do prazo de validade
    if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um funcionário
        if (CookieService.getCookie(request, "tipoUsuario").equals("funcionarioCookie")) {

            // Obtém o ID do funcionário logado a partir do cookie
            int funcionarioId = Integer.parseInt(CookieService.getCookie(request, "usuarioId"));

            // Busca o funcionário pelo ID
            Funcionario funcionario = funcionariosRepo.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));

            // Busca os agendamentos do funcionário na data especificada
            List<Agendamento> agendamentos = agendamentoRepo.findByDataAndFuncionarioOrderByData(data, funcionario);

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

            // Adiciona os agendamentos ao modelo
            model.addAttribute("agendamentos", agendamentos);
            model.addAttribute("dataSelecionada", data);
        }
        return "funcionarios/index";
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }
}


   //Rota para excluir cadastro
   @GetMapping("/clientes/{id}/desmarcar")
   public String clienteDesmarcar(@PathVariable int id, HttpServletRequest request){
          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
      if (CookieService.getCookie(request, "usuarioId") != null) {
       // Verifica se o usuário autenticado é um administrador
       if (CookieService.getCookie(request, "tipoUsuario").equals("clienteCookie")) {
          
           agendamentoRepo.deleteById(id);
           return "redirect:/clientes";
   
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
   @GetMapping("/funcionarios/{id}/desmarcar")
   public String funcionarioDesmarcar(@PathVariable int id, HttpServletRequest request){
          // Verifica se o cookie de usuário existe e está dentro do prazo de validade
      if (CookieService.getCookie(request, "usuarioId") != null) {
       // Verifica se o usuário autenticado é um administrador
       if (CookieService.getCookie(request, "tipoUsuario").equals("funcionarioCookie")) {
          
           agendamentoRepo.deleteById(id);
           return "redirect:/funcionarios";
   
       } else {
           // Se não for administrador, redireciona para a página principal
           return "redirect:/";
       }
   } else {
       // Se o cookie não existe ou está expirado, redireciona para a página de login
       return "redirect:/login";
   }
      
   }



@PostMapping("/funcionarios/{id}/marcar-concluido")
public String funcionarioMarcarConcluido(@PathVariable int id, HttpServletRequest request) {
    // Verifica se o cookie de usuário existe e está dentro do prazo de validade
    if (CookieService.getCookie(request, "usuarioId") != null) {
        // Verifica se o usuário autenticado é um funcionário
        if (CookieService.getCookie(request, "tipoUsuario").equals("funcionarioCookie")) {
            // Verifique se o agendamento com o ID fornecido existe
            Optional<Agendamento> agendamentoOptional = agendamentoRepo.findById(id);
            if (agendamentoOptional.isPresent()) {
                Agendamento agendamento = agendamentoOptional.get();
                // Marque o horário como concluído
                agendamento.setStatus(true);
                agendamentoRepo.save(agendamento);
                return "redirect:/funcionarios"; // Redireciona para a página de funcionários
            } else {
                // Se o agendamento não for encontrado, redireciona com uma mensagem de erro
                return "redirect:/funcionarios?error=agendamentoNotFound";
            }
        } else {
            // Se não for funcionário, redireciona para a página principal
            return "redirect:/";
        }
    } else {
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }
}


}
