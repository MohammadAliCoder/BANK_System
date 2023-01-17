/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author BARON
 */
public class Accounts {
    public  String userName=null ,Password=null ,FName=null,LName=null;

    public Accounts() {
    }

    public Accounts(String fname,String lanme,String username ,String Password) {
        this.FName=fname;
        this.LName=lanme;
        this.userName=username;
        this.Password=Password;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    
    

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }
    
    
    public  boolean isEmpty(){
        if(userName==null ||Password==null || FName==null||LName==null)return true;
        else return false;
    }
    
}
