package t1.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import t1.homework.audit.AuditAspect;
import t1.homework.audit.AuditSender;
import t1.homework.audit.ConsoleAuditSender;
import t1.homework.audit.KafkaAuditSender;
import t1.homework.service.AuditService;

@Configuration
@EnableAspectJAutoProxy
@ConditionalOnProperty(name = "weyland.audit.enabled", havingValue = "true")
public class AuditConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.topic}")
    private String topic;

    @Bean
    public AuditAspect auditAspect(AuditService auditService) {
        return new AuditAspect(auditService);
    }

    @Bean
    public AuditSender auditSender(@Value("${weyland.audit.destination}") String destination) {
        if ("kafka".equalsIgnoreCase(destination)) {
            return new KafkaAuditSender(bootstrapServers, topic);
        }
        return new ConsoleAuditSender();
    }

    @Bean
    public AuditService auditService(AuditSender sender) {
        return new AuditService(sender);
    }
}
