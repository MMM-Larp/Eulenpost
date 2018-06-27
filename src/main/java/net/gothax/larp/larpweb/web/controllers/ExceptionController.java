package net.gothax.larp.larpweb.web.controllers;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionController {
    private static Logger log = Logger.getLogger(ExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(Exception e) {
        return "404";
    }

    @ExceptionHandler(value = Exception.class)
    public String handleError(Exception e, Model model) {
        String message = e.getMessage().replaceAll("java.", "").replaceAll("lang.", "");
        log.info( e.getMessage());
        model.addAttribute("error", message);
        return "error";
    }
}
