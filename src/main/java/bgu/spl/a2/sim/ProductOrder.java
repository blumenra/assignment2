package bgu.spl.a2.sim;

/**
 * This class holds an order for a type of product from a wave of a certain quantity
 * Its parameters are initialized from the Json file by using Gson and so no constructor is required
 */
public class ProductOrder {

    private String product;
    private int qty;
    private long startId;

    /**
     * the following methods are getters and are self explanatory
     * @return
     */
    public String getProduct() {
        return product;
    }

    public int getQty() {
        return qty;
    }

    public long getStartId() {
        return startId;
    }
}
