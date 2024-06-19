function buscarAgendamentos() {
    var dataSelecionada = document.getElementById("dataSelecionada").value;
    // Enviar uma requisição AJAX para buscar os agendamentos para a data selecionada
    $.ajax({
        url: '/funcionarios/agendamentos',
        type: 'GET',
        data: { data: dataSelecionada },
        success: function(response) {
            // Limpar a tabela de agendamentos
            $('#tabelaAgendamentos').empty();
            // Iterar sobre os agendamentos recebidos e adicionar na tabela
            response.forEach(function(agendamento) {
                var linha = '<tr>' +
                    '<td>' + agendamento.data + '</td>' +
                    '<td>' + agendamento.horario + '</td>' +
                    '<td>' + agendamento.cliente + '</td>' +
                    '<td>' + agendamento.servico + '</td>' +
                    '</tr>';
                $('#tabelaAgendamentos').append(linha);
            });
        },
        error: function(xhr, status, error) {
            // Exibir uma mensagem de erro se ocorrer um problema na busca dos agendamentos
            console.error('Erro ao buscar agendamentos:', error);
        }
    });
}