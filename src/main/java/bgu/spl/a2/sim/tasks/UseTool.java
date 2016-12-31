package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.tools.Tool;

import java.util.List;

/**
 * The class represents a task which tries to acquire his tool
 * from the warehouse. if he succeeds, he uses it on each part of his product.
 * Once he finishes that, he completes.
 */
public class UseTool extends Task<Long> {

//    Fields:
    private String toolName;
    private final List<Product> parts;
    private final Warehouse warehouse;
    private Deferred<Tool> deferredTool;

//    Methods:
    public UseTool(String tool, List<Product> parts, Warehouse warehouse) {

        this.toolName = tool;
        this.parts = parts;
        this.warehouse = warehouse;
    }

    @Override
    protected void start() {

        this.deferredTool = warehouse.acquireTool(toolName);

        // When the tool is available, use the tool on each part and sum them
        whenToolIsAvaiable(this.deferredTool, () -> {

            Long sum = new Long(0);

            Tool tool = deferredTool.get();

            for(Product part : parts) {

                sum += Math.abs(tool.useOn(part));
            }

            warehouse.releaseTool(tool);

            complete(sum);
        });

    }

    /**
     * This methods receives {@link #deferredTool} which will hold  as the code to execute
     * once the tool is available in the warehouse. {@link #deferredTool} will be resolved by the warehouse.
     *
     * @param deferredTool
     * @param callback
     */
    private void whenToolIsAvaiable(Deferred<Tool> deferredTool, Runnable callback) {

        synchronized (deferredTool) {

            if(deferredTool.isResolved()) {

                callback.run();
            }
            else {

                deferredTool.whenResolved(callback);
            }
        }
    }

}
