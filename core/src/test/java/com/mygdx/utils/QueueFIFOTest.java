package com.mygdx.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class QueueFIFOTest {

    @Test
    void size() {
        QueueFIFO<Object> queue3 = new QueueFIFO<>();
        queue3.set(new ArrayList<>(Arrays.asList(1,2,3)));
        QueueFIFO<Object> queue0 = new QueueFIFO<>();
        queue0.set(new ArrayList<>());
        assertAll(() -> assertEquals(queue3.size(), 3, "Failed to find size of (1,2,3)"), () -> assertEquals(queue0.size(), 0, "Failed to find size of Empty Queue"));
    }

    @Test
    void isEmpty() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        queue0.set(new ArrayList<>());

        QueueFIFO <Object> queue3 = new QueueFIFO<>();
        queue3.set(new ArrayList<>(Arrays.asList(1,2,3)));

        assertAll(() -> assertTrue(queue0.isEmpty()),
                  () -> assertFalse(queue3.isEmpty())) ;

    }

    @Test
    void contains() {
        QueueFIFO<Object> queue3 = new QueueFIFO<>();
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        queue0.set(new ArrayList<>());
        queue3.set(new ArrayList<>(Arrays.asList(1,2,3)));
        assertAll(() -> assertTrue(queue3.contains(1), "Cant find first value in array")
                , () -> assertTrue(queue3.contains(3), "Cant find last value in array")
                , () -> assertFalse(queue3.contains(5), "finds value when its not there")
                , () -> assertFalse(queue0.contains(1), "finds value in empty array"));

    }

    @Test
    void add() {
        QueueFIFO<Object> queue3 = new QueueFIFO<>();
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(1,2,3)));
        queue3.add(4);
        queue0.add(1);
        assertAll(() -> assertEquals(new ArrayList<>(Arrays.asList(1,2,3,4)), queue3.get(), "Adding values failed" )
                , () -> assertEquals( new ArrayList<>(Collections.singletonList(1)), queue0.get(), "Adding value to empty array failed"));
    }

    @Test
    void removeObject() {
        QueueFIFO<Object> queue3 = new QueueFIFO<>();
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        queue0.set(new ArrayList<>());
        queue3.set(new ArrayList<>(Arrays.asList("a","b","c")));
        String x = "b";
        String y = "e";

        assertAll(() -> assertTrue(queue3.remove(x), "Can't remove a value when present")
                , () -> assertEquals(new ArrayList<>(Arrays.asList("a","c")), queue3.get(), "Removing value unsuccessful" )
                , () -> assertFalse(queue3.remove(y), "removed a value even though its not in the array ")
                , () -> assertEquals(new ArrayList<>(Arrays.asList("a","c")), queue3.get(), "Array is not the same after an unsuccessful remove" )
                , () -> assertFalse(queue0.remove(x), "removed a value even though array is empty ")
                , () -> assertEquals( new ArrayList<>(), queue0.get(), "Array is not the same after an unsuccessful remove due to empty array")
                , () -> assertEquals(1, queue3.getI())
                , () -> assertEquals(-1, queue0.getI()));
    }

    @Test
    void removeIndex() {
        QueueFIFO<Object> queue3 = new QueueFIFO<>();
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(1,2,3)));
        queue3.remove(2);
        queue0.remove(1);
        // System.out.println(queue0.get());
        assertAll(() -> assertEquals(queue3.get(), new ArrayList<>(Arrays.asList(1,2)) )
                , () -> assertEquals(queue0.get(), new ArrayList<>())
                , () -> assertEquals(1, queue3.getI())
                , () -> assertEquals(-1, queue0.getI()));
    }

    @Test
    void addAll() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        Collection<Object> toAdd = new ArrayList<>(Arrays.asList(1,2,3));
        QueueFIFO<Object> queue3 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));
        assertAll(() -> assertTrue(queue0.addAll(toAdd))
                , () -> assertEquals(new ArrayList<>(Arrays.asList(1,2,3)), queue0.get())
                , () -> assertTrue(queue3.addAll(toAdd))
                , () -> assertEquals(new ArrayList<>(Arrays.asList(1,2,3,1,2,3)), queue3.get())
                , () -> assertEquals(5, queue3.getI())
                , () -> assertEquals(2, queue0.getI()));
    }

    @Test
    void removeAll() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        Collection<Object> toRemove = new ArrayList<>(Arrays.asList(1,3));
        QueueFIFO<Object> queue3 = new QueueFIFO<>() ;
        queue0.set(new ArrayList<>());
        queue3.set(new ArrayList<>(Arrays.asList(1,2,3)));
        assertAll(() -> assertFalse(queue0.removeAll(toRemove))
                , () -> assertTrue(queue3.removeAll(toRemove))
                , () -> assertEquals(new ArrayList<>(), queue0.get())
                , () -> assertEquals(new ArrayList<>(Collections.singletonList(2)), queue3.get())
                , () -> assertEquals(0, queue3.getI())
                , () -> assertEquals(-1, queue0.getI()));
    }

    @Test
    void retainAll() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        queue0.set(new ArrayList<>());
        Collection<Object> toRetain = new ArrayList<>(Arrays.asList(1,3,5));
        QueueFIFO<Object> queue3 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(1,2,3)));
        QueueFIFO<Object> queue6 = new QueueFIFO<>() ;
        queue6.set(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 5)));
        assertAll(() -> assertFalse(queue0.retainAll(toRetain))
                , () -> assertTrue(queue3.retainAll(toRetain))
                , () -> assertTrue(queue6.retainAll(toRetain))
                , () -> assertEquals(new ArrayList<>(), queue0.get())
                , () -> assertEquals(new ArrayList<>(Arrays.asList(1,3)), queue3.get())
                , () -> assertEquals(new ArrayList<>(Arrays.asList(1,3,5,5)), queue6.get())
                , () -> assertEquals(1, queue3.getI())
                , () -> assertEquals(-1, queue0.getI())
                , () -> assertEquals(3, queue6.getI())
        );
    }

    @Test
    void clear() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        QueueFIFO<Object> queue3 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));
        queue0.set(new ArrayList<>());
        queue3.clear();
        queue0.clear();
        assertAll(() -> assertEquals(new ArrayList<>(), queue0.get())
                , () -> assertEquals(new ArrayList<>(), queue3.get())
                , () -> assertEquals(-1, queue3.getI())
                , () -> assertEquals(-1, queue0.getI()));
    }

    /**
     * Not implemented
     */
    @Test
    void offer() {

    }

    @Test
    void pop() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        QueueFIFO<Object> queue3 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        try {
            queue0.pop();
        }
        catch (Throwable t) {
            assertInstanceOf(RuntimeException.class,t,"Runtime exception not thrown under correct circumstances");
        }
        assertAll(() -> assertEquals(3,queue3.pop(),"Incorrect value popped")
                , () -> assertEquals(new ArrayList<>(Arrays.asList(1,2)),queue3.get(),"Queue not correctly modified post-pop")
                , () -> assertEquals(1, queue3.getI(),"Top Index not correctly modified post-pop"));
    }

    @Test
    void poll() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        QueueFIFO<Object> queue3 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        assertAll(() -> assertNull(queue0.poll(), "Incorrect value popped")
                , () -> assertEquals(3,queue3.poll(),"Incorrect value popped")
                , () -> assertEquals(new ArrayList<>(Arrays.asList(1,2)),queue3.get(),"Queue not correctly modified post-pop")
                , () -> assertEquals(1, queue3.getI(),"Top Index not correctly modified post-pop"));
    }

    @Test
    void element() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        QueueFIFO<Object> queue3 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        try {
            queue0.element();
        }
        catch (Throwable t) {
            assertInstanceOf(RuntimeException.class,t,"Runtime exception not thrown under correct circumstances");
        }
        assertAll(() -> assertEquals(3,queue3.element(),"Incorrect value popped")
                , () -> assertEquals(new ArrayList<>(Arrays.asList(1,2,3)),queue3.get(),"Queue modified when shouldn't be")
                , () -> assertEquals(2, queue3.getI(),"Top Index modified when shouldn't be"));
    }

    @Test
    void peek() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        QueueFIFO<Object> queue3 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        try {
            queue0.element();
        }
        catch (Throwable t) {
            assertInstanceOf(RuntimeException.class,t,"Runtime exception not thrown under correct circumstances");
        }
        assertAll(() -> assertNull(queue0.peek(), "Incorrect value popped")
                , () -> assertEquals(3,queue3.peek(),"Incorrect value popped")
                , () -> assertEquals(new ArrayList<>(Arrays.asList(1,2,3)),queue3.get(),"Queue modified when shouldn't be")
                , () -> assertEquals(2, queue3.getI(),"Top Index modified when shouldn't be"));
    }

    @Test
    void toObjectArray() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        QueueFIFO<Object> queue3 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(1, 2, 3)));

        Object[] arrayQueue3 = new Object[3];
        for(int i = 0; i < arrayQueue3.length; i++) {
            arrayQueue3[i] = i + 1;
        }

        assertAll(() -> assertArrayEquals(new Object[0],queue0.toArray(), "Empty Queue converted incorrectly")
                , () -> assertArrayEquals(arrayQueue3,queue3.toArray(), "Queue converted incorrectly")
                , () -> assertEquals(new ArrayList<>(Arrays.asList(1,2,3)),queue3.get(),"Queue modified when shouldn't be"));
    }

    @Test
    void toT1Array() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        QueueFIFO<Object> queue3 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(0, 1, 2)));

        //Arrays to append
        Object[] emptyPrepend = new Object[0];
        Object[] mirrorPrepend = new Object[3];
        for(int i = 0; i < mirrorPrepend.length; i++) {
            mirrorPrepend[i] = 3 - i;
        }

        //Resultant Arrays to test
        Object[] mirrorTest = new Object[6];
        for(int i = 0; i < mirrorTest.length; i++) {
            mirrorTest[i] = Math.abs(3-i);
        }

        Object[] arrayQueue3 = new Object[3];
        for(int i = 0; i < arrayQueue3.length; i++) {
            arrayQueue3[i] = i;
        }

        assertAll(() -> assertArrayEquals(new Object[0],queue0.toArray(emptyPrepend), "Both empty fails")
                , () -> assertArrayEquals(arrayQueue3,queue3.toArray(emptyPrepend), "Prepend empty fails")
                , () -> assertArrayEquals(mirrorPrepend,queue0.toArray(mirrorPrepend), "Queue empty fails")
                , () -> assertArrayEquals(mirrorTest,queue3.toArray(mirrorPrepend), "Neither empty fails")
                , () -> assertEquals(new ArrayList<>(Arrays.asList(0,1,2)),queue3.get(),"Queue modified when shouldn't be"));
    }

    @Test
    void containsAll() {
        QueueFIFO<Object> queue0 = new QueueFIFO<>() ;
        QueueFIFO<Object> queue3 = new QueueFIFO<>() ;
        queue3.set(new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6)));

        ArrayList<Integer> testCollection = new ArrayList<>(Arrays.asList(0,2,4,6));
        ArrayList<Integer> testFailCollection = new ArrayList<>(Arrays.asList(0,2,4,6,8));

        assertAll(() -> assertFalse(queue0.containsAll(testCollection))
                , () -> assertTrue(queue3.containsAll(testCollection))
                , () -> assertFalse(queue3.containsAll(testFailCollection))
        );
    }
}