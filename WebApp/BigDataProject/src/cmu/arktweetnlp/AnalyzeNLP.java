package cmu.arktweetnlp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class AnalyzeNLP {
    private String words;
    private String tags;
    List<String> wordList; 
    List<String> tagList; 
	public  ArrayList<String> getNoun() throws IOException{
		String result = " ";
		HashSet set = new HashSet();
		set.add("want");
		set.add("wish");
		set.add("like");
		set.add("hope");
	    BufferedReader br1 = new BufferedReader(new FileReader("/Users/qianyizhong/Desktop/example1.txt"));
	    BufferedReader br2 = new BufferedReader(new FileReader("/Users/qianyizhong/Desktop/example2.txt"));
	    try {
	        StringBuilder sb1 = new StringBuilder();
	        String line = br1.readLine();

	        while (line != null) {
	            sb1.append(line);
	            sb1.append(System.lineSeparator());
	            line = br1.readLine();
	        }
	        words = sb1.toString();
	       
	    } finally {
	        br1.close();
	    }
	    try {
	        StringBuilder sb2 = new StringBuilder();
	        String line2 = br2.readLine();

	        while (line2 != null) {
	            sb2.append(line2);
	            sb2.append(System.lineSeparator());
	            line2 = br2.readLine();
	        }
	        tags = sb2.toString();
	       
	    } finally {
	        br2.close();
	    }
	    
	    System.out.println(words);
	    System.out.println(tags);
	    
	    wordList= new ArrayList<String>(Arrays.asList(words.split(" ")));
	    tagList= new ArrayList<String>(Arrays.asList(tags.split(" ")));
	    System.out.println(wordList);
	    System.out.println(tagList);
	    int length = wordList.size();
	    ArrayList<String> nouns = new ArrayList<String>();
	    String v1="";
	    String v2="";
	    int flag1=0;
	    int i = 0;
        for (i = 0; i< length;){
        	if (tagList.get(i).equals("V") && set.contains(wordList.get(i))) {
        		v1 = wordList.get(i);
        		flag1=i;
        		i++;
        		
        			while (i<length &&(tagList.get(i).equals("^")||tagList.get(i).equals("P")||tagList.get(i).equals("O")||tagList.get(i).equals("A")|| tagList.get(i).equals("D"))){
        				i++;
        			}
        			if(i<length&&tagList.get(i).equals("N")) {
        				while (i<length && tagList.get(i).equals("N") ){
        					nouns.add(wordList.get(i));
        					i++;
        				}
        				return nouns;
        			} else if (i<length) {
        				while (i<length) {
        					if (wordList.get(i).equals("buy")) {
        						i++;
        						while (i<length &&(tagList.get(i).equals("^")||tagList.get(i).equals("P")||tagList.get(i).equals("O") ||tagList.get(i).equals("A")|| tagList.get(i).equals("D"))){
        	        				i++;
        	        			}
        	        			if(i<length&&tagList.get(i).equals("N")) {
        	        				while (i<length && tagList.get(i).equals("N") ){
        	        					nouns.add(wordList.get(i));
        	        					i++;
        	        				}
        	        				return nouns;
        	        			} else {
        	        				i++;
        	        				continue;
        	        			}
        						
        					}
        					i++;
        				}
        				
        			}
        			
        			i= flag1;
        			i++;
        		
        	} else {
        		i++;
        	}
        	
        }
        System.out.println(i);
		return nouns;
	}
	
   public static void main(String args[]) throws IOException{
	   AnalyzeNLP a = new AnalyzeNLP();
	   ArrayList<String> str = a.getNoun();
	   System.out.println(str.toString());
   }
}
