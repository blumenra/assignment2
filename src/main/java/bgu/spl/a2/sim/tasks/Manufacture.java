package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brukes on 12/29/16.
 */
public class Manufacture extends Task<Product> {

    //    Fields:
    private final String productName;
    private final long startId;
    private final Warehouse warehouse;

    //    Methods:
    public Manufacture(String product, long startId, Warehouse warehouse) {

        this.productName = product;
        this.startId = startId;
        this.warehouse = warehouse;
    }

    @Override
    protected void start() {

        ManufactoringPlan plan = warehouse.getPlan(productName);
        List<Task<Product>> tasks = new ArrayList<>();

        String[] parts = plan.getParts();

        if(parts.length == 0){

            Product product = new Product(this.startId, this.productName);
            product.setFinalId(this.startId);

            complete(product);
        }
        else {
            for (String part : parts) {

                Manufacture task = new Manufacture(part, startId + 1, warehouse);
                tasks.add(task);
                spawn(task);
            }

            whenResolved(tasks, () -> {

                List<Product> finishedParts = new ArrayList<>();
                List<Task<Long>> toolTasks = new ArrayList<>();

                for (Task<Product> task : tasks) {

                    finishedParts.add(task.getResult().get());
                }

                for (String tool : plan.getTools()) {

                    UseTool task = new UseTool(tool, finishedParts, warehouse);
                    toolTasks.add(task);
                    spawn(task);

                }

                whenResolved(toolTasks, () -> {

                    long sum = this.startId;

                    for (Task<Long> toolTask : toolTasks) {

                        sum += toolTask.getResult().get();
                    }

                    Product product = new Product(this.startId, this.productName);
                    product.setFinalId(sum);
                    complete(product);
                });

            });
        }
    }
}
