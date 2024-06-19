package com.web.BarbeariaGS.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.web.BarbeariaGS.models.Agendamento;
import com.web.BarbeariaGS.repository.AgendamentoRepo;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepo agendamentoRepo;

    // Agendador que executa diariamente às 00:00
    @Scheduled(cron = "0 0 * * * ?")
    public void marcarAgendamentosAtrasadosComoConcluidos() {
        LocalDate tresDiasAtras = LocalDate.now().minusDays(3);
        List<Agendamento> agendamentos = agendamentoRepo.findAgendamentosAntesDe(tresDiasAtras);
        for (Agendamento agendamento : agendamentos) {
            if (agendamento.isStatus() != true) {
                agendamento.setStatus(true);
                agendamentoRepo.save(agendamento);
            }
        }
    }
}
