package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

/**
 * This class implements the Tool interface and represents the NextPrimeHammer tool.
 * It implements the useOn methods which receives a product and returns a long number.
 */
public class NextPrimeHammer implements Tool{

	private final String type;

	public NextPrimeHammer() {
		this.type = "np-hammer";
	}

	public String getType() {

		return type;
	}

	/**
	 * This method finds the next prime number after the finalId of the given product
	 * and returns it.
	 *
	 * @param p - Product to use tool on
	 * @return a number of type long which is the next prime number after the finalId of the given product
	 */
	public long useOn(Product p) {

		long id = p.getFinalId();

		return findNextPrime(id);
	}

	private long findNextPrime(long id) {

	    long v = id;

		if((id % 2) == 0){

            v++;
        }
        else {

            v+=2;
        }

		while (!isPrime(v)) {
			v+=2;
		}

		return v;
	}

	private boolean isPrime(long value) {

	    if(value < 2){

	        return false;
        }

		if(value == 2){

	        return true;
        }

		long sq = (long) Math.sqrt(value);

		for (long i = 2; i <= sq; i++) {

		    if (value % i == 0) {

		        return false;
			}
		}

		return true;
	}
}
