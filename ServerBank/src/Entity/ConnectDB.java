/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Encryption.DSA;
import java.math.BigDecimal;
import java.security.KeyPair;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author BARON
 */
public class ConnectDB {

    private final String url="jdbc:sqlserver://MOHAMMED:1433;databaseName=banck";
    private final String password="12345";
    private final String user="sa";
    private final String Driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static Connection connection;
    private static Statement statement;
    private  String Sql;
    private  PreparedStatement ps;
    public void Connect() {
        try {
            Class.forName(Driver);
             connection=DriverManager.getConnection(url, user, password);
            statement=connection.createStatement();
        } catch (Exception e) {
            throw  new RuntimeException("Error in Connect DB");
        }
    }
    
//    public TableModel getAll_Customer () throws Exception{
//         Sql="select idc,fname,lname,username,amount from customer c,account a  where c.idAccount=a.ida";
//        ResultSet resultSet=statement.executeQuery(Sql);
//        return DbUtils.resultSetToTableModel(resultSet);
//    }
    public ObservableList getAll_Customer_Table () throws Exception{
        List<Customer_Table> list=new ArrayList<>();
         Sql="select idc,fname,lname,username,amount from customer c,account a  where c.idAccount=a.ida";
        ResultSet resultSet=statement.executeQuery(Sql);
        while (resultSet.next()) {            
            list.add(new Customer_Table(resultSet.getInt(1), resultSet.getString(2)
             , resultSet.getString(3), resultSet.getString(4), 
                    resultSet.getFloat(5)));
        }
       ObservableList data = FXCollections.observableList(list);
 
        return data;
    }
    
    public ObservableList getAll_transfers () throws Exception{
        List<Customer_Table> list=new ArrayList<>();
         Sql="select idc,fname,lname,username,amount,idt,amounttran,message from customer c,account a,transfer t where c.idAccount=a.ida and a.ida=t.idaccount";
        ResultSet resultSet=statement.executeQuery(Sql);
        while (resultSet.next()) {            
            list.add(new Customer_Table(resultSet.getInt(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4), 
              resultSet.getFloat(5),resultSet.getInt(6),
              resultSet.getFloat(7),resultSet.getString(8)));
        }
       ObservableList data = FXCollections.observableList(list);
 
        return data;
    }
    public void insert_Customer(Customer customer,Account account) throws Exception{
        KeyPair generateKeyPair = DSA.generateKeyPair();
        String publicKey = Base64.getEncoder().encodeToString(generateKeyPair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(generateKeyPair.getPrivate().getEncoded());
     Sql="insert into customer (fname,lname,username,password,idAccount,publickey,privatekey)values(?,?,?,?,?,?,?)";
     ps=connection.prepareStatement(Sql);
     customer.setId_Customer(next_idc());
     ps.setString(1, customer.getFname());
     ps.setString(2, customer.getLname());
     ps.setString(3, customer.getUsername());
     ps.setString(4, customer.getPassword());
     ps.setInt(5, customer.getId_Customer());
     ps.setString(6, publicKey);
     ps.setString(7, privateKey);
     ps.executeUpdate();
     
     Sql="insert into account(ida,amount)values(?,?)";
     ps=connection.prepareStatement(Sql);
     ps.setInt(1,customer.getId_Customer());
     ps.setFloat(2, account.getAmount());
     ps.executeUpdate();
     
        JOptionPane.showMessageDialog(null, "save successfully");
    }
    
    public void delete_customer(Customer customer) throws SQLException{
     Sql="delete from customer where fname=? and lname=?";   
      ps=connection.prepareStatement(Sql);
      ps.setString(1, customer.getFname());
      ps.setString(2, customer.getLname());
      ps.executeUpdate();
     
        JOptionPane.showMessageDialog(null, "Delete successfully");
    }

    public int next_idc() throws Exception{
          int id=0;
        Sql="select max(idc) from customer";
        ResultSet resultSet=statement.executeQuery(Sql);
                 while (resultSet.next()) {            
                     id=resultSet.getInt(1);
                     }
           
           
       return (id+1);
    }
  
    
    public Account getAccount(Customer customer) throws Exception{
    
     Account account=new Account();
        Sql="select  ida,amount from customer ,account where username='"+customer.getUsername()+"' and fname='"+customer.getFname()+"' and ida=idAccount";
        ResultSet resultSet=statement.executeQuery(Sql);
                 while (resultSet.next()) {            
                     account.setId_Account(resultSet.getInt(1));
                     account.setAmount(resultSet.getFloat(2));
                     }
           
           
       return account;
    }
   
    public void updateAccount(Accounts customer,float amount) throws Exception{
        Sql="update account set amount=? where ida=("
                + "select  ida  from customer ,account where username=? and fname=? and ida=idAccount )";
        
      ps=connection.prepareStatement(Sql);
      ps.setFloat(1, amount);
      ps.setString(2, customer.getUsername());
      ps.setString(3, customer.getFName());
      ps.executeUpdate();
    
    }
     public void updateAccountSend(Transfer transfer,float amount) throws Exception{
        Sql="update account set amount=? where ida=("
                + "select  ida  from customer ,account where username=? and ida=idAccount )";
        
      ps=connection.prepareStatement(Sql);
      ps.setFloat(1, amount);
      ps.setString(2, transfer.getUsername());
      ps.executeUpdate();
    
    }
   
    public void insertTransfer(Transfer transfer) throws Exception{
        Sql="insert into transfer(amounttran,message,receiverUsername,idaccount)values(?,?,?,?)";
        
      ps=connection.prepareStatement(Sql);
      ps.setFloat(1, transfer.getAmount());
      ps.setString(2, transfer.getMessage());
      ps.setString(3, transfer.getUsername());
      ps.setInt(4, transfer.getId_account());
      ps.executeUpdate();
    
    }
   
    public int getIDCustomer(Accounts accounts){
        int id=0;
        try {
             Sql="select idc from customer where lname='"+accounts.LName+"' and username='"+accounts.userName+"' and fname='"+accounts.FName+"' and password='"+accounts.Password+"'";
                ResultSet resultSet=statement.executeQuery(Sql);
                 while (resultSet.next()) {            
                     id=resultSet.getInt(1);
                     }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
           
           
           
       
        return id;
    }
    
    public float  getAmount(int id){
        float amount=0;
        try {
             Sql="select amount from account where ida="+id;
                ResultSet resultSet=statement.executeQuery(Sql);
                 while (resultSet.next()) {            
                     amount=resultSet.getFloat(1);
                     }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
          
        return amount;
    }
    public float  getAmountUserRecive(String username){
        float amount=0;
         try {
             Sql="select amount from customer,account where ida=idAccount and username='"+username+"'";
                ResultSet resultSet=statement.executeQuery(Sql);
                 while (resultSet.next()) {            
                     amount=resultSet.getFloat(1);
                     }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  amount;
    }
    public boolean isFound_Username(String username){
     
        String isnotNull=null;
        try {
             Sql="select username from customer where username='"+username+"'";
                ResultSet resultSet=statement.executeQuery(Sql);
                 while (resultSet.next()) {            
                     isnotNull=resultSet.getString(1);
                     }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(isnotNull!=null )
              return true;
          
        else return false;
    }
    public String get_publickey(Accounts accounts){
        String publickey=null;
         try {
             Sql="select publickey from customer where username='"+accounts.getUsername()+"' and fname='"+accounts.getFName()+
                     "' and lname='"+accounts.getLName()+"'";
                ResultSet resultSet=statement.executeQuery(Sql);
                 while (resultSet.next()) {            
                     publickey=resultSet.getString(1);
                     }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  publickey;
    }
    public String get_privatekey(Accounts accounts){
       String privatekey=null;
         try {
             Sql="select privatekey from customer where username='"+accounts.getUsername()+"' and fname='"+accounts.getFName()+
                     "' and lname='"+accounts.getLName()+"'";
                ResultSet resultSet=statement.executeQuery(Sql);
                 while (resultSet.next()) {            
                     privatekey=resultSet.getString(1);
                     }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  privatekey;
    }
    public String get_Password(String Username){
       String password=null;
         try {
             Sql="select password from customer where Username='"+Username+"'" ;
                ResultSet resultSet=statement.executeQuery(Sql);
                 while (resultSet.next()) {            
                     password=resultSet.getString(1);
                     }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  password;
    }
    
}
