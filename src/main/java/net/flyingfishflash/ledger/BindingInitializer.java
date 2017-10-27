package net.flyingfishflash.ledger;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;


/*
 *  Ensures form fields are converted to null
 *  
 *   https://stackoverflow.com/questions/2977649/is-there-an-easy-way-to-turn-empty-java-spring-form-input-into-null-strings
 *   https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-initbinder
 *   
 */

@ControllerAdvice
@Controller
public class BindingInitializer {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}