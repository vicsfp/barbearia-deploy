package com.web.BarbeariaGS.Integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.web.BarbeariaGS.controllers.AdminController;
import com.web.BarbeariaGS.repository.AdminRepo;
import com.web.BarbeariaGS.repository.ClientesRepo;
import com.web.BarbeariaGS.repository.FuncionariosRepo;
import com.web.BarbeariaGS.services.CookieService;

@ActiveProfiles("test")
@WebMvcTest(AdminController.class)
public class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminRepo adminRepo;

    @MockBean
    private ClientesRepo clienteRepo;

    @MockBean
    private FuncionariosRepo funcionariosRepo;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testIndex_WhenNotLoggedIn_ShouldRedirectToError404() throws Exception {
        // Clear any existing static mocks
        Mockito.framework().clearInlineMocks();
        mockMvc.perform(get("/administradores"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/error/404"));
    }


    @Test
    public void testNovo_WhenNotLoggedIn_ShouldRedirectToError404() throws Exception {
        // Clear any existing static mocks
        Mockito.framework().clearInlineMocks();
        mockMvc.perform(get("/administradores/novo"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/error/404"));
    }

    @Test
    public void testNovo_WhenLoggedInAsAdmin_ShouldShowNewAdminPage() throws Exception {
        // Clear any existing static mocks
        Mockito.framework().clearInlineMocks();
        Cookie usuarioIdCookie = new Cookie("usuarioId", "1");
        Cookie tipoUsuarioCookie = new Cookie("tipoUsuario", "adminCookie");

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{usuarioIdCookie, tipoUsuarioCookie});

        Mockito.mockStatic(CookieService.class);
        when(CookieService.getCookie(any(HttpServletRequest.class), eq("usuarioId"))).thenReturn(usuarioIdCookie.getValue());
        when(CookieService.getCookie(any(HttpServletRequest.class), eq("tipoUsuario"))).thenReturn(tipoUsuarioCookie.getValue());

        mockMvc.perform(get("/gerenciar/administradores/novo"))
            .andExpect(status().isOk())
            .andExpect(view().name("administradores/novo"));
    }

    @Test
    public void testCriarAdmin_WhenLoggedInAsAdmin_ShouldCreateAdmin() throws Exception {
        Cookie usuarioIdCookie = new Cookie("usuarioId", "1");
        Cookie tipoUsuarioCookie = new Cookie("tipoUsuario", "adminCookie");

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{usuarioIdCookie, tipoUsuarioCookie});

        Mockito.mockStatic(CookieService.class);
        when(CookieService.getCookie(any(HttpServletRequest.class), eq("usuarioId"))).thenReturn(usuarioIdCookie.getValue());
        when(CookieService.getCookie(any(HttpServletRequest.class), eq("tipoUsuario"))).thenReturn(tipoUsuarioCookie.getValue());

        mockMvc.perform(post("/gerenciar/administradores/criar")
            .param("email", "admin@example.com")
            .param("senha", "Senha123!")
            .param("nome", "Admin Name"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/gerenciar/administradores?cadastroSucesso=true"));
    }
}
