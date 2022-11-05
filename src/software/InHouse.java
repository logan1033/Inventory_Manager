package software;
/**
 *  InHouse Class per UML doc
 *  Extends Part.java, adds machine ID.
 * @see Part
 * @author Kyle Hogue
 */


public class InHouse extends Part{
    private int machineID;

    /**
     *  InHouse Class Constructor
     * @param id Part ID
     * @param name Part Name
     * @param price Part Price
     * @param stock Part Price
     * @param min Part Minimum
     * @param max Part Maximum
     * @param machineID Part Machine ID, InHouse exclusive.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * @param machineID the machineID to set
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    /**
     * @return the machineID
     */
    public int getMachineID() { return machineID; }
}
