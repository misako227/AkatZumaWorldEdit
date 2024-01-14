package com.z227.AkatZumaWorldEdit.utilities;

import java.util.ArrayDeque;

class BoundedStack<T> {
    private final ArrayDeque<T> stack;
    private final int maxSize;

    public BoundedStack(int maxSize) {
        this.maxSize = maxSize;
        this.stack = new ArrayDeque<>(maxSize);
    }

    public void push(T item) {
        if (stack.size() >= maxSize) {
            stack.removeLast(); // 移除栈底元素
        }
        stack.push(item); // 执行push操作
    }

    public T pop() {
        return stack.pop();
    }

    public T peek() {
        return stack.peek();
    }

    public int size() {
        return stack.size();
    }

    public ArrayDeque getStack() {
        return this.stack;
    }
}