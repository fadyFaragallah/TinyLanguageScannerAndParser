package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML private Button inputFileButton;
    @FXML private TextField inputTextField;
    @FXML private TextArea outputTextArea;
    @FXML private Button ParseButton;
    private ArrayList<Token> tokens;
    public void initialize(URL url, ResourceBundle rb){
        //outputTextArea.setEditable(false);
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
    public void scan() {
        tokens=new ArrayList<>();
        Scanner scanner = new Scanner(tokens);
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

        System.out.println(tokens.size());
        for(int i=0;i<tokens.size();i++){
            System.out.println(tokens.get(i));
        }
    }
    public void parse(){
        outputTextArea.clear();
        scan();
        Parser p=new Parser(tokens);
        JSONObject obj=p.parse();
        outputTextArea.clear();
        String s=obj.toString();
        //s.replace('[',' ');
        //s.replace('[',' ');

        outputTextArea.setText(s);


    }
}