package com.web.BarbeariaGS.controllers;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.BarbeariaGS.models.Agendamento;
import com.web.BarbeariaGS.models.Cliente;
import com.web.BarbeariaGS.models.Funcionario;
import com.web.BarbeariaGS.repository.AgendamentoRepo;
import com.web.BarbeariaGS.repository.ClientesRepo;
import com.web.BarbeariaGS.repository.FuncionariosRepo;

import com.web.BarbeariaGS.services.CookieService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RelatorioFinanceiroController {

     @Autowired
    private AgendamentoRepo agendamentoRepo;

    @Autowired
    private FuncionariosRepo funcionariosRepo;

    @Autowired
    private ClientesRepo clientesRepo;

    @GetMapping("/funcionarios/relatorio-financeiro")
    public String getRelatorioFinanceiroFuncionario(
            @RequestParam(value = "filterType", required = false) String filterType,
            @RequestParam(value = "filterDay", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate filterDay,
            @RequestParam(value = "filterMonth", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth filterMonth,
            @RequestParam(value = "filterYear", required = false) Integer filterYear,
            Model model, HttpServletRequest request) {
    
        // Verifica se o cookie de usuário existe e está dentro do prazo de validade
        if (CookieService.getCookie(request, "usuarioId") != null) {
            // Verifica se o usuário autenticado é um administrador
            if (CookieService.getCookie(request, "tipoUsuario").equals("funcionarioCookie")) {
                // Obtém o ID do cliente logado a partir do cookie
                int funcionarioId = Integer.parseInt(CookieService.getCookie(request, "usuarioId"));
    
                // Busca o funcionario pelo ID
                Funcionario funcionario = funcionariosRepo.findById(funcionarioId)
                        .orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));
    
                List<Agendamento> agendamentos = new ArrayList<>();
                Double totalValorServicos = 0.0;
    
                // Verifica se algum filtro foi aplicado
                if (filterType != null) {
                    switch (filterType) {
                        case "day":
                            if (filterDay != null) {
                                agendamentos = agendamentoRepo.findByDataAndFuncionarioOrderByDataWithStatus1(filterDay, funcionario);
                                totalValorServicos = agendamentoRepo.findTotalValueByFuncionarioAndData(funcionarioId, filterDay);
                            }
                            break;
                        case "month":
                            if (filterMonth != null) {
                                LocalDate startOfMonth = filterMonth.atDay(1);
                                LocalDate endOfMonth = filterMonth.atEndOfMonth();
                                agendamentos = agendamentoRepo.findByFuncionarioAndDateRange(funcionarioId, startOfMonth, endOfMonth);
                                totalValorServicos = agendamentoRepo.findTotalValueByFuncionarioAndDateRange(funcionarioId, startOfMonth, endOfMonth);
                            }
                            break;
                        case "year":
                            if (filterYear != null) {
                                LocalDate startOfYear = LocalDate.of(filterYear, 1, 1);
                                LocalDate endOfYear = LocalDate.of(filterYear, 12, 31);
                                agendamentos = agendamentoRepo.findByFuncionarioAndDateRange(funcionarioId, startOfYear, endOfYear);
                                totalValorServicos = agendamentoRepo.findTotalValueByFuncionarioAndDateRange(funcionarioId, startOfYear, endOfYear);
                            }
                            break;
                    }
                } 
    
                if (totalValorServicos == null) {
                    totalValorServicos = 0.0;
                }
                 // Lógica para buscar agendamentos e calcular totalValorServicos

                model.addAttribute("filterUsed", true);
                model.addAttribute("filterType", filterType);

                if ("day".equals(filterType)) {
                    model.addAttribute("filterValue", filterDay);
                } else if ("month".equals(filterType)) {
                    model.addAttribute("filterValue", filterMonth);
                } else if ("year".equals(filterType)) {
                    model.addAttribute("filterValue", filterYear);
                }
    
                model.addAttribute("agendamentos", agendamentos);
                model.addAttribute("totalValorServicos", totalValorServicos);
                model.addAttribute("logado", true);
                 model.addAttribute("funcionarioCookie", true);
                return "/funcionarios/relatorioFinanceiro";
            }
            return "redirect:/";
        }
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }
    
    @GetMapping("/clientes/relatorio-financeiro")
    public String getRelatorioFinanceiroCliente(
            @RequestParam(value = "filterType", required = false) String filterType,
            @RequestParam(value = "filterDay", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate filterDay,
            @RequestParam(value = "filterMonth", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth filterMonth,
            @RequestParam(value = "filterYear", required = false) Integer filterYear,
            Model model, HttpServletRequest request) {
    
        // Verifica se o cookie de usuário existe e está dentro do prazo de validade
        if (CookieService.getCookie(request, "usuarioId") != null) {
            // Verifica se o usuário autenticado é um administrador
            if (CookieService.getCookie(request, "tipoUsuario").equals("clienteCookie")) {
                // Obtém o ID do cliente logado a partir do cookie
                int clienteId = Integer.parseInt(CookieService.getCookie(request, "usuarioId"));
    
                // Busca o funcionario pelo ID
                Cliente cliente = clientesRepo.findById(clienteId)
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    
                List<Agendamento> agendamentos = new ArrayList<>();
                Double totalValorServicos = 0.0;
    
                // Verifica se algum filtro foi aplicado
                if (filterType != null) {
                    switch (filterType) {
                        case "day":
                            if (filterDay != null) {
                                agendamentos = agendamentoRepo.findByDataAndClienteOrderByDataWithStatus1(filterDay, cliente);
                                totalValorServicos = agendamentoRepo.findTotalValueByClienteAndData(clienteId, filterDay);
                            }
                            break;
                        case "month":
                            if (filterMonth != null) {
                                LocalDate startOfMonth = filterMonth.atDay(1);
                                LocalDate endOfMonth = filterMonth.atEndOfMonth();
                                agendamentos = agendamentoRepo.findByClienteAndDateRange(clienteId, startOfMonth, endOfMonth);
                                totalValorServicos = agendamentoRepo.findTotalValueByClienteAndDateRange(clienteId, startOfMonth, endOfMonth);
                            }
                            break;
                        case "year":
                            if (filterYear != null) {
                                LocalDate startOfYear = LocalDate.of(filterYear, 1, 1);
                                LocalDate endOfYear = LocalDate.of(filterYear, 12, 31);
                                agendamentos = agendamentoRepo.findByClienteAndDateRange(clienteId, startOfYear, endOfYear);
                                totalValorServicos = agendamentoRepo.findTotalValueByClienteAndDateRange(clienteId, startOfYear, endOfYear);
                            }
                            break;
                    }
                } 
    
                if (totalValorServicos == null) {
                    totalValorServicos = 0.0;
                }
                 // Lógica para buscar agendamentos e calcular totalValorServicos

                model.addAttribute("filterUsed", true);
                model.addAttribute("filterType", filterType);

                if ("day".equals(filterType)) {
                    model.addAttribute("filterValue", filterDay);
                } else if ("month".equals(filterType)) {
                    model.addAttribute("filterValue", filterMonth);
                } else if ("year".equals(filterType)) {
                    model.addAttribute("filterValue", filterYear);
                }
    
                model.addAttribute("agendamentos", agendamentos);
                model.addAttribute("totalValorServicos", totalValorServicos);
                model.addAttribute("logado", true);
                 model.addAttribute("clienteCookie", true);
                return "/clientes/relatorioFinanceiro";
            }
            return "redirect:/";
        }
        // Se o cookie não existe ou está expirado, redireciona para a página de login
        return "redirect:/login";
    }
    
}
