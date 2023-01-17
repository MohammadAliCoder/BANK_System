/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbank;

import Entity.Account;
import Entity.ConnectDB;
import Entity.Customer;
import Entity.Customer_Table;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author BARON
 */
public class ServerController implements Initializable {

    private static ServerSocket serverSocket;
    private static final int PORT = 1234;
    private static int count = 0;
    ConnectDB connectDB = new ConnectDB();
    @FXML
    private Label stateServer;

    @FXML
    private Label countusers;

    @FXML
    private TextField Fname;

    @FXML
    private TextField Lname;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private TextField amount;

    @FXML
    private TextField Delete_Fname;

    @FXML
    private TextField Delete_Lname;

    @FXML
    private TableView<Customer_Table> Table_show;

    @FXML
    private TableView<?> Table_Transfer;

    @FXML
    void Delete(ActionEvent event) {
        Customer customer = new Customer();
        customer.setFname(Delete_Fname.getText());
        customer.setLname(Delete_Lname.getText());
        try {
            if (!customer.getLname().isEmpty() && !customer.getFname().isEmpty()) {
                connectDB.delete_customer(customer);

                Table_show.setItems(connectDB.getAll_Customer_Table());
            } else {
                JOptionPane.showMessageDialog(null, "Please input Fname or Lname");
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    void Exit(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void Run_Server(ActionEvent event) {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                connectDB.Connect();

                try {
                    serverSocket = new ServerSocket(PORT);
                    System.out.println("Starting Server...");

                } catch (IOException ioEx) {
                    System.out.println("\nUnable to set up port!");
                    System.exit(1);
                }

                do {

                    try {
                        Socket client = serverSocket.accept();
                        System.out.println("\nNew client accepted.\n");

                        ClientHandler handler = new ClientHandler(client);
                        handler.start();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } while (true);

            }

        });
        t.start();
        stateServer.setText("State Server :ON");

    }

    @FXML
    void Save(ActionEvent event) {
        try {
            Customer customer = new Customer(Fname.getText(), Lname.getText(),
                     username.getText(), password.getText());

            if (!customer.getLname().isEmpty() && !customer.getFname().isEmpty() && !customer.getUsername().isEmpty()
                    && !customer.getPassword().isEmpty() && !amount.getText().isEmpty()) {

                Account account = new Account(Float.valueOf(amount.getText()));

                connectDB.insert_Customer(customer, account);
            } else {
                JOptionPane.showMessageDialog(null, "Please input every data");
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    void Show_Transfer(ActionEvent event) {
        try {
            try {
                Table_Transfer.setItems(connectDB.getAll_transfers());
                TableColumn id = new TableColumn("Id");
                id.setCellValueFactory(new PropertyValueFactory("idc"));
                TableColumn fname = new TableColumn("First Name");
                fname.setCellValueFactory(new PropertyValueFactory("Fname"));
                TableColumn Lname = new TableColumn("Last Name");
                Lname.setCellValueFactory(new PropertyValueFactory("Lname"));
                TableColumn UserName = new TableColumn("UserName");
                UserName.setCellValueFactory(new PropertyValueFactory("username"));
                TableColumn Amount = new TableColumn("Amount");
                Amount.setCellValueFactory(new PropertyValueFactory("Amount"));

                TableColumn idt = new TableColumn("Id Transfer");
                idt.setCellValueFactory(new PropertyValueFactory("idc"));
                TableColumn AmountTrans = new TableColumn("Amount Trasfer");
                AmountTrans.setCellValueFactory(new PropertyValueFactory("amounttran"));
                TableColumn Message = new TableColumn("Message");
                Message.setCellValueFactory(new PropertyValueFactory("message"));

                Table_Transfer.getColumns().setAll(id, fname, Lname, UserName, Amount, idt, AmountTrans, Message);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception ex) {
            System.err.println("please on server");
        }
    }

    @FXML
    void min(MouseEvent event) {
        Stage stage = (Stage) stateServer.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void show(ActionEvent event) {
        try {
            Table_show.setItems(connectDB.getAll_Customer_Table());
            TableColumn id = new TableColumn("Id");
            id.setCellValueFactory(new PropertyValueFactory("idc"));
            TableColumn fname = new TableColumn("First Name");
            fname.setCellValueFactory(new PropertyValueFactory("Fname"));

            TableColumn Lname = new TableColumn("Last Name");
            Lname.setCellValueFactory(new PropertyValueFactory("Lname"));
            TableColumn UserName = new TableColumn("UserName");
            UserName.setCellValueFactory(new PropertyValueFactory("username"));
            TableColumn Amount = new TableColumn("Amount");
            Amount.setCellValueFactory(new PropertyValueFactory("Amount"));
            Table_show.getColumns().setAll(id, fname, Lname, UserName, Amount);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
