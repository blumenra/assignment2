package bgu.spl.a2;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {

    private ArrayList<ConcurrentLinkedDeque<Task<?>>> dequesOfProcessors;
    private ArrayList<Thread> tProcessors;
    private VersionMonitor vm;
    private CountDownLatch latch;

    /**
     * creates a {@link WorkStealingThreadPool} which has nthreads
     * {@link Processor}s. Note, threads should not get started until calling to
     * the {@link #start()} method.
     *
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this
     * thread pool
     */
    public WorkStealingThreadPool(int nthreads) {

        this.dequesOfProcessors = new ArrayList<>();

        for(int i = 0; i < nthreads; i++){

            this.dequesOfProcessors.add(new ConcurrentLinkedDeque<>());
        }

        vm = new VersionMonitor();
        tProcessors = new ArrayList<>();

        this.latch = new CountDownLatch(dequesOfProcessors.size());
    }

    /**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task) {

        int randomNumber = (int) ((Math.random())*(dequesOfProcessors.size()));
        this.dequesOfProcessors.get(randomNumber).addLast(task);
        vm.inc();
    }

    /**
     * closes the thread pool - this method interrupts all the threads and wait
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     *
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is
     * interrupted
     * @throws UnsupportedOperationException if the thread that attempts to
     * shutdown the queue is itself a processor of this queue
     */
    public void shutdown() throws InterruptedException {

        for(Thread t : tProcessors) {
            t.interrupt();
        }

        latch.await();
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {

        for(int i = 0; i < dequesOfProcessors.size(); i++) {

            Processor processor = new Processor(i, this);
            Thread thread = new Thread(processor);
            thread.start();
            tProcessors.add(thread);
        }

    }

    /*package*/ ArrayList<ConcurrentLinkedDeque<Task<?>>> getDeques() {

        return dequesOfProcessors;
    }

    /*package*/ VersionMonitor getVersionMonitor() {

        return vm;
    }

    /*package*/ ConcurrentLinkedDeque<Task<?>> getDeque(int i) {

        return dequesOfProcessors.get(i);
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
