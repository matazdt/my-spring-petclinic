package com.desnistamz.myspringpetclinic.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidatorTests {

    private Validator createValidator(){
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    void shouldBotValidateWhenFirstNameEmpty(){

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Person person = new Person();
        person.setFirst_name("");
        person.setLast_name("smith");

        Validator validator = createValidator();
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertThat(constraintViolations).hasSize(1);
        ConstraintViolation<Person> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }
}
