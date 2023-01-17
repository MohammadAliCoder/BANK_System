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
public class Customer_Table {
   int idc;
   String fname;
   String lname;
   String username;
   float amount ;
   int idt;
  float amounttran;
  String message;

    public Customer_Table(int idc, String fname, String lname, String username, float amount) {
        this.idc = idc;
        this.fname = fname;
        this.lname = lname;
        this.username = username;
        this.amount = amount;
    }

    public Customer_Table(int idc, String fname, String lname, String username, float amount, int idt, float amounttran, String message) {
        this.idc = idc;
        this.fname = fname;
        this.lname = lname;
        this.username = username;
        this.amount = amount;
        this.idt = idt;
        this.amounttran = amounttran;
        this.message = message;
    }

    public int getIdc() {
        return idc;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getUsername() {
        return username;
    }

    public float getAmount() {
        return amount;
    }

    public int getIdt() {
        return idt;
    }

    public float getAmounttran() {
        return amounttran;
    }

    public String getMessage() {
        return message;
    }
   
}
