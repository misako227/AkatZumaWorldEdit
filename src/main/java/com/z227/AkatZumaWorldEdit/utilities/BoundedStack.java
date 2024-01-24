package com.z227.AkatZumaWorldEdit.utilities;

import java.util.ArrayDeque;

public class BoundedStack<T> {
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

    //移除并返回队列中的最后一个元素
    public T pop() {
        if (stack.size() <= 0) {
            return null;
        }
        return stack.pop();
    }

    //移除并返回队列中的第一个元素
    public T pollFirst() {
        if (stack.size() <= 0) {
            return null;
        }
        return stack.pollFirst();
    }

    public T peek() {
        return stack.peek();
    }

    public int size() {
        return stack.size();
    }


    public ArrayDeque<T> getStack() {
        return this.stack;
    }
}