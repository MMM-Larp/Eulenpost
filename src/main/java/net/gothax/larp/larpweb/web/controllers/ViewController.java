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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ViewController {
    private final FormValidator formValidator;

    private final EntryService entryService;

    private final MappingService mappingService;

    private final ContentService contentService;

    @Autowired
    public ViewController(FormValidator formValidator, EntryService entryService, MappingService mappingService, ContentService contentService) {
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
        model.addAttribute("content", contentService.getContent());

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

        List<Entry> entries = entryService.getAllEntries();
        model.addAttribute("entries", entries);

        return "admin";
    }

    @PostMapping(value = "/register")
    public String onFormSubmit(Model model, @Validated @ModelAttribute("entry") Entry entry, BindingResult result) {
        if(result.hasErrors())
            return "index";

        entryService.saveEntry(entry);
        return "thank-you";
    }

    @RequestMapping(value = "/admin/clear")
    public String onClear() {
        entryService.clearEntries();

        return "admin";
    }

    @RequestMapping(value = "/admin/roll")
    public String onRoll() {
        mappingService.clearMappings();

        while(true) {
            List<Entry> senders = entryService.getAllEntries();
            List<Entry> receiverOne = entryService.getShuffledEntries();
            List<Entry> receiverTwo = entryService.getShuffledEntries();

            if(mappingService.isShuffleCorrect(senders, receiverOne, receiverTwo)) {
                mappingService.saveMapping(senders, receiverOne, receiverTwo);
                break;
            }
        }

        return "redirect:/admin/mapping";
    }

    @RequestMapping(value = "/admin/mapping")
    public String onMapping(Model model) {
        List<Mapping> mappings = mappingService.getAllMappings();
        model.addAttribute("mappings", mappings);

        return "mapping";
    }


    @PostMapping(value = "/admin/saveDescription")
    public String onSaveDescription(@ModelAttribute Content content) {
        contentService.saveContent(content);
        return "redirect:/";
    }


    /**
     * This is getting deleted after testing
     * TODO: DON'T FORGET TO DELETE THIS!
     * @return
     */
    @RequestMapping(value = "/admin/testdata")
    public String onTestData() {
        List<String> firstNames = Arrays.asList("Hannah", "Eddie", "Remus", "Lily", "James", "Amycus", "Devon", "Sirius", "Reginald", "Eve");
        List<String> lastNames = Arrays.asList("Ward", "Deimos", "Lupin", "Evans", "Potter", "Carrow", "Avery", "Black", "Cattermole", "Edgecomb");

        Random r = new Random();
        for(int i = 0; i < 6; i++) {
            String firstName = firstNames.get(r.nextInt(firstNames.size()));
            String lastName = lastNames.get(r.nextInt(lastNames.size()));

            House house = House.values()[r.nextInt(House.values().length)];
            String note = UUID.randomUUID().toString();

            String email = "mkling@gothax.net";

            Entry e = new Entry();
            e.setFirstName(firstName);
            e.setLastName(lastName);
            e.setHouse(house);
            e.setNote(note);
            e.setEmail(email);

            entryService.saveEntry(e);
        }

        return "redirect:/admin";
    }
}
