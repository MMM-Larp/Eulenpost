package net.gothax.larp.larpweb.web.controllers;

import net.gothax.larp.larpweb.model.Entry;
import net.gothax.larp.larpweb.persistence.EntryRepository;
import net.gothax.larp.larpweb.web.validator.FormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ViewController {
    private final FormValidator formValidator;

    @Resource
    private EntryRepository entryRepository;

    @Autowired
    public ViewController(FormValidator formValidator) {
        this.formValidator = formValidator;
    }

    @InitBinder("entry")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(formValidator);
    }

    @RequestMapping("/")
    public String onIndex(Model model) {
        model.addAttribute("entry", new Entry());
        return "index";
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
    public String onAdmin(Model model) {

        List<Entry> entries = entryRepository.findAll();
        model.addAttribute("entries", entries);

        return "admin";
    }

    @PostMapping(value = "/register")
    public String onFormSubmit(@Validated @ModelAttribute("entry") Entry entry, BindingResult result) {
        if(result.hasErrors())
            return "error";

        entryRepository.save(entry);

        return "thank-you";
    }
}
