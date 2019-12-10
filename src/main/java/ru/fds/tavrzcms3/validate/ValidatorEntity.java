package ru.fds.tavrzcms3.validate;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidatorEntity {

    private String errorMessage;

    public <T>Set<ConstraintViolation<T>> validateEntity(T entity){
        Validator validator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();
        Set<ConstraintViolation<T>> violations;
        violations = validator.validate(entity);

        if(!violations.isEmpty()){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Ошибка валидации: ");
            for (ConstraintViolation<T> c : violations)
                stringBuilder.append(c.getMessage()).append("(").append(c.getInvalidValue()).append(") в поле: \"").append(c.getPropertyPath()).append("\". ");

            errorMessage = stringBuilder.toString();
        }

        return violations;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
