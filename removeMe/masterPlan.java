
ExecutorService executor = Executor.newFixedThreadPool(nThreads);

for(...) {
	Thread t = new Thread();
	executor.submit(tt);
}


executor.shutdown();


//**********WorkStealingThreadPool

public class WorkStealingThreadPool{

	private_class:
		private static class WorkStealingThreadPoolSingleton {
			private static WorkStealingThreadPool instance = new WorkStealingThreadPool()
		}


	//SINGELTON IT!!!!
	fields:
    	public ArrayList<ConcurrentLinkedDeque<Task<?>>> dequesOfProcessors;
		ExecutorService processors;
    	private VersionMonitor vm;

	constructor:
		public WorkStealingThreadPool(int nthreads) {


	        this.dequesOfProcessors = new ArrayList<ConcurrentLinkedDeque<Task<?>>>();

	        for(int i = 0; i < nthreads; i++){

	            this.dequesOfProcessors.add(new ConcurrentLinkedDeque<Task<?>>());
	        }
			processors = Executor.newFixedThreadPool(nthreads);
		}

	methods:
		public void submit(Task<?> task) {

			int randomNumber = (int) (Math.random())*(processors.size());

			this.dequesOfProcessors[randomNumber].addLast(task);
		}

		public void shutdown() throws InterruptedException {

			processors.shutdown();
		}

		public void start() {

			for(int i = 0; i < processors.size(); i++) {
				Processor processor = new Processor(id: i, this);
				Thread t = new Thread(processor);
				processors.submit(t);
			}
		}

		public ArrayList<ConcurrentLinkedDeque<Task<?>>> getDeques() {

			return dequesOfProcessors;
		}

		public VersionMonitor getVersionMonitor() {

			return vm;
		}

		public ConcurrentLinkedDeque<Task<?>> getDeque(int i) {

			return dequesOfProcessors.get(i);
		}		
}

//**********WorkStealingThreadPool



//**********Processor
public class Processor implements Runnable {

	fields:
		private final WorkStealingThreadPool pool;
    	private final int id;

    methods:
    	public void run() {

    		while(someCondition(true??)){

    			while(pool.getDeque(id).isEmpty()) {

			    	if(!steal()){

			    		pool.getVersionMonitor().await();
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

    					pool.getDeque(id).addFirst(tempTask);
    				}
    			}

    			if(!pool.getDeque(id).isEmpty()){

    				break;
    			}

    		}

    		return !pool.getDeque(id).isEmpty();
    	}

}
//**********Processor

//**********Task

/**
 * an abstract class that represents a task that may be executed using the
 * {@link WorkStealingThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the task result type
 */
public abstract class Task<R> {


	Fields:
		Processor processor;
		Deferred deferred;
		

    /**
     * start handling the task - note that this method is protected, a handler
     * cannot call it directly but instead must use the
     * {@link #handle(bgu.spl.a2.Processor)} method
     */
    protected abstract void start();

    /**
     *
     * start/continue handling the task
     *
     * this method should be called by a processor in order to start this task
     * or continue its execution in the case where it has been already started,
     * any sub-tasks / child-tasks of this task should be submitted to the queue
     * of the handler that handles it currently
     *
     * IMPORTANT: this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     * @param handler the handler that wants to handle the task
     */
    /*package*/ final void handle(Processor handler) {
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    /**
     * This method schedules a new task (a child of the current task) to the
     * same processor which currently handles this task.
     *
     * @param task the task to execute
     */
    protected final void spawn(Task<?>... task) {
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    /**
     * add a callback to be executed once *all* the given tasks results are
     * resolved
     *
     * Implementors note: make sure that the callback is running only once when
     * all the given tasks completed.
     *
     * @param tasks
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void whenResolved(Collection<? extends Task<?>> tasks, Runnable callback) {
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    /**
     * resolve the internal result - should be called by the task derivative
     * once it is done.
     *
     * @param result - the task calculated result
     */
    protected final void complete(R result) {
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    /**
     * @return this task deferred result
     */
    public final Deferred<R> getResult() {
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

}
//**********Task