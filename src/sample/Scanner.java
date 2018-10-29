package sample;


import javafx.scene.control.TextArea;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Scanner {
    private TextArea outputTextArea;
    public Scanner(){

        this.outputTextArea=null;
    }
    public Scanner(TextArea t1) {
        this.outputTextArea = t1;
}
    private static boolean isAReservedWord(String input){
        String reservedWords[]={"if","then","else","end","repeat","until","read","write"};
        for(int i=0;i<reservedWords.length;i++){
            if(reservedWords[i].equals(input))
                return true;
        }
        return false;
    }
    private static boolean isADigit(char c){
        if((int)c>=48 && (int)c<=57)
            return true;
        return false;
    }
    private static boolean isALetter(char c){
        if(((int)c>=65 && (int)c<=90) || ((int)c>=97 && (int)c<=122))
            return true;
        return false;
    }
    public void scan(String input) {
     String state="start";
        for(int i=0;i<input.length();i++){
            if(state.equals("start") && input.charAt(i)==' '){
                state="start";
            }
            else if(state.equals("start") && input.charAt(i)=='{'){
                state="incomment";
            }
            else if(state.equals("incomment") && input.charAt(i)!='}'){
                state="incomment";
            }
            else if(state.equals("incomment") && input.charAt(i)=='}'){
                state="start";
            }
            else if(state.equals("start") && (isADigit(input.charAt(i)))){
                state="innum";
                StringBuilder number=new StringBuilder();
                number.append(input.charAt(i));
                Token t=new Token();
                t.setType("number");
                i++;
                while((i<input.length()) && (isADigit(input.charAt(i)))){
                    number.append(input.charAt(i));
                    i++;
                }
                t.setValue(number.toString());
                outputTextArea.appendText(t.toString());
                outputTextArea.appendText("\n");
                if(i<input.length())
                    i--;
                state="start";
            }
            else if((state.equals("start")) && (isALetter(input.charAt(i)))){
                state="inid";
                StringBuilder id=new StringBuilder();
                id.append(input.charAt(i));
                Token t=new Token();
                i++;
                while((i<input.length()) && isALetter(input.charAt(i))){
                    id.append(input.charAt(i));
                    i++;
                }
                t.setValue(id.toString());
                if(isAReservedWord(id.toString())){
                    t.setType("reserved word");
                }
                else
                    t.setType("identifier");
                outputTextArea.appendText(t.toString());
                outputTextArea.appendText("\n");
                if(i<input.length())
                    i--;
                state="start";
            }
            else if(state.equals("start") && input.charAt(i)==':'){
                state="inassign";
                i++;
                if(input.charAt(i)=='='){
                    Token t=new Token();
                    t.setType("special symbol");
                    t.setValue(":=");
                    outputTextArea.appendText(t.toString());
                    outputTextArea.appendText("\n");
                }
                else
                    i--;
                state="start";
            }
            else{
                Token t=new Token();
                StringBuilder s=new StringBuilder();
                s.append(input.charAt(i));
                t.setType("special symbol");
                t.setValue(s.toString());
                outputTextArea.appendText(t.toString());
                outputTextArea.appendText("\n");
                state="start";
            }

        }
    }
}
