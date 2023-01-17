
package Entity;

/**
 *
 * @author BARON
 */
public class Transfer {
    private int id;
    private float amount;
    private String message;
    private String Username;
    private int id_account;

    public Transfer(float amount, String Username, String message, int id_account) {
        this.amount = amount;
        this.message = message;
        this.Username = Username;
        this.id_account = id_account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }
    
}
