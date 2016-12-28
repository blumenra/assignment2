package bgu.spl.a2;

import bgu.spl.a2.test.MergeSort;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.*;

/**
 * Created by blumenra on 12/27/16.
 */
public class WorkStealingThreadPoolTest {

    @Test
    public void WorkStealingThreadPoolTest() throws Exception {

        int nthreads = 4;
        WorkStealingThreadPool pool = new WorkStealingThreadPool(nthreads);

        assertEquals(nthreads, pool.getDeques().size());

        Class<?> instanceDPool= pool.getClass();
        Field[] fields = instanceDPool.getDeclaredFields();
        fields[1].setAccessible(true);

        assertNotNull(fields[1].get(pool));
    }

    @Test
    public void submit() throws Exception {

        int nthreads = 4;
        WorkStealingThreadPool pool = new WorkStealingThreadPool(nthreads);

        int[] array = {5, 1};
        MergeSort task = new MergeSort(array);

        for(ConcurrentLinkedDeque<Task<?>> deque : pool.getDeques()) {

            assertTrue("All deques should be empty before submit", deque.size() == 0);
        }

        pool.submit(task);

        int cumulativeSize = 0;
        for(ConcurrentLinkedDeque<Task<?>> deque : pool.getDeques()) {

            cumulativeSize += deque.size();
        }

        assertTrue("All deques should be empty except for one and only one deque with size 1 after submit", cumulativeSize == 1);


    }

    @Test
    public void shutdown() throws Exception {

    }

    @Test
    public void start() throws Exception {

    }

}