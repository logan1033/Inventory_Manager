package software;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Add Product form Controller
 * @see Product
 * @author Kyle Hogue
 */
public class Add_Product_Controller {
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

    /**
     * Generic Product to populate and facilitate clean cancellation.
     * @see Product
     */
    private Product product = new Product(0, "", 0.0, 0, 0, 0);

    /**
     * Initializes TableView columns, Search Fields, and Populates TableViews with appropriate data..
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
        assocPartTableview.setItems(product.getAssociatedParts());


        //Search field for productPartTableView
        FilteredList<Part> filteredPartList = new FilteredList<>(Inventory.getAllParts(), b -> true);

        productSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
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
        Stage stage = (Stage) productCancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Saves New Product to Inventory.
     * @see Product
     * @see Inventory
     */
    public void handleProductSaveButton() {
        try {
            int enteredProductID = Inventory.getAllProducts().size() + 1;
            String enteredProductName = productNameField.getText();
            double enteredPrice = Double.parseDouble(productPriceField.getText());
            int enteredStock = Integer.parseInt(productInvField.getText());
            int enteredMin = Integer.parseInt(productMinField.getText());
            int enteredMax = Integer.parseInt(productMaxField.getText());

            if (enteredMin > enteredMax) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Min must be less than Max!");
                productMinField.clear();
                productMaxField.clear();
                alert.showAndWait();
            } else if (enteredStock > enteredMax || enteredStock < enteredMin) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Current Inventory must be more than Min and less than Max!");
                productInvField.clear();
                alert.showAndWait();
            }  else {
                product.setId(enteredProductID);
                product.setName(enteredProductName);
                product.setPrice(enteredPrice);
                product.setStock(enteredStock);
                product.setMin(enteredMin);
                product.setMax(enteredMax);
                Inventory.addProduct(product);
                Stage stage = (Stage) productCancelButton.getScene().getWindow();
                stage.close();
            }
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Data Must Be Of Correct Type! \n\nAll Boxes Must Be Filled!");
            productNameField.clear();
            productInvField.clear();
            productPriceField.clear();
            productMinField.clear();
            productMaxField.clear();

            alert.showAndWait();
        }
    }

    /**
     * Removes Selected Part from Association with Product.
     */
    public void handleRemoveAssociatedPartButton() {
        int selectedIndex = assocPartTableview.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                 product.deleteAssociatedPart((Part) assocPartTableview.getItems().get(selectedIndex));
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
     * Adds Selected Part to Product Associated Part List.
     */
    public void handleProductAddButton() {
        int selectedIndex = productPartTableview.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                product.addAssociatedPart((Part) productPartTableview.getItems().get(selectedIndex));
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
