package cmu.arktweetnlp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.File;
//import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
//import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDeleteTest {
public static void main(String[] args) throws IOException{
	String text = "Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world Hello world";
    try {
      File file = new File("/Users/qianyizhong/Desktop/example.txt");
      BufferedWriter output = new BufferedWriter(new FileWriter(file));
      output.write(text);
      output.close();
    } catch ( IOException e ) {
       e.printStackTrace();
    }
  
    BufferedReader br = new BufferedReader(new FileReader("/Users/qianyizhong/Desktop/example.txt"));
    try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        System.out.println(sb);
        String everything = sb.toString();
        System.out.println(everything);
    } finally {
        br.close();
    }
	//File f = new File("/Users/qianyizhong/Desktop/pg1342.txt");
	//boolean success = f.delete();
}
}
