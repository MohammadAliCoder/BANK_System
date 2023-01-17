
package Entity;



public class Customer {
    
    private int Id_Customer;
    private String Fname;
    private String Lname;
    private String Username;
    private String password;
    private int Id_Account;
    public Customer() {
    }

    public Customer(String username, String password) {
        this.Username = username;
        this.password = password;
    }

    public Customer(String Fname, String Lname, String username, String password) {
        this.Fname = Fname;
        this.Lname = Lname;
        this.Username = username;
        this.password = password;
    }
    

    
    public int getId_Customer() {
        return Id_Customer;
    }

    public void setId_Customer(int Id_Customer) {
        this.Id_Customer = Id_Customer;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

   

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFname() {
        return Fname;
    }

    public void setFname(String Fname) {
        this.Fname = Fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String Lname) {
        this.Lname = Lname;
    }

    public int getId_Account() {
        return Id_Account;
    }

    public void setId_Account(int Id_Account) {
        this.Id_Account = Id_Account;
    }
    
    
    
}
