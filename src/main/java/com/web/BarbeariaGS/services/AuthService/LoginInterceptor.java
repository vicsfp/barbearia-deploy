package com.web.BarbeariaGS.services.AuthService;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.web.BarbeariaGS.services.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class LoginInterceptor implements HandlerInterceptor {

   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      
         if(CookieService.getCookie(request, "usuarioId") != null){
            return true;
         }else if(CookieService.getCookie(request, "tipoUsuario") != null){
            return true;
         }else{
         // Redireciona para a página de erro 404
      response.sendRedirect("/error/404");
      return false; // Retorna false para indicar que a requisição foi tratada e não deve continuar o processamento
   }
   }
      


   // @Override
   // public void postHandle(HttpServletRequest request, HttpServletResponse response, 
   //    Object handler, ModelAndView modelAndView) throws Exception {
      
   //    System.out.println("Post Handle method is Calling");
   // }
   // @Override
   // public void afterCompletion
   //    (HttpServletRequest request, HttpServletResponse response, Object 
   //    handler, Exception exception) throws Exception {
      
   //    System.out.println("Request and Response is completed");
   // }
}
