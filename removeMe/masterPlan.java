
ExecutorService executor = Executor.newFixedThreadPool(nThreads);

for(...) {
	Thread t = new Thread();
	executor.submit(tt);
}


executor.shutdown();


//**********WorkStealingThreadPool

public class WorkStealingThreadPool{

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
			processors = Executors.newFixedThreadPool(nthreads);
		}

	methods:
		public void submit(Task<?> task) {

			int randomNumber = (int) (Math.random())*(processors.size());

			this.dequesOfProcessors[randomNumber].addLast(task);
            vm.inc();
		}

		public void shutdown() throws InterruptedException {

			processors.shutdown();
		}

		public void start() {

			for(int i = 0; i < dequesOfProcessors.size(); i++) {
				Processor processor = new Processor(id: i, this);
				Thread t = new Thread(processor);
				processors.submit(t);
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
    /*package*/ addToMyDeque(Task<?> task) {
        
        pool.getDeque(id).addFirst(task);
        pool.getVersionMonitor().inc();
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
		ArrayList<Deferred<bla>> deferreds;
        int target;
        AtomicInteger myVM = new AtomicInteger();
        boolean readyToComplete = false;
        Runnable myCallback;
        Collection<? extends Task<?>> tasks;
		Deferred deferred = new Deferred();

    Methods:
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
            
            this.processor = handler;

            if(readyToComplete) {

                myCallback.run();
            }
            else {

                start();                
            }


            // //TODO: replace method body with real implementation
            // throw new UnsupportedOperationException("Not Implemented Yet.");
        }

        /**
         * This method schedules a new task (a child of the current task) to the
         * same processor which currently handles this task.
         *
         * @param task the task to execute
         */
        protected final void spawn(Task<?>... task) {
            
            processor.addToMyDeque(task);


            // //TODO: replace method body with real implementation
            // throw new UnsupportedOperationException("Not Implemented Yet.");
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
            
            this.myCallback = callback;
            this.readyToComplete = true;
            this.myVM = new AtomicInteger(tasks.size());

            VersionMonitor parentVM = this.myVM;
            Task<?> parentTask = this;

            Runnable callbackForChild = new Runnable() {

                @Override
                void run() {

                    synchronize(parentVM) {

                        if(parentVM.decrementAndGet() == 0) {

                            // callback.run();
                            spawn(parentTask);
                        }
                    }
                }
            };

            for(Task<?> task : tasks) {
                
                if(task.getResult().isResolved()) {

                    callbackForChild.run();
                }
                else {

                    task.getResult().whenResolved(callbackForChild);
                }

            }
        }

        /**
         * resolve the internal result - should be called by the task derivative
         * once it is done.
         *
         * @param result - the task calculated result
         */
        protected final void complete(R result) {
         
            deferred.resolve(result);

            // if(deferred.isResolved()) {

            //     deferred.getCallback.run();
            // }


            // //TODO: replace method body with real implementation
            // throw new UnsupportedOperationException("Not Implemented Yet.");
        }

        /**
         * @return this task deferred result
         */
        public final Deferred<R> getResult() {
            
            return this.deferred;

            // //TODO: replace method body with real implementation
            // throw new UnsupportedOperationException("Not Implemented Yet.");
        }

        protected 

}
//**********Task

//**********Deferred<E>
public class Deferred<T> {

    private ArrayList<Runnable> callbacks = new ArrayList<>();
    private T result = null;
    /**
     *
     * @return the resolved value if such exists (i.e., if this object has been
     * {@link #resolve(java.lang.Object)}ed yet
     * @throws IllegalStateException in the case where this method is called and
     * this object is not yet resolved
     */
    public T get() {

        if(isResolved()) {
            return result;
        }

        throw new IllegalStateException("The object is not yet resolved");



        // //TODO: replace method body with real implementation
        // throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    /**
     *
     * @return true if this object has been resolved - i.e., if the method
     * {@link #resolve(java.lang.Object)} has been called on this object before.
     */
    public boolean isResolved() {
        
        return !result.isNull();

        // //TODO: replace method body with real implementation
        // throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    /**
     * resolve this deferred object - from now on, any call to the method
     * {@link #get()} should return the given value
     *
     * Any callbacks that were registered to be notified when this object is
     * resolved via the {@link #whenResolved(java.lang.Runnable)} method should
     * be executed before this method returns
     *
     * @param value - the value to resolve this deferred object with
     * @throws IllegalStateException in the case where this object is already
     * resolved
     */
    public void resolve(T value) {

        this.result = value;

        // not sure if it is better to run over the list and execute each callback, or to do it as follows...
        while(!callbacks.isEmpty()){
            Runnable tmpCallback = callbacks.get(0);
            callbacks.remove(0);
            tmpCallback.run();
        }

    }

    /**
     * add a callback to be called when this object is resolved. if while
     * calling this method the object is already resolved - the callback should
     * be called immediately
     *
     * Note that in any case, the given callback should never get called more
     * than once, in addition, in order to avoid memory leaks - once the
     * callback got called, this object should not hold its reference any
     * longer.
     *
     * @param callback the callback to be called when the deferred object is
     * resolved
     */
    public void whenResolved(Runnable callback) {

        this.callbacks.add(callback);

        //TODO: replace method body with real implementation
//        throw new UnsupportedOperationException("Not Implemented Yet.");
    }


}



//**********Deferred<E>