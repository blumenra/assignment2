package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.tools.Tool;

import java.util.List;

/**
 * Created by brukes on 12/29/16.
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

    private void whenToolIsAvaiable(Deferred<Tool> deferredTool, Runnable callback) {

        //TODO: needs to be decided whether if the callback will be executed immediately or will be spawned
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
