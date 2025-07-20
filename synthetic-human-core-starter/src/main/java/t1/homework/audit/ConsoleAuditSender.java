package t1.homework.audit;

public class ConsoleAuditSender implements AuditSender {
    @Override
    public void send(String message) {
        System.out.println("[AUDIT] " + message);
    }
}