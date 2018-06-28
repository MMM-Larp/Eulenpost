package net.gothax.larp.larpweb.web.controllers;

import net.gothax.larp.larpweb.model.Entry;
import net.gothax.larp.larpweb.model.House;
import net.gothax.larp.larpweb.model.Mapping;
import net.gothax.larp.larpweb.persistence.EntryRepository;
import net.gothax.larp.larpweb.persistence.MappingRepository;
import net.gothax.larp.larpweb.web.validator.FormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Controller
public class ViewController {
    private final FormValidator formValidator;

    @Resource
    private EntryRepository entryRepository;

    @Resource
    private MappingRepository mappingRepository;

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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //if(!(auth instanceof AnonymousAuthenticationToken))
        //    return "redirect:/admin";
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

    @RequestMapping(value = "/admin/clear")
    public String onClear() {
        entryRepository.deleteAll();

        return "admin";
    }

    @RequestMapping(value = "/admin/roll")
    public String onRoll() {
        mappingRepository.deleteAll();

        boolean correct = false;
        while(!correct) {
            List<Entry> senders = entryRepository.findAll();

            List<Entry> receiverOne = new ArrayList<>(senders);
            Collections.shuffle(receiverOne);

            List<Entry> receiverTwo = new ArrayList<>(senders);
            Collections.shuffle(receiverTwo);

            if(shuffleCorrect(senders, receiverOne, receiverTwo)) {
                correct = true;
                saveMapping(senders, receiverOne, receiverTwo);
            }
        }

        return "redirect:/admin/mapping";
    }

    @RequestMapping(value = "/admin/mapping")
    public String onMapping(Model model) {
        List<Mapping> mappings = mappingRepository.findAll();

        model.addAttribute("mappings", mappings);

        return "mapping";
    }

    private void saveMapping(List<Entry> senders, List<Entry> receiverOne, List<Entry> receiverTwo) {
        for(int i = 0; i < senders.size(); i++) {
            Mapping m = new Mapping();
            m.setSender(senders.get(i));
            m.setReceiverOne(receiverOne.get(i));
            m.setReceiverTwo(receiverTwo.get(i));

            mappingRepository.save(m);
        }
    }


    private boolean shuffleCorrect(List<Entry> senders, List<Entry> receiverOne, List<Entry> receiverTwo) {
        for(int i = 0; i < senders.size(); i++) {
            if(senders.get(i) == receiverOne.get(i) ||
               senders.get(i) == receiverTwo.get(i) ||
               receiverOne.get(i) == receiverTwo.get(i))
                return false;
        }
        return true;
    }

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

            String email = "test@gothax.net";

            Entry e = new Entry();
            e.setFirstName(firstName);
            e.setLastName(lastName);
            e.setHouse(house);
            e.setNote(note);
            e.setEmail(email);

            entryRepository.save(e);
        }

        return "redirect:/admin";
    }
}
