package bgu.spl.a2;

import bgu.spl.a2.test.MergeSort;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by blumenra on 12/28/16.
 */
public class ProcessorTest {

    private int nthreads;
    private WorkStealingThreadPool pool;

    @Before
    public void setUp() throws Exception {

        nthreads = 2;
        pool = new WorkStealingThreadPool(nthreads);
        pool.start();
    }

    @Test
    public void run() throws Exception {

        int[] ints = {5, 2};

        MergeSort task = new MergeSort(ints);
        System.out.println("Before submit");
        pool.submit(task);

    }

    @Test
    public void addToMyDeque() throws Exception {


    }

}