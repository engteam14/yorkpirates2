package com.mygdx.utils;

import org.junit.jupiter.api.function.Executable;

import java.lang.UnsupportedOperationException;

import java.util.*;

/**
 * A First in first out queue
 *
 * @param <T> Data type to store
 */
public class QueueFIFO<T> implements Queue<T> {
    private ArrayList<T> data;
    private int topIndex;

    /**
     * Initialize all properties
     */
    public QueueFIFO() {
        topIndex = -1;
        data = new ArrayList<>();
    }

    //Implemented for Testing
    public void set(ArrayList<T> setThis) {
        data = setThis;
        topIndex = size() -1;
    }

    public ArrayList<T> get() {
        return data; }

    public int getI() {
        return topIndex;
    }
    //End of Testing Functions
    
    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return data.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        topIndex++;
        return data.add(t);
    }

    @Override
    public boolean remove(Object o) {
        if (isEmpty()) {
            return false;
        }
        int i = data.indexOf(o);
        if (i == -1) {
            return false;
        }
        topIndex--;
        data.remove(i);
        return true;
    }

    public void remove(int index) {
        if (isEmpty()) {
            return;
        }
        topIndex--;
        data.remove(index);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return data.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean suc = data.addAll(c);
        if (suc) {
            topIndex = data.size() -1;
        }
        return suc;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean suc = data.removeAll(c);
        if (suc) {
            topIndex = data.size() -1;
        }
        return suc;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean suc = data.retainAll(c);
        if (suc) {
            topIndex = data.size() -1;
        }
        return suc;
    }

    @Override
    public void clear() {
        data.clear();
        topIndex = -1;

    }

    /**
     * Not implemented
     */
    @Override
    public boolean offer(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove() throws RuntimeException{
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        T t = data.remove(topIndex);
        topIndex--;
        return t;
    }

    public T pop() {
        return remove();
    }

    @Override
    public T poll() {
        if (isEmpty()) {
            return null;
        }
        topIndex--;
        return data.remove(topIndex + 1);
    }

    @Override
    public T element() throws RuntimeException{
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        return data.get(topIndex);
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return data.get(topIndex);
    }
}
