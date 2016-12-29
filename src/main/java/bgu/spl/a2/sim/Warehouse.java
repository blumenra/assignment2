package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.Deferred;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class representing the warehouse in your simulation
 * 
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 */
public class Warehouse {

	Map<String, AtomicInteger> inventory;
	Map<String, ConcurrentLinkedQueue<Deferred<Tool>>> toolWaitingLists;
	Map<String, Tool> tools;
	Map<String, ManufactoringPlan> plans;

	/**
	* Constructor
	*/
	public Warehouse() {

		inventory = new HashMap<>();
		toolWaitingLists = new HashMap<>();
		tools = new HashMap<>();
		plans = new HashMap<>();
	}

	/**
	* Tool acquisition procedure
	* Note that this procedure is non-blocking and should return immediatly
	* @param type - string describing the required tool
	* @return a deferred promise for the  requested tool
	*/
	public Deferred<Tool> acquireTool(String type) {

		Deferred<Tool> deferred = new Deferred<>();

		synchronized (inventory.get(type)) {

			if(inventory.get(type).get() > 0) {

				deferred.resolve(tools.get(type));
			}
			else {

				toolWaitingLists.get(type).add(deferred);
			}
		}

		return deferred;
	}

	/**
	* Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
	* @param tool - The tool to be returned
	*/
	public void releaseTool(Tool tool) {

		if(toolWaitingLists.get(tool.getType()).isEmpty()) {

			inventory.get(tool.getType()).incrementAndGet();
		}
		else {

			toolWaitingLists.get(tool.getType()).poll().resolve(tool);
		}
	}

	
	/**
	* Getter for ManufactoringPlans
	* @param product - a string with the product name for which a ManufactoringPlan is desired
	* @return A ManufactoringPlan for product
	*/
	public ManufactoringPlan getPlan(String product) {

		return plans.get(product);
	}
	
	/**
	* Store a ManufactoringPlan in the warehouse for later retrieval
	* @param plan - a ManufactoringPlan to be stored
	*/
	public void addPlan(ManufactoringPlan plan) {

		plans.put(plan.getProductName(), plan);
	}

	/**
	* Store a qty Amount of tools of type tool in the warehouse for later retrieval
	* @param tool - type of tool to be stored
	* @param qty - amount of tools of type tool to be stored
	*/
	public void addTool(Tool tool, int qty) {

		tools.put(tool.getType(), tool);

		AtomicInteger qtyAI = new AtomicInteger(qty);
		inventory.put(tool.getType(), qtyAI);

		ConcurrentLinkedQueue<Deferred<Tool>> waitingList = new ConcurrentLinkedQueue<>();
		toolWaitingLists.put(tool.getType(), waitingList);
	}

}
