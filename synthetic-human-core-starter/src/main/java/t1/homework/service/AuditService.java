package t1.homework.service;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import t1.homework.audit.AuditSender;


@AllArgsConstructor
public class AuditService {

    private final AuditSender sender;

    public void logEvent(String methodName, Object[] args, Object result) {
        String message = String.format(
            "Метод: %s | Параметры: %s | Результат: %s",
            methodName, Arrays.toString(args), result
        );
        sender.send(message);
    }
}