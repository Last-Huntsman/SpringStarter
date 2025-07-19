package org.zuzukov.synthetichumancorestarter.commands;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zuzukov.synthetichumancorestarter.metrics.AndroidMetrics;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class CommandWorker {
    private static final Logger logger = LoggerFactory.getLogger(CommandWorker.class);
    private final CommandQueue commandQueue;
    private final ThreadPoolExecutor threadPoolExecutor;

    private final AndroidMetrics androidMetrics;
    private final MeterRegistry meterRegistry;


    public CommandWorker(@Autowired CommandQueue commandQueue, @Autowired AndroidMetrics androidMetrics,@Autowired MeterRegistry meterRegistry) {
        this.androidMetrics = androidMetrics;
        this.meterRegistry = meterRegistry;
        this.commandQueue = commandQueue;
        this.threadPoolExecutor = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));

        androidMetrics.registerQueueSize(meterRegistry, threadPoolExecutor);

        starterThreadPool();

    }

    private void starterThreadPool() {
        Thread thread = new Thread(() -> {
            while (true) {
                Command command = commandQueue.pop();
                if (command != null) {
                    try {
                        threadPoolExecutor.submit(() -> {
                            try {
                                logger.info("Выполняется команда:{}", command);
                                Thread.sleep(5000);
                                androidMetrics.taskCompletedBy(command.getAuthor(), meterRegistry);
                            } catch (Exception e) {
                                logger.error("Ошибка при выполнении команды: {}", command, e);
                            }
                        });
                    } catch (RejectedExecutionException exception) {
                        logger.info("Очередь переполнена, команда:{} ожидает", command);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        commandQueue.pushFront(command);
                    }

                } // TODO: Аудит, метрики, try/catch
                else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }, "CommandDispatcherThread");
        thread.start();


    }
}
