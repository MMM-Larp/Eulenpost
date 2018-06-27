package net.gothax.larp.larpweb.web.controllers;

import net.gothax.larp.larpweb.model.Entry;
import net.gothax.larp.larpweb.web.validator.FormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class ViewController {
    private final FormValidator formValidator;

    @Autowired
    public ViewController(FormValidator formValidator) {
        this.formValidator = formValidator;
    }

    @InitBinder("entry")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(formValidator);
    }

    @RequestMapping("/login")
    public String onLogin() {
        return "login";
    }

    @RequestMapping("/accessDenied")
    public String onAccessDenied() {
        return "accessDenied";
    }

    @RequestMapping("/admin")
    public String onAdmin() {
        return "admin";
    }

    @PostMapping(value = "/admin/formSubmit")
    public String onFormSubmit(@Valid @ModelAttribute("entry") Entry entry, BindingResult result) {
        if(result.hasErrors())
            return "error";

        return "thank-you";
    }
}
