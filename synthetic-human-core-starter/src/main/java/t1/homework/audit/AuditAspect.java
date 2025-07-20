package t1.homework.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import t1.homework.service.AuditService;

@Slf4j
@Aspect
@AllArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Around("@annotation(WeylandWatchingYou)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Executing method: {}", methodName);
        Object result = joinPoint.proceed();
        log.info("Finished method: {}", methodName);
        auditService.logEvent(methodName, args, result);
        return result;
    }
}
