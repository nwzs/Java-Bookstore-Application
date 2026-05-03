package phasefinal;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.IOException;
import java.util.ArrayList;


public class Main extends Application {

    private final Owner owner = new Owner();
    private Customer currentCustomer;
    private static final FileHandler files = new FileHandler();

    Button loginButton = new Button("Login");
    Button booksButton = new Button("Books 📚");
    Button customersButton = new Button("Customers");
    Button logoutButton = new Button("Logout");
    Button backButton = new Button("🔙");
    Button buyButton = new Button("Buy");
    Button pointsBuyButton = new Button("Redeem points & Buy");
    TextField userTextField = new TextField();
    PasswordField passTextField = new PasswordField();
    HBox hb = new HBox();

    TableView<Book> booksTable = new TableView<>();
    final TableView.TableViewFocusModel<Book> defaultFocusModel = booksTable.getFocusModel();
    ObservableList<Book> books = FXCollections.observableArrayList();

    public ObservableList<Book> addBooks() {
        books.setAll(Owner.books);
        return books;
    }

    TableView<Customer> customersTable = new TableView<>();
    ObservableList<Customer> customers = FXCollections.observableArrayList();

    public ObservableList<Customer> addCustomers() {
        customers.setAll(owner.getCustomers());
        return customers;
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Bookstore App");
        primaryStage.getIcons().add(new Image("file:src/bookPic.png"));
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(loginScreen(false), 605, 550));
        primaryStage.show();
        System.out.println("Opened bookstore application");

        try {
            owner.restockArrays();
            System.out.println("Arrays restocked from files");
        } catch (IOException e) {
            System.out.println("File Importing Error");
        }

        loginButton.setOnAction(e -> {
            boolean logged_in = false;
            if (userTextField.getText().equals(owner.getUsername()) && passTextField.getText().equals(owner.getPassword())) {
                primaryStage.setScene(new Scene(ownerStartScreen(), 605, 550));
                logged_in = true;
            }
            for (Customer c : owner.getCustomers()) {
                if (userTextField.getText().equals(c.getUsername()) && passTextField.getText().equals(c.getPassword())) {
                    currentCustomer = c;
                    primaryStage.setScene(new Scene(customerHomeScreen(0), 605, 550));
                    logged_in = true;
                }
            }
            if (!logged_in) {
                primaryStage.setScene(new Scene(loginScreen(true), 605, 550));
            }
        });

        logoutButton.setOnAction(e -> {
            primaryStage.setScene(new Scene(loginScreen(false), 605, 550));
            for (Book b : Owner.books) {
                b.setSelect(new CheckBox());
            }
            userTextField.clear();
            passTextField.clear();
        });

        booksButton.setOnAction(e -> primaryStage.setScene(new Scene(booksTableScreen(), 605, 550)));

        customersButton.setOnAction(e -> primaryStage.setScene(new Scene(customerTableScreen(), 605, 550)));
        backButton.setOnAction(e -> primaryStage.setScene(new Scene(ownerStartScreen(), 605, 550)));

        pointsBuyButton.setOnAction(e -> {
            boolean bookSelected = Owner.books.stream().anyMatch(b -> b.getSelect().isSelected());
            if (!bookSelected) {
                primaryStage.setScene(new Scene(customerHomeScreen(1), 605, 550));
            } else if (currentCustomer.getPoints() == 0) {
                primaryStage.setScene(new Scene(customerHomeScreen(2), 605, 550));
            } else {
                primaryStage.setScene(new Scene(checkoutScreen(true), 605, 550));
            }
        });

        buyButton.setOnAction(e -> {
            boolean bookSelected = Owner.books.stream().anyMatch(b -> b.getSelect().isSelected());
            if (bookSelected) {
                primaryStage.setScene(new Scene(checkoutScreen(false), 605, 550));
            } else {
                primaryStage.setScene(new Scene(customerHomeScreen(1), 605, 550));
            }
        });

        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Exited the book store");
            try {
                FileHandler.writeBook(new ArrayList<>(Owner.books));
                FileHandler.writeCustomer(new ArrayList<>(owner.getCustomers()));
                System.out.println("Files updated with current array data");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });



       
       {
           buyButton.setStyle("-fx-background-color: #455947; -fx-text-fill: white;");
           pointsBuyButton.setStyle("-fx-background-color: #455947; -fx-text-fill: white;");
           customersButton.setStyle("-fx-background-color: #455947;" + "-fx-font-size:25;" + "-fx-background-radius: 10; -fx-text-fill: white;");
           booksButton.setStyle("-fx-background-color: #455947;" + "-fx-font-size:25;" + "-fx-background-radius: 10; -fx-text-fill: white;");
           logoutButton.setStyle("-fx-background-color: #455947; -fx-text-fill: white;");
           backButton.setStyle("-fx-background-color: #455947;" + "-fx-font-size:14; -fx-text-fill: white;");
           loginButton.setStyle("-fx-background-color: #455947; -fx-text-fill: white;");


           customersTable.setStyle("-fx-control-inner-background: #c9d9c3;" +
                   "-fx-selection-bar: #a2bf97; -fx-selection-bar-non-focused: #a2bf97;" + "-fx-border-color: #c9d9c3;" +
                   "-fx-table-cell-border-color: #c9d9c3;" + "-fx-background-color: #c9d9c3;");


           booksTable.setStyle("-fx-control-inner-background: #c9d9c3;" + "-fx-border-color: #c9d9c3;" +
                   "-fx-selection-bar: #a2bf97; -fx-selection-bar-non-focused: #a2bf97;" +
                   "-fx-table-cell-border-color: #c9d9c3;" + "-fx-background-color: #c9d9c3;" + "-fx-column-header-background: #c9d9c3;");
       }
   }


   public Group loginScreen(boolean loginError){
       Group lis = new Group();


       HBox header = new HBox();
       Label brand = new Label("Bookstore 📚");
       brand.setFont(new Font("Arial", 35));
       header.getChildren().addAll(brand);
       header.setSpacing(15);
       header.setAlignment(Pos.CENTER);


       VBox loginBox = new VBox();
       loginBox.setPadding(new Insets(30,65,45,65));
       loginBox.setStyle("-fx-background-color: #94b598;" + "-fx-background-radius: 10 10 10 10;");
       loginBox.setSpacing(6);
       Text user = new Text("Username");
       userTextField.setStyle("-fx-background-color: #c9d9c3;");
       passTextField.setStyle("-fx-background-color: #c9d9c3;");
       Text pass = new Text("Password");
       loginButton.setMinWidth(174);
       loginBox.getChildren().addAll(user, userTextField, pass, passTextField, loginButton);


       if(loginError){
           Text errorMsg = new Text("Incorrect username or password.");
           errorMsg.setFill(Color.RED);
           loginBox.getChildren().add(errorMsg);
       }


       VBox bg = new VBox();
       bg.getChildren().addAll(header, loginBox);
       bg.setStyle("-fx-background-color: #dfe8e0;");
       bg.setPadding(new Insets(80, 280, 200, 150));
       bg.setSpacing(80);


       lis.getChildren().addAll(bg);
       return lis;
   }


   public Group customerHomeScreen(int type){
       Group bookstore = new Group();
       booksTable.getItems().clear();
       booksTable.getColumns().clear();
       booksTable.setFocusModel(null);


       Font font = new Font(14);
       Text welcomeMsg = new Text("Welcome, " + currentCustomer.getUsername() + ".");
       welcomeMsg.setFont(font);
       Text status1 = new Text(" Status: ");
       status1.setFont(font);
       Text status2 = new Text(currentCustomer.getStatus());
       status2.setFont(font);
       status2.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 14px;" +
               "-fx-stroke: black;" + "-fx-stroke-width: 0.5px;");


       if(currentCustomer.getStatus().equals("Gold")){
           status2.setFill(Color.GOLD);
       }else status2.setFill(Color.SILVER);


       Text points = new Text(" Points: " + currentCustomer.getPoints());
       points.setFont(font);


       
       TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
       titleColumn.setMinWidth(200);
       titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));


       
       TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
       priceColumn.setMinWidth(100);
       priceColumn.setStyle("-fx-alignment: CENTER;");
       priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


       
       TableColumn<Book, String> selectColumn = new TableColumn<>("Select");
       selectColumn.setMinWidth(100);
       selectColumn.setStyle("-fx-alignment: CENTER;");
       selectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));


       booksTable.setItems(addBooks());
       booksTable.getColumns().addAll(titleColumn, priceColumn, selectColumn);


       HBox info = new HBox();
       info.getChildren().addAll(status1, status2, points);
       BorderPane header = new BorderPane();
       header.setLeft(welcomeMsg);
       header.setRight(info);


       HBox bottom = new HBox();
       bottom.setAlignment(Pos.BOTTOM_CENTER);
       bottom.setSpacing(5);
       bottom.getChildren().addAll(buyButton, pointsBuyButton, logoutButton);


       VBox vbox = new VBox();
       String errMsg = "";
       if(type == 1){
           errMsg = "Please select a book before proceeding.";
       }
       else if(type == 2){
           errMsg =  "No points available to redeem.";
       }
       Text warning = new Text(errMsg);
       warning.setFill(Color.RED);
       vbox.setStyle("-fx-background-color: #dfe8e0;");
       vbox.setSpacing(10);
       vbox.setAlignment(Pos.CENTER);
       vbox.setPadding(new Insets(40, 200, 30, 100));
       vbox.getChildren().addAll(header, booksTable, bottom, warning);


       bookstore.getChildren().addAll(vbox);


       return bookstore;
   }


   public Group checkoutScreen(boolean usedPoints){
       Group cos = new Group();
       double subtotal = 0, discount, total;
       int i = 0, bookCount = 0;
       String[][] booksBought = new String[25][2];
       ArrayList<Book> selectedBooksList = new ArrayList<>();

       for(Book b: Owner.books){
           if(b.getSelect().isSelected()){
               subtotal += b.getPrice();
               booksBought[i][0] = b.getTitle();
               booksBought[i][1] = String.valueOf(b.getPrice());
               selectedBooksList.add(b);
               i++;
           }
       }

       
       currentCustomer.buyBooks(selectedBooksList, usedPoints);

       
       discount = usedPoints
               ? Math.min(currentCustomer.getPoints() / 100.0 + (subtotal - currentCustomer.getPoints() / 100.0), subtotal)
               : 0;
       total = subtotal - discount;

       HBox header = new HBox();
       header.setAlignment(Pos.CENTER);
       header.setSpacing(15);
       header.setPadding(new Insets(0,0,25,0));
       Label brandName = new Label("Bookstore 📚");
       brandName.setFont(new Font("Arial", 35));
       header.getChildren().addAll(brandName);

       VBox receipt = new VBox();
       receipt.setSpacing(7);
       Text receiptTxt = new Text("Receipt");
       receiptTxt.setFont(Font.font(null, FontWeight.BOLD, 12));
       Line thickLine = new Line(0, 150, 400, 150);
       thickLine.setStrokeWidth(3);
       receipt.getChildren().addAll(receiptTxt, thickLine);

       VBox receiptItems = new VBox();
       receiptItems.setStyle("-fx-background-color: #c9d9c3;");
       receiptItems.setSpacing(7);
       for (i = 0; i<25; i++) {
           if(booksBought[i][0] != null){
               Text bookTitle = new Text(booksBought[i][0]);
               Text bookPrice = new Text(booksBought[i][1]);
               BorderPane item = new BorderPane();
               item.setLeft(bookTitle);
               item.setRight(bookPrice);
               Line thinLine = new Line(0, 150, 400, 150);
               receiptItems.getChildren().addAll(item, thinLine);
               bookCount++;
           }
       }

       ScrollPane scrollReceipt = new ScrollPane(receiptItems);
       scrollReceipt.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
       scrollReceipt.setStyle("-fx-background-color:transparent;");
       scrollReceipt.setFitToWidth(true);
       if(bookCount<=4){
           scrollReceipt.setFitToHeight(true);
       } else scrollReceipt.setPrefHeight(130);

       Text subtotalTxt = new Text("Subtotal: $" + (Math.round(subtotal*100.0))/100.0);
       Text pointsDisc = new Text("Points Discount: $" + (Math.round(discount*100.0))/100.0);
       Text totalTxt = new Text("Total: $" + (Math.round(total*100.0))/100.0);
       totalTxt.setFont(new Font("Arial", 15));
       Line thickLine2 = new Line(0, 150, 400, 150);
       thickLine2.setStrokeWidth(3);
       receipt.getChildren().addAll(scrollReceipt, subtotalTxt, pointsDisc, totalTxt, thickLine2);

       VBox bottom = new VBox();
       bottom.setSpacing(40);
       bottom.setAlignment(Pos.CENTER);
       Text info = new Text("You now have " + currentCustomer.getPoints() + " points " +
               "& your current status is " + currentCustomer.getStatus() + "\n\t\t\tThank you for your purchase!");
       bottom.getChildren().addAll(info, logoutButton);

       VBox screen = new VBox();
       screen.setStyle("-fx-background-color: #dfe8e0;");
       screen.setPadding(new Insets(60,105,500,100));
       screen.setAlignment(Pos.CENTER);
       screen.setSpacing(10);
       screen.getChildren().addAll(header, receipt, bottom);

       cos.getChildren().addAll(screen);
       Owner.books.removeIf(b -> b.getSelect().isSelected());
       return cos;
   }



   public VBox ownerStartScreen() {
       VBox osc = new VBox();
       osc.setStyle("-fx-background-color: #dfe8e0;");
       osc.setAlignment(Pos.CENTER);
       osc.setSpacing(100);
       osc.setPadding(new Insets(80,0,30,0));


       HBox buttons = new HBox();
       buttons.setAlignment(Pos.CENTER);
       buttons.setSpacing(40);
       Line vLine = new Line(150, 0, 150, 200);
       buttons.getChildren().addAll(booksButton, vLine, customersButton);
       booksButton.setPrefSize(200,150);
       customersButton.setPrefSize(200,150);


       osc.getChildren().addAll(buttons, logoutButton);
       return osc;
   }


   public Group booksTableScreen() {
       Group bt = new Group();
       hb.getChildren().clear();
       booksTable.getItems().clear();
       booksTable.getColumns().clear();
       booksTable.setFocusModel(defaultFocusModel);


       Label label = new Label("Books");
       label.setFont(new Font("Arial", 20));


       
       TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
       titleColumn.setMinWidth(200);
       titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));


       
       TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
       priceColumn.setMinWidth(100);
       priceColumn.setStyle("-fx-alignment: CENTER;");
       priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


       booksTable.setItems(addBooks());
       booksTable.getColumns().addAll(titleColumn, priceColumn);


       final TextField addBookTittle = new TextField();
       addBookTittle.setPromptText("Title");
       addBookTittle.setMaxWidth(titleColumn.getPrefWidth());
       final TextField addBookPrice = new TextField();
       addBookPrice.setMaxWidth(priceColumn.getPrefWidth());
       addBookPrice.setPromptText("Price");
       addBookTittle.setStyle("-fx-background-color: #c9d9c3;");
       addBookPrice.setStyle("-fx-background-color: #c9d9c3;");


       VBox core = new VBox();
       final Button addButton = new Button("Add");
       addButton.setStyle("-fx-background-color: #455947; -fx-text-fill: white;");
       Label bookAddErr = new Label("Invalid Input");
       bookAddErr.setTextFill(Color.color(1,0,0));


       addButton.setOnAction(e -> {
           try {
               double price = Math.round((Double.parseDouble(addBookPrice.getText()))*100);
               Owner.books.add(new Book(addBookTittle.getText(), price/100));
               
               booksTable.getItems().clear(); 
               booksTable.setItems(addBooks());
               addBookTittle.clear(); 
               addBookPrice.clear();
               core.getChildren().remove(bookAddErr); 
           }
           catch (Exception exception){
               if(!core.getChildren().contains(bookAddErr)){
                   core.getChildren().add(bookAddErr);
               }
           }
       });


       final Button deleteButton = new Button("Delete");
       deleteButton.setStyle("-fx-background-color: #455947; -fx-text-fill: white;");
       deleteButton.setOnAction(e -> {
           Book selectedItem = booksTable.getSelectionModel().getSelectedItem(); 
           booksTable.getItems().remove(selectedItem); 
           Owner.books.remove(selectedItem); 
       });




       hb.getChildren().addAll(addBookTittle, addBookPrice, addButton, deleteButton);
       hb.setSpacing(3);
       hb.setAlignment(Pos.CENTER);


       HBox back = new HBox();
       back.setPadding(new Insets(5));
       back.getChildren().addAll(backButton);


       core.setAlignment(Pos.CENTER);
       core.setSpacing(5);
       core.setPadding(new Insets(0, 0, 0, 150));
       core.getChildren().addAll(label, booksTable, hb);


       VBox vbox = new VBox();
       vbox.setStyle("-fx-background-color: #dfe8e0;");
       vbox.setPadding(new Insets(0, 200, 60, 0));
       vbox.setAlignment(Pos.CENTER);
       vbox.getChildren().addAll(back, core);




       bt.getChildren().addAll(vbox);


       return bt;
   }


   public Group customerTableScreen() {
       Group ct = new Group();
       hb.getChildren().clear();
       customersTable.getItems().clear();
       customersTable.getColumns().clear();


       Label label = new Label("Customers");
       label.setFont(new Font("Arial", 20));


       
       TableColumn<Customer, String> usernameCol = new TableColumn<>("Username");
       usernameCol.setMinWidth(140);
       usernameCol.setStyle("-fx-alignment: CENTER;");
       usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));


       
       TableColumn<Customer, String> passwordCol = new TableColumn<>("Password");
       passwordCol.setMinWidth(140);
       passwordCol.setStyle("-fx-alignment: CENTER;");
       passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));


       
       TableColumn<Customer, Integer> pointsCol = new TableColumn<>("Points");
       pointsCol.setMinWidth(100);
       pointsCol.setStyle("-fx-alignment: CENTER;");
       pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));


       customersTable.setItems(addCustomers());
       customersTable.getColumns().addAll(usernameCol, passwordCol, pointsCol);


       final TextField addUsername = new TextField();
       addUsername.setPromptText("Username");
       addUsername.setMaxWidth(usernameCol.getPrefWidth());
       final TextField addPassword = new TextField();
       addPassword.setMaxWidth(passwordCol.getPrefWidth());
       addPassword.setPromptText("Password");
       addPassword.setStyle("-fx-background-color: #c9d9c3;");
       addUsername.setStyle("-fx-background-color: #c9d9c3;");


       VBox core = new VBox();
       Text customerAddErr = new Text("Customer already exists!");
       customerAddErr.setFill(Color.color(1,0,0));
       final Button addButton = new Button("Add");
       addButton.setStyle("-fx-background-color: #455947; -fx-text-fill: white;");
       addButton.setOnAction(e -> {
           boolean duplicate = false;


           for(Customer c: owner.getCustomers()){
               if((c.getUsername().equals(addUsername.getText()) && c.getPassword().equals(addPassword.getText())) ||
                       (addUsername.getText().equals(owner.getUsername()) && addPassword.getText().equals(owner.getPassword()))){
                   duplicate = true;
                   if(!core.getChildren().contains(customerAddErr)){
                       core.getChildren().add(customerAddErr);
                   }
               }
           }


           if(!(addUsername.getText().equals("") || addPassword.getText().equals("")) && !duplicate) {
               owner.addCustomer(new Customer(addUsername.getText(), addPassword.getText())); 
               customersTable.getItems().clear(); 
               customersTable.setItems(addCustomers());
               core.getChildren().remove(customerAddErr); 
               addPassword.clear(); 
               addUsername.clear();
           }
       });


       final Button deleteButton = new Button("Delete");
       deleteButton.setStyle("-fx-background-color: #455947; -fx-text-fill: white;");
       deleteButton.setOnAction(e -> {
           Customer selectedItem = customersTable.getSelectionModel().getSelectedItem();
           customersTable.getItems().remove(selectedItem); 
           
           owner.deleteCustomer(selectedItem); 
       });


       hb.getChildren().addAll(addUsername, addPassword, addButton, deleteButton);
       hb.setAlignment(Pos.CENTER);
       hb.setSpacing(3);


       HBox back = new HBox();
       back.setPadding(new Insets(5));
       back.getChildren().addAll(backButton);


       core.setAlignment(Pos.CENTER);
       core.setSpacing(5);
       core.setPadding(new Insets(0,0,0,110));
       core.getChildren().addAll(label, customersTable, hb);


       VBox vbox = new VBox();
       vbox.setStyle("-fx-background-color: #dfe8e0;");
       vbox.setPadding(new Insets(0, 150, 60, 0));
       vbox.getChildren().addAll(back, core);
       vbox.setAlignment(Pos.CENTER);


       ct.getChildren().addAll(vbox);
       return ct;
   }


   public static void main(String[] args) {
       launch(args);
   }
}