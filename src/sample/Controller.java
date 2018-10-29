package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;

public class Controller {
    @FXML private Button inputFileButton;
    @FXML private Button outputFileButton;
    @FXML private TextField inputTextField;
    @FXML private TextField outputTextField;
    @FXML private Button ScanButton;

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
    public void outputButtonClicked(){
        FileChooser fc=new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text files","*.txt"));
        File selectedFile =fc.showOpenDialog(null);
        if(selectedFile !=null){
            outputTextField.setText(selectedFile.getAbsolutePath());
        }
        else{
            //error
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","an error occured");
        }
    }
    public void Scan() {
        Scanner scanner = new Scanner(outputTextField.getText());
        try {
            FileReader fr = new FileReader(inputTextField.getText());
            BufferedReader br=new BufferedReader(fr);
            String str;
            FileWriter fw=new FileWriter(outputTextField.getText());
            PrintWriter pw=new PrintWriter(fw);
            while((str=br.readLine()) !=null) {
                scanner.scan(str,pw);
            }
            br.close();
            pw.close();
        } catch (IOException e) {
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","an error occured");
        }
    }
}
