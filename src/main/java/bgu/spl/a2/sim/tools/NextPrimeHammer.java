package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

/**
 * Created by blumenra on 12/29/16.
 */
public class NextPrimeHammer implements Tool{

	private final String type;

	public NextPrimeHammer() {
		this.type = "np-hammer";
	}

	public String getType() {

		return type;
	}

	public long useOn(Product p) {

		long id = p.getFinalId();

		return findNextPrime(id);
	}

	public long findNextPrime(long id) {

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
