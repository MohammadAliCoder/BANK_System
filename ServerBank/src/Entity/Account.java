
package Entity;

public class Account {
   private int id_Account;
   private float amount;

    public Account() {
    }

    public Account(float amount) {
        this.amount = amount;
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

   
}
