/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbank;

import Encryption.AES;
import Encryption.DSA;
import Encryption.PGP;
import static Encryption.PGP.decrypt;
import static Encryption.PGP.encrypt_AES;
import Entity.Accounts;
import Entity.ConnectDB;
import Entity.Transfer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.Signature;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BARON
 */
class ClientHandler extends Thread {

    private Socket client;
    private Scanner input;
    private PrintWriter output;
    private DataInputStream dIn;
    private DataOutputStream dOut;
    ConnectDB connectDB;

    public ClientHandler(Socket socket) {
        client = socket;
        try {
            input = new Scanner(client.getInputStream());
            output = new PrintWriter(client.getOutputStream(), true);
            dIn = new DataInputStream(client.getInputStream());
            dOut = new DataOutputStream(client.getOutputStream());

        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    public void run() {
        String received;
        connectDB = new ConnectDB();
        String TypeEncryption = null;
        while (true) {
            try {
                //استقبلنا الحساب والتشفير
                TypeEncryption = input.nextLine();

                if (TypeEncryption.split(",")[0].equals("AES") && TypeEncryption.split(",").length == 2) {
                    

                    String key = connectDB.get_Password(TypeEncryption.split(",")[1]);
                    //output.println(key);
                    //______________
                    received = input.nextLine();

                    String message = AES.decrypt(received, key);
                    if (message != null) {

                        String[] packet = message.split("\t");
                        String[] accounts = packet[0].split(",");
                        String[] transfer = packet[1].split(",");
                        Accounts myaccounts = new Accounts(accounts[0], accounts[1], accounts[2], accounts[3]);
                        int id = connectDB.getIDCustomer(myaccounts);

                        if (id != 0) {
                            float amountDb = connectDB.getAmount(id);
                            float amountuser = Float.valueOf(transfer[0]);

                            Transfer myTransfer = new Transfer(amountuser, transfer[1], transfer[2], id);
                            if (amountuser <= amountDb) {
                                if (connectDB.isFound_Username(myTransfer.getUsername()) == true) {

                                    connectDB.updateAccount(myaccounts, (amountDb - amountuser));
                                    connectDB.insertTransfer(myTransfer);
                                    float amountRecive = connectDB.getAmountUserRecive(myTransfer.getUsername());
                                    connectDB.updateAccountSend(myTransfer, (amountRecive + amountuser));
                                    output.println("Done send");

                                } else {
                                    output.println("This name does not found");
                                }

                            } else {
                                output.println("Your balance is insufficient amount:" + amountDb);
                            }

                        } else {
                            output.println("Don't have an account");
                        }
                    }

                } else if (TypeEncryption.split(",")[3].equals("PGP") && TypeEncryption.split(",").length == 4) {
                    
                    String fname = TypeEncryption.split(",")[0];
                    String lname = TypeEncryption.split(",")[1];
                    String username = TypeEncryption.split(",")[2];

                    Accounts a = new Accounts(fname, lname, username, null);
                    if (connectDB.get_privatekey(a).length() != 0 && connectDB.get_privatekey(a) != null) {
                        String publicKey = connectDB.get_publickey(a);
                        String privatekey = connectDB.get_privatekey(a);

                        output.println(publicKey);
                        String key = input.nextLine();
                        String decryptedData = PGP.decrypt(privatekey, key);
                        String receivKey = new String(decryptedData);
                        //____________received data and decrypt_AES__
                        received = input.nextLine();
                        String message = PGP.decrypt_AES(received, receivKey);
                        if (message != null) {

                            String[] packet = message.split("\t");
                            String[] accounts = packet[0].split(",");
                            String[] transfer = packet[1].split(",");
                            Accounts myaccounts = new Accounts(accounts[0], accounts[1], accounts[2], accounts[3]);
                            int id = connectDB.getIDCustomer(myaccounts);

                            if (id != 0) {
                                float amountDb = connectDB.getAmount(id);
                                float amountuser = Float.valueOf(transfer[0]);

                                Transfer myTransfer = new Transfer(amountuser, transfer[1], transfer[2], id);
                                if (amountuser <= amountDb) {
                                    if (connectDB.isFound_Username(myTransfer.getUsername()) == true) {

                                        connectDB.updateAccount(myaccounts, (amountDb - amountuser));
                                        connectDB.insertTransfer(myTransfer);
                                        float amountRecive = connectDB.getAmountUserRecive(myTransfer.getUsername());
                                        connectDB.updateAccountSend(myTransfer, (amountRecive + amountuser));
                                        output.println("Done send");

                                    } else {
                                        output.println("This name does not found");
                                    }

                                } else {
                                    output.println("Your balance is insufficient amount:" + amountDb);
                                }

                            } else {
                                output.println("Don't have an account");
                            }

                        }

                    }

                } else if (TypeEncryption.split(",")[3].equals("DSA") && TypeEncryption.split(",").length == 4) {

                    System.out.println("Check 1");
                    String fname = TypeEncryption.split(",")[0];
                    String lname = TypeEncryption.split(",")[1];
                    String username = TypeEncryption.split(",")[2];

                    Accounts a = new Accounts(fname, lname, username, null);
                    if (connectDB.get_privatekey(a).length() != 0 && connectDB.get_privatekey(a) != null) {

                        System.out.println("Check 2");
                        String publicKey = connectDB.get_publickey(a);
                        String privatekey = connectDB.get_privatekey(a);

                        System.out.println("1  " + privatekey);
                        output.println(privatekey);
                        System.out.println("2  " + publicKey);
                        //String key = input.nextLine();
                        /////String decryptedData = PGP.decrypt(privatekey, key);
                        ///// String receivKey = new String(decryptedData);

                        System.out.println("Check 3");
                        //____________received data and decrypt_AES__
                        //منستقبل التوقيع ومنتأكد منو
                        //*received = input.nextLine(); //signature
                        int length = dIn.readInt();
                        byte[] message;
                        String data;
                        if (length > 0) {
                            message = new byte[length];
                            dIn.readFully(message, 0, message.length);
                            System.out.println("byte[] message  " + message);
                            //System.out.println(DSA.performVerification("test", message, publicKey));
                            if (DSA.performVerification("test", message, publicKey).equals("true")) {
                            System.out.println("worked :)");
                            received = input.nextLine();
                            data = received;
                            /// String message = DSA.performVerification(key, key, signature, publicKey);
                            if (data != null) {

                                //____________received data and decrypt_AES__
                        
                                // String data = PGP.decrypt_AES(received, receivKey);
                                
                                String[] packet = data.split("\t");
                                String[] accounts = packet[0].split(",");
                                String[] transfer = packet[1].split(",");
                                Accounts myaccounts = new Accounts(accounts[0], accounts[1], accounts[2], accounts[3]);
                                int id = connectDB.getIDCustomer(myaccounts);

                                if (id != 0) {
                                    float amountDb = connectDB.getAmount(id);
                                    float amountuser = Float.valueOf(transfer[0]);

                                    Transfer myTransfer = new Transfer(amountuser, transfer[1], transfer[2], id);
                                    if (amountuser <= amountDb) {
                                        if (connectDB.isFound_Username(myTransfer.getUsername()) == true) {

                                            connectDB.updateAccount(myaccounts, (amountDb - amountuser));
                                            connectDB.insertTransfer(myTransfer);
                                            float amountRecive = connectDB.getAmountUserRecive(myTransfer.getUsername());
                                            connectDB.updateAccountSend(myTransfer, (amountRecive + amountuser));
                                            output.println("Done send");

                                        } else {
                                            output.println("This name does not found");
                                        }

                                    } else {
                                        output.println("Your balance is insufficient amount:" + amountDb);
                                    }

                                } else {
                                    output.println("Don't have an account");
                                }

                            }
                        }
                        }
                        
                        //*byte[] sign = received.getBytes();
                       // System.out.println("byte :" + sign);
                        System.out.println("Check 4");
                        
                        
                    }

                }

            } catch (Exception e) {
                try {
                    System.out.println("Closing down connection...");
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    break;
                }

            }
        }

    }
}
