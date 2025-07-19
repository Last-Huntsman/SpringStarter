package org.zuzukov.synthetichumancorestarter.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import org.zuzukov.synthetichumancorestarter.commands.CommandQueue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

@Component
public class AndroidMetrics {

    private final Map<String, Counter> completedByAuthor = new ConcurrentHashMap<>();

    private boolean gaugeRegistered = false;

    public void registerQueueSize(MeterRegistry registry, ThreadPoolExecutor threadPoolExecutor) {
        if (!gaugeRegistered) {
            Gauge.builder("android.task.queue.size", threadPoolExecutor, executor -> executor.getQueue().size())
                    .description("Current thread pool task queue size")
                    .tag("application", "synthetic-bishop")
                    .register(registry);


        }
    }

    public void taskCompletedBy(String author, MeterRegistry registry) {
        completedByAuthor
                .computeIfAbsent(author, key ->
                        Counter.builder("android.task.completed")
                                .description("Completed tasks per author")
                                .tag("author", key)
                                .register(registry)
                ).increment();
    }
}
