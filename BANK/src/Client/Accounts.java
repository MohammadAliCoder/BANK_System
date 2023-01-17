/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

/**
 *
 * @author BARON
 */
public class Accounts {

    public static String Username = null, Password = null, FName = null, LName = null;

    public static boolean isEmpty() {
        if (Username == null || Password == null || FName == null || LName == null) {
            return true;
        } else {
            return false;
        }
    }

    public static String getAccounts() {
        return FName + "," + LName + "," + Username + "," + Password;
    }

}
