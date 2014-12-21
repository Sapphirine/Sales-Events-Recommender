package Util;

import java.util.ArrayList;
import java.util.Map;

public class test {
public static void main(String[] args) {

        ArrayList<String> list = ItemSearch.getProductInfo("frozen", "Toys");
        System.out.println(list.get(0));
        System.out.println(list.get(1));
    }
    
}
