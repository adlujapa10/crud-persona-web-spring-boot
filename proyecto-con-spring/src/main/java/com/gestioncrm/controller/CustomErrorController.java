package com.gestioncrm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Controlador para manejar errores de la aplicación
 */
@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger log = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        
        log.error("Error en la aplicación - Status: {}, Message: {}, Exception: {}", 
                 status, message, exception);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            switch (statusCode) {
                case 404:
                    model.addAttribute("status", "404");
                    model.addAttribute("error", "Página No Encontrada");
                    model.addAttribute("message", "Lo sentimos, la página que buscas no existe o ha sido movida.");
                    break;
                case 403:
                    model.addAttribute("status", "403");
                    model.addAttribute("error", "Acceso Denegado");
                    model.addAttribute("message", "No tienes permisos para acceder a este recurso.");
                    break;
                case 500:
                    model.addAttribute("status", "500");
                    model.addAttribute("error", "Error del Servidor");
                    model.addAttribute("message", "Ha ocurrido un error interno en el servidor. Por favor, inténtalo de nuevo más tarde.");
                    break;
                default:
                    model.addAttribute("status", statusCode);
                    model.addAttribute("error", "Error Inesperado");
                    model.addAttribute("message", "Ha ocurrido un error inesperado. Por favor, inténtalo de nuevo más tarde.");
                    break;
            }
        } else {
            model.addAttribute("status", "Error");
            model.addAttribute("error", "Error Desconocido");
            model.addAttribute("message", "Ha ocurrido un error desconocido. Por favor, inténtalo de nuevo más tarde.");
        }
        
        return "error";
    }
}
