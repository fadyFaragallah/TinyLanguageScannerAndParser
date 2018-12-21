package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
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
        try{
            PrintStream out = new PrintStream(new FileOutputStream("output.js"));
            out.print("var myObj="+s);
        }catch(Exception e){

        }

        ///


        StackPane secondaryLayout = new StackPane();
        Scene secondScene = new Scene(secondaryLayout, 1000, 700);
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        browser.sceneProperty().addListener(new ChangeListener<Scene>() {

            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene scene) {
                if (scene != null) {
                    browser.setMaxSize(secondScene.getWidth(),secondScene.getHeight());
                    browser.maxWidthProperty().bind(secondScene.widthProperty());
                    browser.maxHeightProperty().bind(secondScene.heightProperty());
                } else {
                    browser.maxWidthProperty().unbind();
                    browser.maxHeightProperty().unbind();
                }
            }
        });
        File f = new File("index.html");
        webEngine.load(f.toURI().toString());

        secondaryLayout.getChildren().add(browser);



        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Tiny Syntax Tree");
        newWindow.setScene(secondScene);

        newWindow.show();
        ///
    }
}