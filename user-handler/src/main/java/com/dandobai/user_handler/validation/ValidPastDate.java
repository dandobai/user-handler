package com.dandobai.user_handler.validation;

import jakarta.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PastDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPastDate {
    String message() default "Birthday must be a valid past date (yyyy-MM-dd)";
    Class<?>[] groups() default {};
    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
