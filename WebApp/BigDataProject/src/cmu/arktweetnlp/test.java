package cmu.arktweetnlp;

import java.util.ArrayList;
import java.util.Map;

public class test {
public static void main(String[] args) {

//        Map<String, String> content  = ItemSearch.getProductInfo("frozen", "Toys");
//        System.out.println(content.get("name"));
//        System.out.println(content.get("url"));
        ArrayList<String> list = ItemSearch.getProductInfo("skirt", "Toys");
        System.out.println(list.get(0));
        System.out.println(list.get(1));
    }
    
}
