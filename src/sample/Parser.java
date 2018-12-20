package sample;


import jdk.nashorn.internal.scripts.JS;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Stack;

public class Parser {
    private JSONObject root;
    private ArrayList<Token> tokens;
    private Stack<JSONObject> references;
    private int currentIndex;
    private static boolean isANumber;
    private int numberOfAss;
    public Parser(ArrayList<Token> tokens){
        this.tokens=tokens;
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    private void matchType(String s){
        if(tokens.get(currentIndex).getType().equals(s))
            currentIndex++;
        else{
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","syntax error");
        }
    }

    private void match(String tokenValue){
        if(tokens.get(currentIndex).getValue().equals(tokenValue))
            currentIndex++;
        else{
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","syntax error");
        }
    }

    public JSONObject parse(){
        this.root=new JSONObject();
        this.references=new Stack<>();
        this.references.push(root);
        currentIndex=0;
        numberOfAss=0;
        isANumber=false;
        program();
        System.out.println(currentIndex);
        return root;
    }

    private void program(){
        stmt_sequence();
    }

    private void stmt_sequence(){
        statement();
        while(tokens.get(currentIndex).getValue().equals(";")){
            match(";");
            if(currentIndex==tokens.size())
                return;
            statement();
        }
    }

    private void statement(){
        JSONObject ref,obj;
        ref=references.peek();
        obj=new JSONObject();
        ref.put(Integer.toString(numberOfAss++),obj);
        references.push(obj);
        if(tokens.get(currentIndex).getValue().equals("if"))
            if_stmt();
        else if(tokens.get(currentIndex).getValue().equals("repeat"))
            repeat_stmt();
        else if(tokens.get(currentIndex).getType().equals(Constants.IDENTIFIER))
            assign_stmt();
        else if(tokens.get(currentIndex).getValue().equals("read"))
            read_stmt();
        else if(tokens.get(currentIndex).getValue().equals("write"))
            write_stmt();
        else{
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","syntax error");
        }
        references.pop();
    }

    private void if_stmt(){
        match("if");
        JSONObject ref;
        ref=references.peek();
        JSONObject obj=new JSONObject();
        ref.put("ifStatement",obj);
        references.push(obj);

        ref=references.peek();
        obj=new JSONObject();
        ref.put("testPart",obj);
        references.push(obj);
        exp();
        references.pop();

        match("then");

        ref=references.peek();
        obj=new JSONObject();
        ref.put("thenPart",obj);
        references.push(obj);

        stmt_sequence();

        references.pop();

        if(tokens.get(currentIndex).getValue().equals("else")){
            ref=references.peek();
            obj=new JSONObject();
            ref.put("elsePart",obj);
            references.push(obj);
            match("else");

            stmt_sequence();
            match("end");
            references.pop();
        }
        references.pop();
    }

    private void repeat_stmt(){
        match("repeat");
        JSONObject ref;
        ref=references.peek();
        JSONObject obj=new JSONObject();
        ref.put("repeatStatement",obj);
        references.push(obj);

        ref=references.peek();
        obj=new JSONObject();
        ref.put("repeatBody",obj);
        references.push(obj);

        stmt_sequence();

        references.pop();

        match("until");

        ref=references.peek();
        obj=new JSONObject();
        ref.put("repeatTest",obj);
        references.push(obj);

        exp();

        references.pop();
        references.pop();
    }

    private void assign_stmt(){
        JSONObject ref;
        ref=references.peek();
        JSONObject obj=new JSONObject();
        ref.put("assign",obj);
        obj.put("IdentifierName",tokens.get(currentIndex).getValue());
        matchType(Constants.IDENTIFIER);
        match(":=");
        references.push(obj);

        exp();

        references.pop();
    }

    private void read_stmt(){
        match("read");
        JSONObject obj=references.peek();
        obj.put("readStatement",tokens.get(currentIndex).getValue());
        matchType(Constants.IDENTIFIER);
    }

    public void write_stmt(){
        match("write");
        JSONObject ref;
        ref=references.peek();
        JSONObject obj=new JSONObject();
        ref.put("writeStatement",obj);
        references.push(obj);

        exp();

        references.pop();
    }

    public void exp(){
        simple_exp();
        if(currentIndex==tokens.size())
            return;
        if(tokens.get(currentIndex).getValue().equals("<")){
            JSONObject ref;
            ref=references.peek();
            JSONObject temp=ref;
            while(temp.has("operation")){
                temp=temp.getJSONObject("operation");
            }
            ref=temp;
            String value;
            value=ref.getString("rightOperand");
            ref.remove("rightOperand");
            JSONObject obj=new JSONObject();
            ref.put("operation",obj);
            references.push(obj);
            match("<");
            obj.put("op","<");
            obj.put("leftOperand",value);
            simple_exp();

            references.pop();
        }
        else if(tokens.get(currentIndex).getValue().equals("=")){
            JSONObject ref;
            ref=references.peek();
            JSONObject temp=ref;
            while(temp.has("operation")){
                temp=temp.getJSONObject("operation");
            }
            ref=temp;
            String value;
            value=ref.getString("rightOperand");
            ref.remove("rightOperand");
            JSONObject obj=new JSONObject();
            ref.put("operation",obj);
            references.push(obj);
            match("=");
            obj.put("op","=");
            obj.put("leftOperand",value);
            simple_exp();

            references.pop();
        }
    }

    public void simple_exp(){
        term();
        if(currentIndex==tokens.size())
            return;
        while(tokens.get(currentIndex).getValue().equals("+") ||
                tokens.get(currentIndex).getValue().equals("-")){
            JSONObject ref;
            ref=references.peek();
            String value;
            String op;
            if(tokens.get(currentIndex).getValue().equals("+"))
                op = "+";
            else
                op="-";
            JSONObject temp=ref;
            while(temp.has("operation")){
                temp=temp.getJSONObject("operation");
            }
            ref=temp;
            value=ref.getString("rightOperand");
            ref.remove("rightOperand");
            JSONObject obj=new JSONObject();
            ref.put("operation",obj);
            references.push(obj);
            match(op);
            obj.put("op",op);
            obj.put("leftOperand",value);
            term();
            references.pop();
        }
    }

    public void term(){
        factor();
        if(currentIndex==tokens.size())
            return;
        while(tokens.get(currentIndex).getValue().equals("*") ||
                tokens.get(currentIndex).getValue().equals("/")){
            JSONObject ref;
            ref=references.peek();
            String value;
            String op;
            if(tokens.get(currentIndex).getValue().equals("*"))
                op = "*";
            else
                op="/";
            JSONObject temp=ref;
            while(temp.has("operation")){
                temp=temp.getJSONObject("operation");
            }
            ref=temp;
            value=ref.getString("rightOperand");
            ref.remove("rightOperand");
            JSONObject obj=new JSONObject();
            ref.put("operation",obj);
            references.push(obj);
            match(op);
            obj.put("op",op);
            obj.put("leftOperand",value);
            factor();
            references.pop();
        }
    }

    public void factor(){
        String s=tokens.get(currentIndex).getValue();
        if(s.equals("(")){
            match("(");
            exp();
            match(")");
        }
        else if(isInteger(s)){
            JSONObject ref=references.peek();
            ref.put("rightOperand",tokens.get(currentIndex).getValue());
            currentIndex++;
            isANumber=true;
        }
        else if(s.equals("-")){
            match("-");
            JSONObject ref=references.peek();
            ref.put("rightOperand",tokens.get(currentIndex).getValue());
            currentIndex++;
            isANumber=true;
        }
        else if(tokens.get(currentIndex).getType().equals(Constants.IDENTIFIER)){
            JSONObject ref=references.peek();
            ref.put("rightOperand",tokens.get(currentIndex).getValue());
            currentIndex++;
            isANumber=false;
        }
        else{
            AlertBox alertBox=new AlertBox();
            alertBox.display("Error","syntax error");
        }

    }

}