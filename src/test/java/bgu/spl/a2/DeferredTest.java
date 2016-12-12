package bgu.spl.a2;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by blumenra on 12/12/16.
 */
public class DeferredTest {
//    @Test
//    public void get() throws Exception {
//
//
//    }
//
    @Test
    public void isResolved() throws Exception {

        Deferred<int[]> deferred = new Deferred<int[]>();
        int[] ints = new int[10];

        assertFalse(deferred.isResolved());

        deferred.resolve(ints);

        assertTrue(deferred.isResolved());

    }
//
//    @Test
//    public void resolve() throws Exception {
//
//    }
//
//    @Test
//    public void whenResolved() throws Exception {
//
//    }

}