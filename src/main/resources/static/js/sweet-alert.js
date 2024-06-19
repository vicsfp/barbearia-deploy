// NAVBAR

// Adicionando evento de clique ao botão "Agendar"
if(document.getElementById("btnAgendar")){
    document.getElementById("btnAgendar").addEventListener("click", function() {
        // Redirecionando para a página de login
        window.location.href = "/login";
        
      });
    }
    
      // Função para redirecionar para a página de clientes
    function redirecionarParaClientes() {
      window.location.href = "/clientes";
    }
    
     // Função para redirecionar para a página de clientes
     function redirecionarParaFuncionarios() {
      window.location.href = "/funcionarios";
    }
    
 // Função para redirecionar para a página de administradores
function redirecionarParaEscolhaGerenciar() {
    // Exibe uma caixa de diálogo personalizada
    Swal.fire({
        title: 'O que você gostaria de gerenciar?',
        icon: 'question',
        showCancelButton: true,
        showConfirmButton: false,
        cancelButtonText: 'Cancelar',
        cancelButtonColor: '#d33',
        html:
        '<div class="swal2-row">' +
        '<button class="swal2-styled swal2-confirm admin" role="button" tabindex="0" aria-label="">Administradores</button>' +
        '<button class="swal2-styled swal2-confirm employee" role="button" tabindex="0" aria-label="">Funcionários</button>' +
        '<button class="swal2-styled swal2-confirm schedule" role="button" tabindex="0" aria-label="">Horários</button>' +
        '<button class="swal2-styled swal2-confirm service" role="button" tabindex="0" aria-label="">Serviços</button>' +
        '</div>',
       
        width: window.innerWidth <= 1366 ? '70%' : '50%', // Aumenta a largura da caixa de diálogo
        heightAuto: window.innerWidth <= 1366 ? false : true, // Diminui a altura da caixa de diálogo
        iconColor: '#1a242f', // Muda a cor do ícone de ponto de interrogação
        focusConfirm: false,
        didOpen: () => {
            const adminButton = document.querySelector('.admin');
            const employeeButton = document.querySelector('.employee');
            const scheduleButton = document.querySelector('.schedule');
            const serviceButton = document.querySelector('.service');

            adminButton.addEventListener('click', function() {
                window.location.href = "/gerenciar/administradores";
            });

            employeeButton.addEventListener('click', function() {
                window.location.href = "/gerenciar/funcionarios";
            });

            scheduleButton.addEventListener('click', function() {
                window.location.href = "/gerenciar/horarios";
            });

            serviceButton.addEventListener('click', function() {
                window.location.href = "/gerenciar/servicos";
            });
        }
    });
}


    
    
    // Sair da conta logada
    
    function confirmarSair() {
        // Exibe uma caixa de diálogo personalizada
        Swal.fire({
            title: 'Tem certeza que deseja sair?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Sim',
            cancelButtonText: 'Cancelar',
            iconColor: '#1a242f'
        }).then((result) => {
            // Se o usuário clicar em "Sim", exclui o cookie e redireciona para a rota de logout
            if (result.isConfirmed) {
                document.cookie = "usuarioId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                window.location.href = "/sair";
            }
        });
    }
    
    // Cadastro de administrador
    
    if(document.getElementById('cadastroForm')){
    document.getElementById('cadastroForm').addEventListener('submit', function(event) {
        event.preventDefault(); // Impede o envio padrão do formulário
    
        // Exibe uma caixa de diálogo personalizada
        Swal.fire({
            title: 'Tem certeza que deseja cadastrar novo admin?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Cadastrar',
            cancelButtonText: 'Cancelar',
            iconColor: '#1a242f'
        }).then((result) => {
            // Se o usuário confirmar, envia o formulário via AJAX
            if (result.isConfirmed) {
                enviarFormularioAdmin(); // Função para enviar o formulário
            }
        });
    });
    }
    
    function enviarFormularioAdmin() {
        var form = document.getElementById('cadastroForm');
        var formData = new FormData(form);
    
        // Envia o formulário via AJAX
        fetch(form.action, {
            method: form.method,
            body: formData
        })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url; // Redireciona se necessário
            }
        })
        .catch(error => console.error('Erro:', error));
    }
    
    // Cadastro de cliente
    
    if(document.getElementById('cadastroFormUser')){
    document.getElementById('cadastroFormUser').addEventListener('submit', function(event) {
        event.preventDefault(); // Impede o envio padrão do formulário
    
        // Exibe uma caixa de diálogo personalizada
        Swal.fire({
            title: 'Tem certeza que deseja se cadastrar?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Cadastrar',
            cancelButtonText: 'Cancelar',
            iconColor: '#1a242f'
        }).then((result) => {
            // Se o usuário confirmar, envia o formulário via AJAX
            if (result.isConfirmed) {
                enviarFormularioCliente(); // Função para enviar o formulário
            }
        });
    });
    }
    
    function enviarFormularioCliente() {
        var form = document.getElementById('cadastroFormUser');
        var formData = new FormData(form);
    
        // Envia o formulário via AJAX
        fetch(form.action, {
            method: form.method,
            body: formData
        })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url; // Redireciona se necessário
            }
        })
        .catch(error => console.error('Erro:', error));
    }
    
    // Editar Funcionario
    
    if(document.getElementById('cadastroFormEdit')){
        document.getElementById('cadastroFormEdit').addEventListener('submit', function(event) {
            event.preventDefault(); // Impede o envio padrão do formulário
        
            // Exibe uma caixa de diálogo personalizada
            Swal.fire({
                title: 'Tem certeza que deseja confirmar a edição desse funcionário?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Confirmar',
                cancelButtonText: 'Cancelar',
                iconColor: '#1a242f'
            }).then((result) => {
                // Se o usuário confirmar, envia o formulário via AJAX
                if (result.isConfirmed) {
                    enviarFormularioEdit(); // Função para enviar o formulário
                }
            });
        });
        }

        if(document.getElementById('cadastroFormEditAdmin')){
            document.getElementById('cadastroFormEditAdmin').addEventListener('submit', function(event) {
                event.preventDefault(); // Impede o envio padrão do formulário
            
                // Exibe uma caixa de diálogo personalizada
                Swal.fire({
                    title: 'Tem certeza que deseja confirmar a edição desse admin?',
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonText: 'Confirmar',
                    cancelButtonText: 'Cancelar',
                    iconColor: '#1a242f'
                }).then((result) => {
                    // Se o usuário confirmar, envia o formulário via AJAX
                    if (result.isConfirmed) {
                        enviarFormularioEdit(); // Função para enviar o formulário
                    }
                });
            });
            }

        if(document.getElementById('cadastroFormEditServico')){
            document.getElementById('cadastroFormEditServico').addEventListener('submit', function(event) {
                event.preventDefault(); // Impede o envio padrão do formulário
            
                // Exibe uma caixa de diálogo personalizada
                Swal.fire({
                    title: 'Tem certeza que deseja confirmar a edição desse serviço?',
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonText: 'Confirmar',
                    cancelButtonText: 'Cancelar',
                    iconColor: '#1a242f'
                }).then((result) => {
                    // Se o usuário confirmar, envia o formulário via AJAX
                    if (result.isConfirmed) {
                        enviarFormularioEditServico(); // Função para enviar o formulário
                    }
                });
            });
            }

            if(document.getElementById('cadastroFormEditHorario')){
                document.getElementById('cadastroFormEditHorario').addEventListener('submit', function(event) {
                    event.preventDefault(); // Impede o envio padrão do formulário
                
                    // Exibe uma caixa de diálogo personalizada
                    Swal.fire({
                        title: 'Tem certeza que deseja confirmar a edição desse horário?',
                        icon: 'question',
                        showCancelButton: true,
                        confirmButtonText: 'Confirmar',
                        cancelButtonText: 'Cancelar',
                        iconColor: '#1a242f'
                    }).then((result) => {
                        // Se o usuário confirmar, envia o formulário via AJAX
                        if (result.isConfirmed) {
                            enviarFormularioEditServico(); // Função para enviar o formulário
                        }
                    });
                });
                }
            
        
        function enviarFormularioEdit() {
            var form = document.getElementById('cadastroFormEdit');
            var formData = new FormData(form);
        
            // Envia o formulário via AJAX
            fetch(form.action, {
                method: form.method,
                body: formData
            })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url; // Redireciona se necessário
                }
            })
            .catch(error => console.error('Erro:', error));
        }

        function enviarFormularioEditAdmin() {
            var form = document.getElementById('cadastroFormEditAdmin');
            var formData = new FormData(form);
        
            // Envia o formulário via AJAX
            fetch(form.action, {
                method: form.method,
                body: formData
            })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url; // Redireciona se necessário
                }
            })
            .catch(error => console.error('Erro:', error));
        }

        function enviarFormularioEditServico() {
            var form = document.getElementById('cadastroFormEditServico');
            var formData = new FormData(form);
        
            // Envia o formulário via AJAX
            fetch(form.action, {
                method: form.method,
                body: formData
            })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url; // Redireciona se necessário
                }
            })
            .catch(error => console.error('Erro:', error));
        }

        function enviarFormularioEditHorario() {
            var form = document.getElementById('cadastroFormEditHorario');
            var formData = new FormData(form);
        
            // Envia o formulário via AJAX
            fetch(form.action, {
                method: form.method,
                body: formData
            })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url; // Redireciona se necessário
                }
            })
            .catch(error => console.error('Erro:', error));
        }
    
        // Novo Funcionário
        if(document.getElementById('cadastroFormNovoFuncionario')){
        document.getElementById('cadastroFormNovoFuncionario').addEventListener('submit', function(event) {
            event.preventDefault(); // Impede o envio padrão do formulário
        
            // Exibe uma caixa de diálogo personalizada
            Swal.fire({
                title: 'Tem certeza que deseja cadastrar novo funcionário?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Cadastrar',
                cancelButtonText: 'Cancelar',
                iconColor: '#1a242f'
            }).then((result) => {
                // Se o usuário confirmar, envia o formulário via AJAX
                if (result.isConfirmed) {
                    enviarFormularioNovoFuncionario(); // Função para enviar o formulário
                }
            });
        });
    }

     // Novo Funcionário
     if(document.getElementById('cadastroFormNovoServico')){
        document.getElementById('cadastroFormNovoServico').addEventListener('submit', function(event) {
            event.preventDefault(); // Impede o envio padrão do formulário
        
            // Exibe uma caixa de diálogo personalizada
            Swal.fire({
                title: 'Tem certeza que deseja cadastrar novo serviço?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Cadastrar',
                cancelButtonText: 'Cancelar',
                iconColor: '#1a242f'
            }).then((result) => {
                // Se o usuário confirmar, envia o formulário via AJAX
                if (result.isConfirmed) {
                    enviarFormularioNovoServico(); // Função para enviar o formulário
                }
            });
        });
    }

    // Novo Funcionário
    if(document.getElementById('cadastroFormNovoHorario')){
        document.getElementById('cadastroFormNovoHorario').addEventListener('submit', function(event) {
            event.preventDefault(); // Impede o envio padrão do formulário
        
            // Exibe uma caixa de diálogo personalizada
            Swal.fire({
                title: 'Tem certeza que deseja cadastrar novo horário?',
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Cadastrar',
                cancelButtonText: 'Cancelar',
                iconColor: '#1a242f'
            }).then((result) => {
                // Se o usuário confirmar, envia o formulário via AJAX
                if (result.isConfirmed) {
                    enviarFormularioNovoHorario(); // Função para enviar o formulário
                }
            });
        });
    }
        
        function enviarFormularioNovoFuncionario() {
            var form = document.getElementById('cadastroFormNovoFuncionario');
            var formData = new FormData(form);
        
            // Envia o formulário via AJAX
            fetch(form.action, {
                method: form.method,
                body: formData
            })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url; // Redireciona se necessário
                }
            })
            .catch(error => console.error('Erro:', error));
        
        }
    
        document.addEventListener('DOMContentLoaded', function() {
            var adminId = parseInt(document.getElementById('adminId').value);
            document.getElementById('adminId').value = adminId;
        });

        function enviarFormularioNovoServico() {
            var form = document.getElementById('cadastroFormNovoServico');
            var formData = new FormData(form);
        
            // Envia o formulário via AJAX
            fetch(form.action, {
                method: form.method,
                body: formData
            })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url; // Redireciona se necessário
                }
            })
            .catch(error => console.error('Erro:', error));
        
        }

        function enviarFormularioNovoHorario() {
            var form = document.getElementById('cadastroFormNovoHorario');
            var formData = new FormData(form);
        
            // Envia o formulário via AJAX
            fetch(form.action, {
                method: form.method,
                body: formData
            })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url; // Redireciona se necessário
                }
            })
            .catch(error => console.error('Erro:', error));
        
        }
    
        document.addEventListener('DOMContentLoaded', function() {
            var adminId = parseInt(document.getElementById('adminId').value);
            document.getElementById('adminId').value = adminId;
        });
    
    
    // Exibe mensagem de sucesso para login
    $(document).ready(function() {
        var params = new URLSearchParams(window.location.search);
        if(params.has('loginSucesso')){
        var loginSucesso = params.get('loginSucesso');
        
        // Verifica se a mensagem já foi exibida anteriormente
        if (loginSucesso === 'true' && !sessionStorage.getItem('loginSucessoShown')) {
            // Exibe a mensagem de sucesso
            Swal.fire({
                title: 'Login realizado com sucesso!',
                icon: 'success',
                showConfirmButton: false,
                timer: 2000 // Tempo em milissegundos (2 segundos)
            });
            // Remove o indicador de que a mensagem foi exibida
            sessionStorage.removeItem('saidaSucessoShown');
             // Remove o indicador de que a mensagem foi exibida
             sessionStorage.removeItem('cadastroSucessoShown');
            // Define o indicador de que a mensagem foi exibida
            sessionStorage.setItem('loginSucessoShown', 'true');
        }
    } else if (params.has('cadastroSucesso')){
        var cadastroSucesso = params.get('cadastroSucesso');
            
        // Verifica se a mensagem já foi exibida anteriormente
        if (cadastroSucesso === 'true' && !sessionStorage.getItem('cadastroSucessoShown')) {
            // Exibe a mensagem de sucesso para cadastro
            Swal.fire({
                title: 'Cadastro realizado com sucesso!',
                icon: 'success',
                showConfirmButton: false,
                timer: 2000 // Tempo em milissegundos (2 segundos)
            });
    
            // Define o indicador de que a mensagem foi exibida
            sessionStorage.setItem('cadastroSucessoShown', 'true');
            // Remove o indicador de que a mensagem foi exibida
            sessionStorage.removeItem('saidaSucessoShown');
             // Remove o indicador de que a mensagem foi exibida
             sessionStorage.removeItem('loginSucessoShown');
        }
    } else if(params.has('saidaSucesso')){
        var saidaSucesso = params.get('saidaSucesso');
            
            // Verifica se a mensagem já foi exibida anteriormente
            if (saidaSucesso === 'true' && !sessionStorage.getItem('saidaSucessoShown')) {
                Swal.fire({
                    title: 'Você saiu da conta',
                    icon: 'success',
                    showConfirmButton: false,
                    timer: 2000 // Tempo em milissegundos (2 segundos)
                });
                // Define o indicador de que a mensagem foi exibida
                sessionStorage.setItem('saidaSucessoShown', 'true');
                // Remove o indicador de que a mensagem foi exibida
                sessionStorage.removeItem('loginSucessoShown');
    
                // Remove o indicador de que a mensagem foi exibida
                sessionStorage.removeItem('cadastroSucessoShown');
            }
    
    }
    });
    
    
    // Função para exibir o Swal.fire ao clicar no ícone da lixeira
    function ConfirmarExclusao() {
        event.preventDefault(); // Impede o comportamento padrão do link
        const url = event.currentTarget.getAttribute('href'); // Obtém o URL de exclusão do atributo href
        // Exibe uma caixa de diálogo personalizada
        Swal.fire({
            title: 'Tem certeza que deseja excluir este funcionário?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Excluir',
            cancelButtonText: 'Cancelar',
            iconColor: '#1a242f'
        }).then((result) => {
            // Se o usuário confirmar, envia o formulário via AJAX
            if (result.isConfirmed) {
                window.location.href =  url// Redireciona para o URL de exclusão
            }
        });
    }

    // Função para exibir o Swal.fire ao clicar no ícone da lixeira
    function ConfirmarExclusaoAdmin() {
        event.preventDefault(); // Impede o comportamento padrão do link
        const url = event.currentTarget.getAttribute('href'); // Obtém o URL de exclusão do atributo href
        // Exibe uma caixa de diálogo personalizada
        Swal.fire({
            title: 'Tem certeza que deseja excluir este admin?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Excluir',
            cancelButtonText: 'Cancelar',
            iconColor: '#1a242f'
        }).then((result) => {
            // Se o usuário confirmar, envia o formulário via AJAX
            if (result.isConfirmed) {
                window.location.href =  url// Redireciona para o URL de exclusão
            }
        });
    }

     // Função para exibir o Swal.fire ao clicar no ícone da lixeira
     function ConfirmarExclusaoServico() {
        event.preventDefault(); // Impede o comportamento padrão do link
        const url = event.currentTarget.getAttribute('href'); // Obtém o URL de exclusão do atributo href
        // Exibe uma caixa de diálogo personalizada
        Swal.fire({
            title: 'Tem certeza que deseja excluir este serviço?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Excluir',
            cancelButtonText: 'Cancelar',
            iconColor: '#1a242f'
        }).then((result) => {
            // Se o usuário confirmar, envia o formulário via AJAX
            if (result.isConfirmed) {
                window.location.href =  url// Redireciona para o URL de exclusão
            }
        });
    }

    // Função para exibir o Swal.fire ao clicar no ícone da lixeira
    function ConfirmarExclusaoHorario() {
        event.preventDefault(); // Impede o comportamento padrão do link
        const url = event.currentTarget.getAttribute('href'); // Obtém o URL de exclusão do atributo href
        // Exibe uma caixa de diálogo personalizada
        Swal.fire({
            title: 'Tem certeza que deseja excluir este horário?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Excluir',
            cancelButtonText: 'Cancelar',
            iconColor: '#1a242f'
        }).then((result) => {
            // Se o usuário confirmar, envia o formulário via AJAX
            if (result.isConfirmed) {
                window.location.href =  url// Redireciona para o URL de exclusão
            }
        });
    }

     // Função para exibir o Swal.fire ao clicar no ícone da lixeira
     function desmarcarHorario() {
        event.preventDefault(); // Impede o comportamento padrão do link
        const url = event.currentTarget.getAttribute('href'); // Obtém o URL de exclusão do atributo href
        // Exibe uma caixa de diálogo personalizada
        Swal.fire({
            title: 'Tem certeza que deseja desmarcar este horário?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Desmarcar',
            cancelButtonText: 'Cancelar',
            iconColor: '#1a242f'
        }).then((result) => {
            // Se o usuário confirmar, envia o formulário via AJAX
            if (result.isConfirmed) {
                window.location.href =  url// Redireciona para o URL de exclusão
            }
        });
    }

    function marcarHorarioConcluido(event) {
        event.preventDefault(); // Impede o comportamento padrão do formulário
        const form = event.currentTarget.closest('form'); // Obtém o formulário mais próximo
        const url = form.getAttribute('action'); // Obtém o URL do formulário
        // Exibe uma caixa de diálogo personalizada
        Swal.fire({
            title: 'Tem certeza que deseja marcar esse horário como concluído?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Concluir',
            cancelButtonText: 'Cancelar',
            iconColor: '#1a242f'
        }).then((result) => {
            // Se o usuário confirmar, envia o formulário
            if (result.isConfirmed) {
                // Envie o formulário via AJAX
                $.ajax({
                    type: 'POST',
                    url: url,
                    data: $(form).serialize(),
                    success: function(response) {
                        // Se o envio do formulário for bem-sucedido, exiba uma mensagem de sucesso
                        Swal.fire({
                            title: 'Horário marcado como concluído!',
                            icon: 'success',
                            showCancelButton: false,
                            confirmButtonText: 'OK',
                            iconColor: '#28a745'
                        }).then(() => {
                            // Redirecionar ou fazer outra ação após o usuário clicar em OK
                            // Por exemplo, você pode redirecionar para outra página
                            window.location.href = '/funcionarios';
                        });
                    },
                    error: function(xhr, status, error) {
                        // Se ocorrer um erro ao enviar o formulário, exiba uma mensagem de erro
                        Swal.fire({
                            title: 'Erro ao marcar horário!',
                            text: 'Por favor, tente novamente mais tarde.',
                            icon: 'error',
                            showCancelButton: false,
                            confirmButtonText: 'OK',
                            iconColor: '#dc3545'
                        });
                    }
                });
            }
        });
    }
    
    
    
    
    