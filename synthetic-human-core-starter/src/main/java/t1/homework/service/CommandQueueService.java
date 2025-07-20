package t1.homework.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import t1.homework.Command;
import t1.homework.CommandPriority;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CommandQueueService {
    private static final int MAX_QUEUE_SIZE = 100;
    private final ThreadPoolExecutor executor;
    private final MeterRegistry meterRegistry;
    private final Counter rejectedCommandsCounter;
    private final Timer commandExecutionTimer;
    private final AtomicInteger criticalCounter = new AtomicInteger();
    private final AtomicInteger commonCounter = new AtomicInteger();

    public CommandQueueService(MeterRegistry meterRegistry) {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
        this.meterRegistry = meterRegistry;

        this.executor = new ThreadPoolExecutor(
            1, 1, 0, TimeUnit.SECONDS,
            queue,
            new ThreadPoolExecutor.AbortPolicy()
        );

        Gauge.builder("android.queue.size", executor, ex -> ex.getQueue().size())
            .description("Current command queue size")
            .register(meterRegistry);

        this.rejectedCommandsCounter = Counter.builder("android.commands.rejected")
            .description("Total rejected commands count")
            .register(meterRegistry);

        this.commandExecutionTimer = Timer.builder("android.command.execution.time")
            .description("Command execution time distribution")
            .register(meterRegistry);

        Gauge.builder("android.commands.critical.count", criticalCounter, AtomicInteger::get)
            .description("Total executed CRITICAL commands")
            .register(meterRegistry);

        Gauge.builder("android.commands.common.count", commonCounter, AtomicInteger::get)
            .description("Total executed COMMON commands")
            .register(meterRegistry);
    }

    public void processCommand(Command command) {
        Runnable task = () -> {
            commandExecutionTimer.record(() -> {
                try {
                    int randomDelay = ThreadLocalRandom.current().nextInt(10, 31);
                    Thread.sleep(randomDelay * 1000L); //10-30sec
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (command.getCommandPriority() == CommandPriority.CRITICAL) {
                    criticalCounter.incrementAndGet();
                } else {
                    commonCounter.incrementAndGet();
                }

            });
        };

        if (command.getCommandPriority() == CommandPriority.CRITICAL) {
            task.run();
        } else {
            try {
                if (executor.getQueue().size() >= MAX_QUEUE_SIZE) {
                    rejectedCommandsCounter.increment();
                    throw new RejectedExecutionException("Command queue is full");
                }
                executor.submit(task);
            } catch (RejectedExecutionException e) {
                rejectedCommandsCounter.increment();
                throw e;
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }
}
