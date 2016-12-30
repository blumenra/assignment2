package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

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

        long ans = 0;

        //TODO: implement gcd algorithm add use it on p

        return ans;
    }
}
