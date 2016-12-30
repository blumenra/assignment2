package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

/**
 * Created by blumenra on 12/29/16.
 */
public class GcdScrewDriver implements Tool {

	private final String type;

	public GcdScrewDriver() {

		this.type = "gs-driver";
	}

	public String getType() {

		return type;
	}

	public long useOn(Product p) {

        BigInteger b1 = BigInteger.valueOf(p.getFinalId());
        BigInteger b2 = BigInteger.valueOf(reverse(p.getFinalId()));
        long ans = (b1.gcd(b2)).longValue();

		return ans;
	}

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
