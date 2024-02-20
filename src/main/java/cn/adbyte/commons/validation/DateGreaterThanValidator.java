package cn.adbyte.commons.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

/**
 * @author adam
 */
public class DateGreaterThanValidator implements ConstraintValidator<DateGreaterThan, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value instanceof LocalDateTime param) {
            return LocalDateTime.now().isBefore(param);
        }

        return false;
    }
}
