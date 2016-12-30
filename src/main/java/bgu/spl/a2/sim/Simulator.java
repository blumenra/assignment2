/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.WaveOrder;
import bgu.spl.a2.sim.tools.Tool;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	private static WorkStealingThreadPool pool;
	private static JsonParser jsonParser;
	/**
	* Begin the simulation
	* Should not be called before attachWorkStealingThreadPool()
	*/
    public static ConcurrentLinkedQueue<Product> start(){

		//TODO: put this here the right way
		pool.start();

		Warehouse warehouse = new Warehouse();//TODO: should this even be here?

		List<Tool> tools = jsonParser.getTools();
		Map<Tool, Integer> toolsInventory = jsonParser.getToolsInventory();
		List<ManufactoringPlan> plans = jsonParser.getPlans();

		for(Tool tool : tools){

			warehouse.addTool(tool, toolsInventory.get(tool));
		}

		for(ManufactoringPlan plan : plans){

			warehouse.addPlan(plan);
		}

    	List<List<ProductOrder>> waves = jsonParser.getWaves();

		ConcurrentLinkedQueue<Product> finishedProducts = new ConcurrentLinkedQueue<Product>();

    	for(List<ProductOrder> wave : waves){

    		CountDownLatch latch = new CountDownLatch(wave.size());

    		for(ProductOrder order : wave){

				WaveOrder task = new WaveOrder(order.getProduct(), order.getQty(), order.getStartId(), warehouse);
				pool.submit(task);
				task.getResult().whenResolved(() -> {

					for(Product finishedProduct : task.getResult().get()){

						finishedProducts.add(finishedProduct);
					}
                    latch.countDown();
                });
            }

            try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			pool.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return finishedProducts;
	}
	
	/**
	* attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
	* @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
	*/
	public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool){

		Simulator.pool = myWorkStealingThreadPool;
	}

	private static void initializeJsonParser(JsonParser jsonParser){

		Simulator.jsonParser = jsonParser;
	}
	
	public static void main(String [] args){


		String inputFile = args[0];

		JsonParser myJsonParser = new JsonParser(inputFile);

		initializeJsonParser(myJsonParser);

		WorkStealingThreadPool myPool = new WorkStealingThreadPool(jsonParser.getThreads());

		attachWorkStealingThreadPool(myPool);

		ConcurrentLinkedQueue<Product> finishedProducts = start();

		//TODO: remove {
		for(Product product: finishedProducts){
			System.out.println("");
			product.printWithParts("");
		}
		//TODO: remove }

		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream("result.ser");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(finishedProducts);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}



		//TODO: REMOVE ALL THE FOLLOWING
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("result.ser"));
			System.out.println("***************************");
			Object obj = in.readObject();
			System.out.println(obj);


		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
