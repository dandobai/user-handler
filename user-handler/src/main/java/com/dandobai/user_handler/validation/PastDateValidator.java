package com.dandobai.user_handler.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PastDateValidator implements ConstraintValidator<ValidPastDate, String> {

    @Override
    public boolean isValid(String dateStr, ConstraintValidatorContext context) {
        if (dateStr == null || dateStr.isBlank()) return false;
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.isBefore(LocalDate.now()); // Ensures the date is in the past
        } catch (DateTimeParseException e) {
            return false; // Invalid format
        }
    }
}
