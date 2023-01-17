/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Encryption.AES;
import Encryption.DSA;
import Encryption.PGP;
import static Encryption.PGP.encrypt;
import static Encryption.PGP.encrypt_AES;
import Entity.Transfer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

/**
 *
 * @author BARON
 */
public class FXMLDocumentController implements Initializable {

    private double XL = 0;
    private double YL = 0;
    //____________________________________________

    private Socket socket = null;
    private static InetAddress host;
    private static final int PORT = 1234;
    private static String Message;
    private static String Key = null;
    private static PrintWriter networkOutput = null;
    private static Scanner networkInput = null;
    private DataInputStream dIn;
    private DataOutputStream dOut;
    //______________________________________________
    @FXML
    private ImageView Min;

    @FXML
    private TextField amount;

    @FXML
    private TextField Username;

    @FXML
    private TextField message;
    @FXML
    private Button Button_login;
    @FXML
    private Button Send;
    @FXML
    private Label labState;

    @FXML
    private RadioButton Radio_AES;

    @FXML
    private RadioButton Radio_RSA;

    @FXML
    private RadioButton Radio_DSA;

    @FXML
    void Exit(MouseEvent event) {

        if (socket != null) {
            try {
                System.out.println("\nClosing connection...");
                socket.close();
            } catch (IOException ioEx) {
                System.out.println("Unable to disconnect!");
            }
        }
        System.exit(0);
    }

    @FXML
    void Min(MouseEvent event) {
        Stage stage = (Stage) Min.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void AES_Action(ActionEvent event) {
        Radio_AES.setSelected(true);
        Radio_RSA.setSelected(false);
        Radio_DSA.setSelected(false);

    }

    @FXML
    void RSA_Action(ActionEvent event) {
        Radio_AES.setSelected(false);
        Radio_RSA.setSelected(true);
        Radio_DSA.setSelected(false);
    }

    @FXML
    void DSA_Action(ActionEvent event) {
        Radio_AES.setSelected(false);
        Radio_RSA.setSelected(false);
        Radio_DSA.setSelected(true);
    }

    @FXML
    void Login_Action(ActionEvent event) {
        mainLogin();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Radio_AES.setSelected(true);
        try {
            host = InetAddress.getLocalHost();
            socket = new Socket(host, PORT);
            networkInput = new Scanner(socket.getInputStream());
            networkOutput = new PrintWriter(socket.getOutputStream(), true);
            dIn = new DataInputStream(socket.getInputStream());
            dOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.err.println("Service is off try please again in other time");
            labState.setText("Service is off try please again in other time");
        }

        Send.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                labState.setText("");
                if (Accounts.isEmpty()) {
                    mainLogin();
                } else {
                    if (!amount.getText().isEmpty() && !Username.getText().isEmpty() && !message.getText().isEmpty()) {

                        Transfer transfer = new Transfer();
                        transfer.setAmount(Float.valueOf(amount.getText()));
                        transfer.setMessage(message.getText());
                        transfer.setUsername(Username.getText());

                        Message = Accounts.getAccounts() + "\t" + transfer.toString();

                        if (Radio_AES.isSelected()) {
                            try {
                                //____________key__________ 
                                networkOutput.println("AES," + Accounts.Username);

                                //______________send transfer__________
                                networkOutput.println(AES.encrypt(Message, Accounts.Password));
                                labState.setText(networkInput.nextLine());

                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }

//                           
                        } else if (Radio_RSA.isSelected()) {
                            try {
                                //____________key_____PGP_____ 

                                networkOutput.println(Accounts.FName + "," + Accounts.LName + "," + Accounts.Username + ",PGP");

                                String publiceKey = networkInput.nextLine();
                                Key = PGP.getKey_Random();

                                String encryptedkey = PGP.encrypt(publiceKey, Key);
                                networkOutput.println(new String(encryptedkey));
                                //______________send transfer && encryptedData AES__________
                                String encryptedData = encrypt_AES(Message, Key);
                                networkOutput.println(encryptedData);
                                labState.setText(networkInput.nextLine());

                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }

                        } else if (Radio_DSA.isSelected()) {
                            System.out.println("good");
                            try {
                                //____________key_____DSA_____ 
                                //بعثنا الحساب والتشفير
                                networkOutput.println(Accounts.FName + "," + Accounts.LName + "," + Accounts.Username + ",DSA");

                                //استلمنا برايفت
                                String privateKey = networkInput.nextLine();
                                //Key = PGP.getKey_Random();

                                //String encryptedkey = PGP.encrypt(publiceKey, Key);
                                //networkOutput.println(new String(encryptedkey));
                                //______________send transfer && encryptedData AES__________
                                //String encryptedData = encrypt_AES(Message, Key);
                                //بعثنا شهادة موقعة
                                System.out.println("byte1 :" + DSA.performSigning("test", privateKey));
                                //byte[] s = DSA.performSigning("test", privateKey);

                                //System.out.println("string1 " + new String(s));

                                //*String sign = new String(DSA.performSigning("test", privateKey));
                                byte[] message = DSA.performSigning("test", privateKey);
                                //System.out.println("string2 "+sign);

                               // System.out.println("byte message  " + message);

                                //*networkOutput.println(sign);
                                dOut.writeInt(message.length);
                                dOut.write(message);
                                //______________send transfer && encryptedData AES__________
                                //String encryptedData = encrypt_AES(Message, Key);
                                networkOutput.println(Message);
//                                labState.setText(networkInput.nextLine());

                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    } else {

                    }

                }
            }
        });

    }

    public void mainLogin() {
        Stage stage = new Stage();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("GUILogin.fxml"));

            root.setOnMousePressed(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    XL = t.getSceneX();
                    YL = t.getSceneY();
                }
            });

            root.setOnMouseDragged(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {

                    stage.setX(t.getScreenX() - XL);
                    stage.setY(t.getScreenY() - YL);

                }
            });
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
