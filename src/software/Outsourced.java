package software;
/**
 *  Outsourced Class per UML doc
 *  Extends Part.java, adds company name.
 * @see Part
 * @author Kyle Hogue
 */
public class Outsourced extends Part{
    private String companyName;

    /**
     * Outsourced Class constructor.
     * @param id Part ID
     * @param name  Part Name
     * @param price Part Price
     * @param stock Part current Inventory
     * @param min Part Minimum stock
     * @param max Part Maximum stock
     * @param companyName Part Company Name, Outsourced exclusive.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() { return companyName; }
}
