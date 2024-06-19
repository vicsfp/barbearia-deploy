package com.web.BarbeariaGS.Integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

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

import com.web.BarbeariaGS.controllers.ClienteController;
import com.web.BarbeariaGS.repository.AgendamentoRepo;
import com.web.BarbeariaGS.repository.AdminRepo;
import com.web.BarbeariaGS.repository.ClientesRepo;
import com.web.BarbeariaGS.repository.FuncionariosRepo;
import com.web.BarbeariaGS.repository.HorarioRepo;
import com.web.BarbeariaGS.repository.ServicoRepo;
import com.web.BarbeariaGS.services.CookieService;

@ActiveProfiles("test")
@WebMvcTest(ClienteController.class)
@AutoConfigureTestDatabase
public class ClienteControllerIntegrationTest {

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
    public void testAgendamentosCliente_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        // Clear any existing static mocks
        Mockito.framework().clearInlineMocks();
        mockMvc.perform(get("/clientes"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }


    @Test
    public void testNovo_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        // Clear any existing static mocks
        Mockito.framework().clearInlineMocks();
        mockMvc.perform(get("/clientes/novo"))
            .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void testNovo_WhenLoggedIn_ShouldShowNovoPage() throws Exception {
        Cookie usuarioIdCookie = new Cookie("usuarioId", "1");

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{usuarioIdCookie});

        Mockito.mockStatic(CookieService.class);
        when(CookieService.getCookie(any(HttpServletRequest.class), eq("usuarioId"))).thenReturn(usuarioIdCookie.getValue());

        mockMvc.perform(get("/clientes/novo").cookie(usuarioIdCookie))
            .andExpect(status().isOk())
            .andExpect(view().name("clientes/novo"));
    }

}
