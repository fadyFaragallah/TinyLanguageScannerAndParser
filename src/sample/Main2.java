package sample;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main2 {
    public static void main(String[] args){
        Map obj = new LinkedHashMap();
        obj.put("b", "foo1");
        obj.put("d", "sssss");
        obj.put("w","sss");
        obj.put("a", "");
        obj.put("a", "foo2");
        obj.put("v", "foo3");
        obj.put("g", "foo4");
        obj.put("t", "foo5");
        obj.put("e", "xxxxxx");
        JSONObject json = new JSONObject(obj);
        System.out.println(json);
    }
}
