package sample;


import javafx.scene.control.TextArea;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Scanner {
    private TextArea outputTextArea;
    private static enum STATE_TYPES{
            START,INCOMMENT,INNUM,INID,INASSIGN,DONE
    }
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
        STATE_TYPES state = STATE_TYPES.START;
        Token t=new Token();
        StringBuilder s=new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            switch (state) {
                case START:
                    if (input.charAt(i) == ' ')
                        state = STATE_TYPES.START;
                    else if (input.charAt(i) == '{')
                        state = STATE_TYPES.INCOMMENT;
                    else if (isADigit(input.charAt(i))) {
                        t.setType(Constants.NUMBER);
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INNUM;
                    } else if (isALetter(input.charAt(i))) {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INID;
                    } else if (input.charAt(i) == ':') {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INASSIGN;
                    } else {
                        state = STATE_TYPES.DONE;
                        t.setType(Constants.SPECIAL_SYMBOL);
                        s.append(input.charAt(i));
                    }
                    break;
                case INCOMMENT:
                    if (input.charAt(i) != '}') {
                        state = STATE_TYPES.INCOMMENT;
                    } else
                        state = STATE_TYPES.START;
                    break;
                case INNUM:
                    if (isADigit(input.charAt(i))) {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INNUM;
                    } else if (input.charAt(i) == ' ' || i == input.length() - 1 || input.charAt(i)==';') {
                        state = STATE_TYPES.DONE;
                        i--;
                    } else {
                        //error
                    }
                    break;
                case INID:
                    if (isALetter(input.charAt(i))) {
                        s.append(input.charAt(i));
                        state = STATE_TYPES.INID;
                        if(i==input.length()-1){
                            t.setValue(s.toString());
                            t.setType(Constants.RESERVED_WORD);
                            s.delete(0, s.length());
                            state = STATE_TYPES.START;
                            outputTextArea.appendText(t.toString());
                            outputTextArea.appendText("\n");
                        }
                    } else if (input.charAt(i) == ' ' || i == input.length() - 1 || input.charAt(i)==';') {
                        state = STATE_TYPES.DONE;
                        i--;
                        if (isAReservedWord(s.toString()))
                            t.setType(Constants.RESERVED_WORD);
                        else
                            t.setType(Constants.IDENTIFIER);
                    } else {
                        //error
                    }
                    break;
                case INASSIGN:
                    if (input.charAt(i) == '=') {
                        state = STATE_TYPES.DONE;
                        s.append(input.charAt(i));
                        t.setType(Constants.SPECIAL_SYMBOL);
                    } else {
                        state = STATE_TYPES.DONE;
                        i--;
                    }
                    break;
                case DONE:
                    t.setValue(s.toString());
                    i--;
                    s.delete(0, s.length());
                    state = STATE_TYPES.START;
                    outputTextArea.appendText(t.toString());
                    outputTextArea.appendText("\n");
            }
        }

    }
}
