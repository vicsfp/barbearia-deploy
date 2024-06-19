package com.web.BarbeariaGS.Integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.web.BarbeariaGS.controllers.FuncionarioController;
import com.web.BarbeariaGS.repository.AgendamentoRepo;
import com.web.BarbeariaGS.repository.AdminRepo;
import com.web.BarbeariaGS.repository.ClientesRepo;
import com.web.BarbeariaGS.repository.FuncionariosRepo;
import com.web.BarbeariaGS.repository.HorarioRepo;
import com.web.BarbeariaGS.repository.ServicoRepo;

@ActiveProfiles("test")
@WebMvcTest(FuncionarioController.class)
@AutoConfigureTestDatabase
public class HorarioControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientesRepo clientesRepo;

    @MockBean
    private HorarioRepo horarioRepo;

    @MockBean
    private ServicoRepo servicoRepo;

    @MockBean
    private AgendamentoRepo AgendamentoRepo;

    @MockBean
    private FuncionariosRepo funcionariosRepo;

    @MockBean
    private AdminRepo adminRepo;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
       

    }

    @Test
    public void testAgendamentosHorarios_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
         // Clear any existing static mocks
         Mockito.framework().clearInlineMocks();
                mockMvc.perform(get("/gerenciar/horarios"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error/404"));
    }

    @Test
    public void testGerenciarHorarios_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        // Clear any existing static mocks
        Mockito.framework().clearInlineMocks();
        mockMvc.perform(get("/gerenciar/horarios/novo"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/error/404"));
    }

    @Test
    public void testGerenciarNovoHorario_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        // Clear any existing static mocks
        Mockito.framework().clearInlineMocks();
        mockMvc.perform(get("/gerenciar/horarios/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/error/404"));
    }

    @Test
    public void testGerenciarEditHorario_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        // Clear any existing static mocks
        Mockito.framework().clearInlineMocks();
        mockMvc.perform(get("/gerenciar/horarios/1/excluir"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/error/404"));
    }
}
