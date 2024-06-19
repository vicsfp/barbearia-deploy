package com.web.BarbeariaGS.Integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



@ActiveProfiles("test")
@SpringBootTest
public class AgendamentoControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        
    }

    @Test
    public void testIndex_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        // Clear any existing static mocks
        Mockito.framework().clearInlineMocks();
        mockMvc.perform(get("/clientes/novo/agendamento"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/error/404"));
    }


@Test
public void testSelecaoHorarioAgendamento_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
     // Clear any existing static mocks
     Mockito.framework().clearInlineMocks();
            mockMvc.perform(get("/clientes/novo/agendamento/selecao-horarios"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/error/404"));
}

@Test
public void testFuncionarioDesmarcaAgendamento_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
    // Clear any existing static mocks
    Mockito.framework().clearInlineMocks();
    mockMvc.perform(get("/funcionarios/1/desmarcar"))
    .andExpect(status().is3xxRedirection())
    .andExpect(redirectedUrl("/error/404"));
}

@Test
public void testClienteDesmarcarAgendamento_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
    // Clear any existing static mocks
    Mockito.framework().clearInlineMocks();
    mockMvc.perform(get("/clientes/1/desmarcar"))
    .andExpect(status().is3xxRedirection())
    .andExpect(redirectedUrl("/error/404"));
}

@Test
public void testConcluirAgendamento_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
    // Clear any existing static mocks
    Mockito.framework().clearInlineMocks();
    mockMvc.perform(post("/funcionarios/1/marcar-concluido"))
    .andExpect(status().is3xxRedirection())
    .andExpect(redirectedUrl("/error/404"));
}

    @Test
    public void testCriarAgendamento_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        // Clear any existing static mocks
        Mockito.framework().clearInlineMocks();
        mockMvc.perform(post("/clientes/novo/agendamento/selecao-horarios/criar"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/error/404"));
    }

}
