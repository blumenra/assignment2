package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.util.Random;

/**
 * This class implements the Tool interface and represents the RandomSumPliers tool.
 * It implements the useOn methods which receives a product and returns a long number.
 */
public class RandomSumPliers implements Tool {

	private final String type;

	public RandomSumPliers() {
		this.type = "rs-pliers";
	}

	public String getType() {

		return type;
	}

	/**
	 * This method sums a (id % 10000) number of random numbers which were generated according to
	 * the finalId seed of the given product and returns it
	 *
	 * @param p - Product to use tool on
	 * @return sum of (id % 10000) number of random numbers which were generated according to
	 * the finalId seed of the given product.
	 */
	public long useOn(Product p) {

		long id = p.getFinalId();

        Random r = new Random(id); // Initializing Random with the finalId as seed
        long  sum = 0;

        for (long i = 0; i < id % 10000; i++) {

            sum += r.nextInt();
        }

        return sum;
	}
}
