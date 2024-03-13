package br.com.ada.biblioteca.usuario;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UnicoNomeEmailValidador.class)
@Documented
public @interface UnicoNomeEmail {
    String message() default "Combinação de nome e email já existe.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
