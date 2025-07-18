package org.zuzukov.synthetichumancorestarter.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class CommandWorker {
    private static final Logger logger = LoggerFactory.getLogger(CommandWorker.class);

    private CommandQueue commandQueue;
    private ThreadPoolExecutor threadPoolExecutor;

    public CommandWorker(@Autowired CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
        this.threadPoolExecutor = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
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
