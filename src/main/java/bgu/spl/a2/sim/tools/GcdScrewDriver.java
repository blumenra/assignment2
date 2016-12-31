package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

/**
 * This class implements the Tool interface and represents the GcdScrewDriver tool.
 * It implements the useOn methods which receives a product and returns a long number.
 */
public class GcdScrewDriver implements Tool {

	private final String type;

	public GcdScrewDriver() {

		this.type = "gs-driver";
	}

	public String getType() {

		return type;
	}

	/**
	 * This method finds the GCD of the product's finalId
	 * and the reversed finalId and returns it.
	 *
	 * @param p - Product to use tool on
	 * @return a number of type long which is the GCD of the product's finalId
	 * and the reversed finalId
	 */
	public long useOn(Product p) {

        BigInteger b1 = BigInteger.valueOf(p.getFinalId());
        BigInteger b2 = BigInteger.valueOf(reverse(p.getFinalId()));
        long ans = (b1.gcd(b2)).longValue();

		return ans;
	}

	/**
	 * This method reverses the n and returns the result
	 * @param n
	 * @return the reversed representation of n
	 */
	private long reverse(long n){

	    long reverse = 0;

	    while( n != 0 ){

			reverse = reverse * 10;
			reverse = reverse + (n % 10);
			n = n/10;
	    }

		return reverse;
	}
}
