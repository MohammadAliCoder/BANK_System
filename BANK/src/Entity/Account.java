
package Entity;

public class Account {
   private int id_Account;
   private float amount;
   private String Number;
   private String message;

    public Account() {
    }

    public Account(int id_Account, float amount, String Number, String message) {
        this.id_Account = id_Account;
        this.amount = amount;
        this.Number = Number;
        this.message = message;
    }

    public int getId_Account() {
        return id_Account;
    }

    public void setId_Account(int id_Account) {
        this.id_Account = id_Account;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String Number) {
        this.Number = Number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
   
   
}
