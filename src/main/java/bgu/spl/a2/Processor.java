package bgu.spl.a2;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 */
public class Processor implements Runnable {

    private final WorkStealingThreadPool pool;
    private final int id;


    /**
     * constructor for this class
     *
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id - the processor id (every processor need to have its own unique
     * id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    /*package*/ Processor(int id, WorkStealingThreadPool pool) {
        this.id = id;
        this.pool = pool;
    }

    @Override
    public void run() {

        while(true){ //TODO: The condition is not determinde yet!!!

            System.out.println(id);
            while(pool.getDeque(id).isEmpty()) {

                if(!steal()){

                    int currentVersion = pool.getVersionMonitor().getVersion();
                    try {
                        pool.getVersionMonitor().await(currentVersion);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            pool.getDeque(id).removeFirst().handle(this);
        }
    }

    private boolean steal() {

        int nthreads = pool.getDeques().size(); // number of processors

        for(int i = (id+1)%nthreads; i != id; i = (i+1) % nthreads) {

            int numOfTasks = pool.getDeque(i).size();

            Task<?> tempTask;

            for(int j = 0; j < numOfTasks/2; j++) {

                tempTask = pool.getDeque(i).pollLast();
                if(tempTask != null) {

                    // pool.getDeque(id).addFirst(tempTask); //maybe its better with this line instead of the next but could not justify it...
                    addToMyDeque(tempTask);
                }
            }

            if(!pool.getDeque(id).isEmpty()){

                break;
            }

        }

        return !pool.getDeque(id).isEmpty();
    }

    //This method is package protected so it's ok to be defined nanabanana
    /*package*/ void addToMyDeque(Task<?> task) {

        pool.getDeque(id).addFirst(task);
        pool.getVersionMonitor().inc();
    }
}
