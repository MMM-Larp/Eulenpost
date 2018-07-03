package net.gothax.larp.larpweb.web.controllers;

import net.gothax.larp.larpweb.model.Content;
import net.gothax.larp.larpweb.model.Entry;
import net.gothax.larp.larpweb.model.House;
import net.gothax.larp.larpweb.model.Mapping;
import net.gothax.larp.larpweb.service.ContentService;
import net.gothax.larp.larpweb.service.EntryService;
import net.gothax.larp.larpweb.service.MappingService;
import net.gothax.larp.larpweb.web.validator.FormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Controller
public class ViewController {
    private final TemplateEngine templateEngine;

    private final JavaMailSender mailSender;

    private final FormValidator formValidator;

    private final EntryService entryService;

    private final MappingService mappingService;

    private final ContentService contentService;

    @Autowired
    public ViewController(TemplateEngine templateEngine, JavaMailSender mailSender,
                          FormValidator formValidator, EntryService entryService,
                          MappingService mappingService, ContentService contentService) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
        this.formValidator = formValidator;
        this.entryService = entryService;
        this.mappingService = mappingService;
        this.contentService = contentService;
    }

    @InitBinder("entry")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(formValidator);
    }

    @RequestMapping("/")
    public String onIndex(Model model) {
        model.addAttribute("entry", new Entry());

        Content content = contentService.getContent();
        if(content == null) {
            content = new Content();
            content.setAllowSignUp(true);
            content.setDescription("Default-Message");
            contentService.saveContent(content);
        }

        model.addAttribute("content", content);

        return "index";
    }

    @RequestMapping("/login")
    public String onLogin() {
        return "login";
    }

    @RequestMapping("/privacy")
    public String onPrivacy() {
        return "privacy";
    }

    @RequestMapping("/accessDenied")
    public String onAccessDenied() {
        return "accessDenied";
    }

    @RequestMapping("/admin")
    public String onAdmin(Model model) {

        List<Entry> entries = entryService.getAllEntries();
        model.addAttribute("entries", entries);
        model.addAttribute("content", contentService.getContent());

        return "admin";
    }

    @RequestMapping(value = "/admin/edit/{id}")
    public String onEditEntry(Model model, @PathVariable long id) {
        Entry e = entryService.getEntryById(id);
        model.addAttribute("entry", e);

        return "edit-entry";
    }

    @RequestMapping(value = "/admin/delete/{id}")
    public String onDeleteEntry(@PathVariable long id) {
        Entry e = entryService.getEntryById(id);
        entryService.delete(e);

        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/saveEdit")
    public String onSaveEdit(@Validated @ModelAttribute("entry") Entry entry) {
        entryService.saveEntry(entry);
        return "redirect:/admin";
    }

    @PostMapping(value = "/register")
    public String onFormSubmit(Model model, @Validated @ModelAttribute("entry") Entry entry, BindingResult result) {
        if(result.hasErrors()) {
            model.addAttribute("content", contentService.getContent());
            return "index";
        }

        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setFrom("mailer@gothax.net");
            message.setTo(entry.getEmail());
            message.setSubject("M.M.M. LARP - Eulenpost-O-Mat Anmeldung");

            final Context templateData = new Context();
            templateData.setVariable("entry", entry);

            String htmlContent = templateEngine.process("email/opt-in-mail", templateData);
            message.setText(htmlContent, true);
        };

        //mailSender.send(preparator);
        entryService.saveEntry(entry);

        return "thank-you";
    }

    @RequestMapping(value = "/admin/send")
    public String onSendMail() {
        List<Mapping> mappings = mappingService.getAllMappings();

        for(Mapping m : mappings) {
            MimeMessagePreparator preparator = mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
                message.setFrom("mailer@gothax.net");
                message.setTo(m.getSender().getEmail());
                message.setSubject("M.M.M. LARP - Deine Empfänger");

                final Context templateData = new Context();
                templateData.setVariable("sender", m.getSender());
                templateData.setVariable("receivers", m.getReceivers());

                String htmlContent = templateEngine.process("email/roll-mail", templateData);
                message.setText(htmlContent, true);
            };

            mailSender.send(preparator);
        }

        return "redirect:/mapping";
    }

    @RequestMapping(value = "/admin/clear")
    public String onClear() {
        entryService.clearEntries();

        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/close")
    public String onClose() {
        Content c = contentService.getContent();
        c.setAllowSignUp(false);
        contentService.saveContent(c);

        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/open")
    public String onOpen() {
        Content c = contentService.getContent();
        c.setAllowSignUp(true);
        contentService.saveContent(c);

        return "redirect:/admin";
    }


    @RequestMapping(value = "/admin/clearMapping")
    public String onClearMapping() {
        mappingService.clearMappings();

        return "redirect:/mapping";
    }

    @RequestMapping(value = "/admin/roll")
    public String onRoll() {
        mappingService.clearMappings();

        List<Entry> entries = entryService.getAllEntries();
        mappingService.saveMapping(entries);

        return "redirect:/mapping";
    }


    @RequestMapping(value = "/mapping")
    public String onMapping(Model model) {
        List<Mapping> mappings = mappingService.getAllMappings();
        model.addAttribute("mappings", mappings);
        model.addAttribute("content", contentService.getContent());

        return "mapping";
    }


    @PostMapping(value = "/admin/saveDescription")
    public String onSaveDescription(@ModelAttribute Content content) {
        Content c = contentService.getContent();
        c.setDescription(content.getDescription());
        contentService.saveContent(c);

        return "redirect:/";
    }


    @RequestMapping(value = "/admin/testdata")
    public String onTestData() {
        List<String> firstNames = Arrays.asList("Hannah", "Eddie", "Remus", "Lily", "James", "Amycus", "Devon", "Sirius", "Reginald", "Eve");
        List<String> lastNames = Arrays.asList("Ward", "Deimos", "Lupin", "Evans", "Potter", "Carrow", "Avery", "Black", "Cattermole", "Edgecomb");
        List<String> otNames = Arrays.asList("Maxi", "Jen", "Joshi", "Hannah", "Liz", "Kashi", "Max", "Ella", "Saki");

        Random r = new Random();
        for(int i = 0; i < 6; i++) {
            String firstName = firstNames.get(r.nextInt(firstNames.size()));
            String nickName = firstNames.get(r.nextInt(firstNames.size()));
            String lastName = lastNames.get(r.nextInt(lastNames.size()));
            String otName = lastNames.get(r.nextInt(otNames.size()));

            House house = House.values()[r.nextInt(House.values().length)];
            String note = UUID.randomUUID().toString();

            String email = "mkling@gothax.net";

            Entry e = new Entry();
            e.setFirstName(firstName);
            e.setOtName(otName);
            e.setNickName(nickName);
            e.setLastName(lastName);
            e.setHouse(house);
            e.setNote(note);
            e.setEmail(email);
            e.setPrivacy(true);
            e.setRules(true);

            entryService.saveEntry(e);
        }

        return "redirect:/admin";
    }
}
