package bgu.spl.a2;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by blumenra on 12/12/16.
 */
public class DeferredTest {
    @Test
    public void get() throws Exception {

        Deferred<int[]> deferred = new Deferred<int[]>();
        int[] ints = {2, 1};

        deferred.resolve(ints);

        int[] expectedResult = {2, 1};

        assertArrayEquals("should be [1, 2] result", expectedResult, deferred.get());
    }

    @Test
    public void isResolved() throws Exception {

        Deferred<int[]> deferred = new Deferred<int[]>();
        int[] ints = new int[10];

        assertFalse("isResolved should be false before resolve", deferred.isResolved());

        deferred.resolve(ints);

        assertTrue("isResolved should be true after resolve", deferred.isResolved());

    }

    @Test
    public void resolve() throws Exception {


        Deferred<int[]> deferred = new Deferred<int[]>();
        int[] ints = new int[10];
        Runnable callback = () -> System.out.println("deferred's callback (deferred resolve test)");

        Class<?> instanceDefer = deferred.getClass();
        Field[] fields = instanceDefer.getDeclaredFields();
        fields[0].setAccessible(true);
        fields[1].setAccessible(true);

        List<Runnable> tmpCallbacks = (List<Runnable>) fields[0].get(deferred);

        assertNull(fields[1].get(deferred));

        deferred.resolve(ints);

        assertEquals("When List of callbacks was empty", 0, tmpCallbacks.size());
        assertNotNull(fields[1].get(deferred));

        deferred.whenResolved(callback);
        deferred.whenResolved(callback);
        deferred.whenResolved(callback);
        deferred.whenResolved(callback);


        deferred.resolve(ints);

        assertNotNull(fields[1].get(deferred));
        assertEquals("When List of callbacks was NOT empty", 0, tmpCallbacks.size());






    }

    @Test
    public void whenResolved() throws Exception {

        Deferred<int[]> deferred = new Deferred<int[]>();

        Runnable callback = () -> System.out.println("deferred's callback");

        Class<?> instanceDefer = deferred.getClass();
        Field[] fields = instanceDefer.getDeclaredFields();
        fields[0].setAccessible(true);


        List<Runnable> tmpCallbacks = (List<Runnable>) fields[0].get(deferred);
        int numOfCallbacks = tmpCallbacks.size();

        deferred.whenResolved(callback);

        assertEquals("callbacks list size should increase by 1",numOfCallbacks+1, tmpCallbacks.size());
    }

}