package com.LubieKakao1212.opencu.forge.util.transaction;

import com.LubieKakao1212.opencu.common.transaction.IScopedContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScopedContext implements IScopedContext {

    private final List<IScopeClosable> closables = new ArrayList<>();

    private final Stack<AtomicBoolean> resultStack = new Stack<>();

    public ScopedContext() {
        resultStack.push(new AtomicBoolean(false));
    }

    public void registerClosable(IScopeClosable closable) {
        closables.add(closable);
    }

    public void takeInitialSnapshots() {
        for (var closable : closables) {
            closable.takeSnapshot();
        }
    }

    @Override
    public void push() {
        resultStack.push(new AtomicBoolean(false));

        closables.forEach(IScopeClosable::onPush);
    }

    @Override
    public void pop() {
        if(resultStack.size() <= 1) {
            throw new IllegalStateException("push < pop");
        }
        var result = resultStack.pop().get();
        closables.forEach(closable -> closable.onPop(result));
    }

    @Override
    public void commit() {
        resultStack.peek().set(true);
    }

    @Override
    public void close() {
        if(resultStack.size() != 1){
            throw new IllegalStateException("push > pop");
        }
        var result = resultStack.pop().get();
        closables.forEach(closable -> closable.onPop(result));
        resultStack.clear();
    }
}
