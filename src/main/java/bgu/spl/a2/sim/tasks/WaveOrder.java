package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class represents a task which spawns new manufacture tasks
 * as many as the qty he receives as a parameter.
 */
public class WaveOrder extends Task<Queue<Product>> {

//    Fields:
    private final String product;
    private final int qty;
    private final long startId;
    private final Warehouse warehouse;

//    Methods:
    public WaveOrder(String product, int qty, long startId, Warehouse warehouse) {
        this.product = product;
        this.qty = qty;
        this.startId = startId;
        this.warehouse = warehouse;
    }

    @Override
    protected void start() {

        List<Task<Product>> tasks = new ArrayList<>();

        for(int i = 0; i < qty; i++) {

            Manufacture task = new Manufacture(product, startId + i, warehouse);
            tasks.add(task);
            spawn(task);
        }

        // When all tasks are completed, add them to a queue and complete with it
        whenResolved(tasks, () -> {

            Queue<Product> finishedProducts = new ConcurrentLinkedQueue<>();

            for(Task<Product> task : tasks) {

                finishedProducts.add(task.getResult().get());
            }

            complete(finishedProducts);
        });
    }

}
