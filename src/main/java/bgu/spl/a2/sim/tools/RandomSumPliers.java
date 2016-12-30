package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.util.Random;

/**
 * Created by blumenra on 12/29/16.
 */
public class RandomSumPliers implements Tool {

	private final String type;

	public RandomSumPliers() {
		this.type = "rs-pliers";
	}

	public String getType() {

		return type;
	}

	public long useOn(Product p) {

		long id = p.getFinalId();

        Random r = new Random(id);
        long  sum = 0;

        for (long i = 0; i < id % 10000; i++) {

            sum += r.nextInt();
        }

        return sum;
	}
}
