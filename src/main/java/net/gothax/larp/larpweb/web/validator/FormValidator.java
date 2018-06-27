package net.gothax.larp.larpweb.web.validator;

import net.gothax.larp.larpweb.model.Entry;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class FormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Entry.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        errors.rejectValue("firstName", "required", "Bitte geben Sie den Vornamen an.");
        errors.rejectValue("lastName", "required", "Bitte geben Sie den Nachnamen an.");
        rejectIfNoEmail(errors);
    }

    private void rejectIfNoEmail(Errors errors) {
        String value = (String) errors.getFieldValue("email");
        if (value != null) {
            if (!isEmail(value) && !value.isEmpty())
                errors.rejectValue("email", "required", "Ungültige E-Mail Adresse!");
        }
    }

    private boolean isEmail(String email) {
        return email.matches("^[^@\\s]+@([-a-z0-9]+\\.)+[a-z]{2,}$");
    }

}
