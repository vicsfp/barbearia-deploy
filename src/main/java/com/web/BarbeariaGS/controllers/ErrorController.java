package com.web.BarbeariaGS.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/error/404")
    public String index() {
        return "error/404"; // Retorna o nome do arquivo HTML sem a extensão
    }
}
