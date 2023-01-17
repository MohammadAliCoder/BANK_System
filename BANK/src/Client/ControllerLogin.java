/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author BARON
 */
public class ControllerLogin  implements Initializable {
 
      @FXML
    private TextField username;

    @FXML
    private TextField FName;

    @FXML
    private TextField LName;

    @FXML
    private PasswordField Password;
    
    

    @FXML
    void Login(ActionEvent event) {
          if(!username.getText().isEmpty() && !Password.getText().isEmpty() 
                 && !FName.getText().isEmpty() &&! LName.getText().isEmpty()){
             Accounts.FName=FName.getText();
             Accounts.LName=LName.getText();
             Accounts.Username=username.getText();
             Accounts.Password=Password.getText();
            
             username.getScene().getWindow().hide();
          }else{
              JOptionPane.showMessageDialog(null, "Please input Email and Password");
          }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
     
     
    }
  
}
