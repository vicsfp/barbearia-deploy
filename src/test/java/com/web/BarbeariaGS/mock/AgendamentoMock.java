package com.web.BarbeariaGS.mock;

import com.web.BarbeariaGS.models.Agendamento;
import com.web.BarbeariaGS.models.Cliente;
import com.web.BarbeariaGS.models.Funcionario;
import com.web.BarbeariaGS.models.Horario;
import com.web.BarbeariaGS.models.Servico;

import java.time.LocalDate;

public class AgendamentoMock {

    public static Agendamento createAgendamento() {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(1);
        agendamento.setData(LocalDate.now().plusDays(5));
        agendamento.setStatus(true);

        Cliente cliente = new Cliente();
        cliente.setId(1);
        agendamento.setCliente(cliente);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        agendamento.setFuncionario(funcionario);

        Servico servico = new Servico();
        servico.setId(1);
        agendamento.setServico(servico);

        Horario horario = new Horario();
        horario.setId(1);
        agendamento.setHorario(horario);

        return agendamento;
    }
}
