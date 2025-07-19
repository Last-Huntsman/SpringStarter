package org.zuzukov.synthetichumancorestarter.commands;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.zuzukov.synthetichumancorestarter.error.QueueOverflowException;

import java.util.Deque;
import java.util.LinkedList;

@Component
public class CommandQueue {
    private final int QUEUELIMITED = 100;
    private Deque<Command> commandQueue = new LinkedList<>();

    public void push(@Valid Command command) {
        if (!isNotFull()) throw new QueueOverflowException("Command queue overflow");
        else commandQueue.add(command);
    }
    public void pushFront(@Valid Command command) {
        if (!isNotFull()) throw new QueueOverflowException("Command queue overflow");
        else commandQueue.addFirst(command);
    }
    public Command pop() {
        return commandQueue.poll();
    }
    public int size() {
        return commandQueue.size();
    }
    private boolean isNotFull() {
        return QUEUELIMITED >= commandQueue.size();
    }

}
