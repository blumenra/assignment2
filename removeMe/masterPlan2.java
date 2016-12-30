/*TODO:
		--implement JSON
		--implement the Simulator
		--create tool classes
		--create output file
		check everything in Terminal with Maven
		handle all the TODOs in the code
		remove all souts (has a keyboard shortcut in the tips)
		remove unused imports, variables any anything else unused in the code (has a keyboard shortcut in the tips)
		--Fixe the part id's, they dont match the output file and we dont know why...
		--implement the pool method od simulator
		--create classes for all our tasks

*/






import bgu.spl.a2.sim.*;
import bgu.spl.a2;

public class Simulator {

	Fields:
		private WorkStealingThreadPool pool;
		private ConcurrentLinkedQueue<Product> finishedProducts;
		private ArrayList<Wave<>> waves;

	Methods:
		public static ConcurrentLinkedQueue<Product> start(){

		}

		public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool){

		}

		public static int main(String [] args){

		}
}

//************Tasks
public class Wave extends Task<Queue<Product>> {

	Fields:
		private final String product;
		private final int qty;
		private final long startId;
		private final Warehouse warehouse;

	Methods:
		public Wave(String product, int qty, long startId) {
			this.product = product;
			this.qty = qty;
			this.startId = startId;
	}

		@Override
		protected void start() {

			List<Task<Product>> tasks = new ArrayList<>();

			for(int i = 0; i < qty; i++) {

				Manufacture task = new Manufacture(product, startId + i, warehouse);
				tasks.add(task);
				spawn(task);
			}

			whenResolved(tasks, () -> {

				Queue<Product> finishedProducts = new Queue<Product>();

				for(Task<Product> task : tasks) {

					finishedProducts.add(task.getResult().get());
				}

				complete(finishedProducts);
			});
		}

}

public class Manufacture extends Task<Product> {

	Fields:
		private final String productName;
		private final int startId;
		private final Warehouse warehouse;

	Methods:
		public Manufacture(String product, int startId, Warehouse warehouse) {

			this.productName = product;
			this.startId = startId;
			this.warehouse = warehouse;
		}

		@Override
		protected void start() {

			ManufactoringPlan plan = warehouse.getPlan(productName);
			List<Task<Product>> tasks = new ArrayList<>();

			for(String part : plan.getParts()){

				Manufacture task = new Manufacture(part, startId + 1, warehouse);
				tasks.add(task);
				spawn(task);
			}

			whenResolved(tasks, () -> {

				List<Product> finishedParts = new ArrayList<>();
				List<Task<long>> toolTasks = new ArrayList<>();

				for(Task<Product> task : tasks) {

					finishedParts.add(task.getResult().get());
				}

				for(String tool : plan.getTools()){

					UseTool task = new UseTool(tool, finishedParts, warehouse);
					toolTasks.add(task);
					spawn(task);

				}

				whenResolved(toolTasks, () -> {

					long sum = this.startId;

					for(Task<Product> toolTask : toolTasks) {

						sum += toolTask.getResult().get();
					}

					Product product = new Product(this.startId, this.productName);
					product.setFinalId(sum);
					complete(product);
				});

			});

		}

}

public class UseTool extends Task<long> {

	Fields:
		private Tool tool;
		private final List<Product> parts;
		private final Warehouse warehouse;
		private Deferred<Tool> deferredTool;

	Methods:
		public UseTool(Tool tool, List<Product> parts, Warehouse warehouse) {

			this.tool = tool;
			this.parts = parts;
			this.warehouse = warehouse;
		}

		@Override
		protected void start() {

			this.deferredTool = warehouse.acquireTool(tool);

			whenToolIsAvaiable(this.deferredTool, () -> {

				long sum;

				Tool tool = deferredTool.get();

				for(Product part : parts) {

					sum += tool.useOn(part);
				}

				warehouse.releaseTool(tool);

				complete(sum);
			});

		}

		private whenToolIsAvaiable(Deferred<Tool> deferredTool, Runnable callback) {

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
//************Tasks

public class ManufactoringPlan {

	Fields:
		private final String product;
		private final String[] parts;
		private final String[] tools;

	Methods:
		public ManufactoringPlan(String product, String[] parts, String[] tools) {

			this.product = product;
			this.parts = parts;
			this.tools = tools;
		}

		public String[] getParts() {

			return parts;
		}

		public String getProductName() {

			return product;
		}

		public String[] getTools() {

			return tools;
		}
}


