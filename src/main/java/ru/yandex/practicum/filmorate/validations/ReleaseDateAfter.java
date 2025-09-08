package ru.yandex.practicum.filmorate.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

@Constraint(validatedBy = ReleaseDateAfterValidator.class)

public @interface ReleaseDateAfter {
    String message() default "Дата не может быть раньше установленной";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();

}
