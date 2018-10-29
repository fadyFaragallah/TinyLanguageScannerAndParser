package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML private Button inputFileButton;
    @FXML private TextField inputTextField;
    @FXML private TextArea outputTextArea;
    @FXML private Button ScanButton;
    public void initialize(URL url, ResourceBundle rb){
        outputTextArea.setEditable(false);
    }
    public void inputButtonClicked(){
                FileChooser fc=new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text files","*.txt"));
        File selectedFile =fc.showOpenDialog(null);
        if(selectedFile !=null){
            inputTextField.setText(selectedFile.getAbsolutePath());
        }
        else{
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","an error occured");
        }
    }
    public void Scan() {
        Scanner scanner = new Scanner(outputTextArea);
        outputTextArea.clear();
        try {
            FileReader fr = new FileReader(inputTextField.getText());
            BufferedReader br=new BufferedReader(fr);
            String str;
            while((str=br.readLine()) !=null) {
                scanner.scan(str);
            }
            br.close();
        } catch (IOException e) {
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","an error occured");
        }
    }
}
