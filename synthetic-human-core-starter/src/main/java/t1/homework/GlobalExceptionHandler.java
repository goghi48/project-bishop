package t1.homework;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Ошибка: }", ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<String> handleEnumConversion(HttpMessageConversionException ex) {
        log.error("Ошибка: }", ex.getMessage(), ex);

         String validPriorityTypes = Arrays.stream(CommandPriority.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest()
                .body("Неверное значение для поля commandPriority. Допустимые значения: " + validPriorityTypes);
    }

    @ExceptionHandler(RejectedExecutionException.class)
    public ResponseEntity<String> handleQueueFull(RejectedExecutionException ex) {
        log.error("Ошибка: ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .body("Ошибка: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpected(Exception ex) {
        log.error("Ошибка: }", ex.getMessage(), ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ошибка сервера(sorry)");
    }

    
}
