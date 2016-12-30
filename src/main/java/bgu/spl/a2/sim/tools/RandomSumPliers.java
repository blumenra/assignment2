package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

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

        long ans = 0;

        //TODO: implement randomSum algorithm and use it on p

        return ans;
    }
}
