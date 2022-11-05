package software;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *  Static Inventory Class per UML doc
 *
 *  Observable Array List of type Part.java
 *  Observable Array List of type Product.java
 * @see Part
 * @see Product
 * @author Kyle Hogue
 */
public class Inventory{
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param newPart to add
     */
    public static void addPart(Part newPart) { allParts.add(newPart); }

    /**
     *
     * @param newProduct to add
     */
    public static void addProduct(Product newProduct) { allProducts.add(newProduct); }

    /**
     *
     * @param partID to find
     * @return Part
     */
    public static Part lookupPart(int partID) { return allParts.get(partID); }

    /**
     *
     * @param productID to find
     * @return Product
     */
    public static Product lookupProduct(int productID) { return allProducts.get(productID); }

    /**
     *
     * @param partName to lookup
     * @return Part
     */
    public static ObservableList<Part> lookupPart(String partName) { return allParts; }

    /**
     *
     * @param productName to lookup
     * @return Product
     */
    public static ObservableList<Product> lookupProduct(String productName) { return allProducts; }

    /**
     *
     * @param index of Part to update
     * @param selectedPart Part to update
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.remove(index-1);
        allParts.add(index-1, selectedPart);
    }

    /**
     *
     * @param index of Part to update
     * @param newProduct Product to update
     */
    public static void updateProduct(int index, Product newProduct) {
       allProducts.remove(index);
       allProducts.add(index, newProduct);
    }

    /**
     *
     * @param selectedPart Part to delete
     * @return delete successful
     */
    public static boolean deletePart(Part selectedPart) { return allParts.remove(selectedPart); }

    /**
     *
     * @param selectedProduct Product to delete
     * @return delete successful
     */
    public static boolean deleteProduct(Product selectedProduct) { return allProducts.remove(selectedProduct); }

    /**
     *
     * @return Observable List
     * @see Part
     */
    public static ObservableList<Part> getAllParts() { return allParts; }

    /**
     *
     * @return Observable List
     * @see Product
     */
    public static ObservableList<Product> getAllProducts() { return allProducts; }
}
