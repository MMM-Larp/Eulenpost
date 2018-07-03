package net.gothax.larp.larpweb.web.validator;

import net.gothax.larp.larpweb.model.Entry;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Objects;


@Component
public class FormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Entry.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"firstName", "required", "Bitte gib deinen Vornamen (IT) an.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"lastName", "required", "Bitte gib deinen Nachnamen (IT) an.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"otName", "required", "Bitte gib deinen OT-Namen an.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"house", "required", "Bitte gib dein Haus (IT) an.");

        rejectIfNoPrivacy(errors);
        rejectIfNoRules(errors);
        rejectIfNoEmail(errors);
    }

    private void rejectIfNoPrivacy(Errors errors) {
        boolean privacy = Boolean.valueOf(Objects.requireNonNull(errors.getFieldValue("privacy")).toString());
        if (!privacy)
            errors.rejectValue("privacy", "required", "Datenschutzerklärung muss zugestimmt werden");
    }

    private void rejectIfNoRules(Errors errors) {
        boolean rules = Boolean.valueOf(Objects.requireNonNull(errors.getFieldValue("rules")).toString());
        if (!rules)
            errors.rejectValue("rules", "required", "Bitte stimme den Regeln zu.");
    }

    private void rejectIfNoEmail(Errors errors) {
        String value = (String) errors.getFieldValue("email");
        if (value != null) {
            if (!isEmail(value) || value.isEmpty())
                errors.rejectValue("email", "required", "Bitte gib eine gültige Email-Adresse an!");
        }
    }

    private boolean isEmail(String email) {
        return email.matches("^[^@\\s]+@([-a-z0-9]+\\.)+[a-z]{2,}$");
    }

}
