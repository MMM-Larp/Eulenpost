package net.gothax.larp.larpweb.web.validator;

import net.gothax.larp.larpweb.model.Entry;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Entry.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        rejectIfNoEmail(errors, "email");
    }

    private void rejectIfNoEmail(Errors errors, String email) {
        String value = (String) errors.getFieldValue(email);
        if(!isEmail(value) && !Objects.requireNonNull(value).isEmpty())
            errors.rejectValue(email, "required", "Ungültige E-Mail Adresse!");
    }

    private boolean isEmail(String email) {
        String regex = "^[^@\\s]+@([-a-z0-9]+\\.)+[a-z]{2,}$";
        return isStringRegex(email, regex);
    }

    private boolean isStringRegex(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}
