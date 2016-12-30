package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

/**
 * Created by blumenra on 12/29/16.
 */
public class NextPrimeHammer implements Tool{

    private final String type;

    public NextPrimeHammer() {
        this.type = "NextPrimeHammer";
    }

    public String getType() {

        return type;
    }

    public long useOn(Product p) {

        long ans = 0;

        //TODO: implement nextPrime algorithm and use it on p

        return ans;
    }
}
