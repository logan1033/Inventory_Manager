package software;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Modify Product form Controller
 * @see Product
 * @author Kyle Hogue
 */
public class Modify_Product_Controller {
    @FXML
    public Label productTitleLabel;
    @FXML
    public TextField productIDField;
    @FXML
    public TextField productNameField;
    @FXML
    public TextField productInvField;
    @FXML
    public TextField productPriceField;
    @FXML
    public TextField productMaxField;
    @FXML
    public TextField productMinField;
    @FXML
    public TableView productPartTableview;
    @FXML
    public TableView assocPartTableview;
    @FXML
    public TextField productSearchField;
    @FXML
    public Button productAddButton;
    @FXML
    public Button removeAssociatedPartButton;
    @FXML
    public Button productSaveButton;
    @FXML
    public Button productCancelButton;
    @FXML
    public TextField modProductSearchField;

    /**
     * Product ID to be modified.
     * @see Product
     */
    private int productID;

    /**
     * Temporary Product to facilitate cancellation.
     * @see Product
     */
    private Product tempProduct = new Product(0, "", 0.0, 0, 0, 0);

    /**
     * Temporary List to facilitate cancellation.
     */
    private ObservableList<Part> assocParts = FXCollections.observableArrayList();

    /**
     *
     * @param product selected Product passed to modifications form. Previous data displayed.
     */
    public void initData(Product product) {
        //fills text fields
        productID = product.getId();
        productIDField.setText(String.valueOf(product.getId()));
        productNameField.setText(product.getName());
        productPriceField.setText(String.valueOf(product.getPrice()));
        productInvField.setText(String.valueOf(product.getStock()));
        productMinField.setText(String.valueOf(product.getMin()));
        productMaxField.setText(String.valueOf(product.getMax()));

        //temp parts list to allow rollback of cancelled mod.
        product.getAssociatedParts().forEach(part -> {
            assocParts.add(part);
        });
        assocPartTableview.setItems(assocParts);

    }

    /**
     * Initializes TableView columns, Search Fields, and Data.
     */
    public void initialize() {
        //Create partTableview columns
        TableColumn<Part, Integer> id = new TableColumn<>("Part ID");
        TableColumn<Part, String> name = new TableColumn<>("Part Name");
        TableColumn<Part, Double> price = new TableColumn<>("Price");
        TableColumn<Part, Integer> stock = new TableColumn<>("Inv");
        TableColumn<Part, Integer> min = new TableColumn<>("Min");
        TableColumn<Part, Integer> max = new TableColumn<>("Max");
        productPartTableview.getColumns().addAll(id, name, price, stock, min, max);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        min.setCellValueFactory(new PropertyValueFactory<>("min"));
        max.setCellValueFactory(new PropertyValueFactory<>("max"));

        //Create assocPartTableview columns
        TableColumn<Part, Integer> pid = new TableColumn<>("Part ID");
        TableColumn<Part, String> pname = new TableColumn<>("Part Name");
        TableColumn<Part, Double> pprice = new TableColumn<>("Price");
        TableColumn<Part, Integer> pstock = new TableColumn<>("Inv");
        TableColumn<Part, Integer> pmin = new TableColumn<>("Min");
        TableColumn<Part, Integer> pmax = new TableColumn<>("Max");
        assocPartTableview.getColumns().addAll(pid, pname, pprice, pstock, pmin, pmax);

        pid.setCellValueFactory(new PropertyValueFactory<>("id"));
        pname.setCellValueFactory(new PropertyValueFactory<>("name"));
        pprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        pstock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        pmin.setCellValueFactory(new PropertyValueFactory<>("min"));
        pmax.setCellValueFactory(new PropertyValueFactory<>("max"));

        productPartTableview.getSortOrder().add(id);
        assocPartTableview.getSortOrder().add(pid);

        //Populate Tableview
        productPartTableview.setItems(Inventory.getAllParts());
        assocPartTableview.setItems(assocParts);


        //Search field for productPartTableView
        FilteredList<Part> filteredPartList = new FilteredList<>(Inventory.getAllParts(), b -> true);

        modProductSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredPartList.setPredicate(part -> {
                //empty search field
                if (newValue == null || newValue.isEmpty()) { return true; }

                String lowerCaseFilter = newValue.toLowerCase();

                //name field search
                if (part.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                    //id field search
                } else if (String.valueOf(part.getId()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else return false;
            });
        });

        SortedList<Part> sortedPartList = new SortedList<>(filteredPartList);

        sortedPartList.comparatorProperty().bind(productPartTableview.comparatorProperty());

        productPartTableview.setItems(sortedPartList);
    }

    /**
     * Closes form and returns to main form.
     */
    public void handleProductCancelButton() {
        assocParts.clear();
        Stage stage = (Stage) productCancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Saves Modifications to Product Inventory
     *
     *  RUNTIME ERROR COMMENT!
     *  The below code copies the temporary list to the product specific one.
     *  The original problem had associated parts added during modifications still being saved even after modification was cancelled.
     *  A temporary associated parts list was added to allow display of intended modifications before making any changes final.
     *  This assocParts List acts as a buffer, copied via the forEach loop below when saved but discarded (see handleProductCancelButton) if modifications is cancelled.
     *
     */
    public void handleProductSaveButton() {
        try {
            tempProduct.setId(productID);
            tempProduct.setName(productNameField.getText());
            tempProduct.setPrice(Double.parseDouble(productPriceField.getText()));
            tempProduct.setStock(Integer.parseInt(productInvField.getText()));

            // Checks for valid inputs. Only two included from rubric, additional easily added.
            boolean validEntries = false;
            while (validEntries == false) {
                if ((Integer.parseInt(productMinField.getText())) > (Integer.parseInt(productMaxField.getText()))) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Min must be less than Max!");
                    productMinField.clear();
                    productMaxField.clear();
                    alert.showAndWait();
                    break;
                } else if ((Integer.parseInt(productInvField.getText())) > (Integer.parseInt(productMaxField.getText())) || (Integer.parseInt(productInvField.getText())) < (Integer.parseInt(productMinField.getText()))) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Current Inventory must be more than Min and less than Max!");
                    productInvField.clear();
                    alert.showAndWait();
                    break;
                } else {
                    validEntries = true;
                }
            }
            tempProduct.setMin(Integer.parseInt(productMinField.getText()));
            tempProduct.setMax(Integer.parseInt(productMaxField.getText()));
            
            /**
             *   RUNTIME ERROR!  The below code copies the temporary list to the product specific one.
             *   The original problem had associated parts added during modifications still being saved even after modification was cancelled.
             *   A temporary associated parts list was added to allow display of intended modifications before making any changes final.
             *   This assocParts List acts as a buffer, copied via the forEach loop below when saved but discarded (see handleProductCancelButton) if modifications is cancelled.
             *
             */

            if (validEntries) {
                assocParts.forEach(part -> {
                    tempProduct.addAssociatedPart(part);
                });
                Inventory.getAllProducts().set(productID - 1, tempProduct);
                Stage stage = (Stage) productCancelButton.getScene().getWindow();
                stage.close();
            }
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Data Must Be Of Correct Type! \n\nAll Boxes Must Be Filled!");
            Stage stage = (Stage) productCancelButton.getScene().getWindow();
            stage.close();

            alert.showAndWait();
        }
    }

    /**
     *  Deletes Selected Product from Inventory.
     */
    public void handleRemoveAssociatedPartButton() {
        int selectedIndex = assocPartTableview.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                assocParts.remove(assocPartTableview.getItems().get(selectedIndex));
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!!");
                alert.setHeaderText(null);
                alert.setContentText("Something Went Wrong! Try Again.");

                alert.showAndWait();

            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Must Select an Item to Delete.");

            alert.showAndWait();
        }
    }

    /**
     * Adds Selected Part to Product Associated Part List
     */
    public void handleProductAddButton() {
        int selectedIndex = productPartTableview.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                assocParts.add((Part) productPartTableview.getItems().get(selectedIndex));
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!!");
                alert.setHeaderText(null);
                alert.setContentText("Something Went Wrong! Try Again.");

                alert.showAndWait();

            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Must Select an Item.");

            alert.showAndWait();
        }
    }
}
