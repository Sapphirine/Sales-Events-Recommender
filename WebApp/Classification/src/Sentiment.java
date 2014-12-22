import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
 
public class Sentiment {
	private static HashSet<String> negativeSet;
	private static HashSet<String> positiveSet;
	public static void initialize() throws IOException{
		    BufferedReader br1 = new BufferedReader(new FileReader("/Users/qianyizhong/Desktop/negative.txt"));
		    BufferedReader br2 = new BufferedReader(new FileReader("/Users/qianyizhong/Desktop/positive.txt"));
		    String negativeWords="";
		    String positiveWords="";
		    negativeSet = new HashSet<String>();
		    positiveSet = new HashSet<String>();
		    try {
		        StringBuilder sb1 = new StringBuilder();
		        String line = br1.readLine();
                
		        while (line != null) {
		            sb1.append(line);
		            sb1.append(System.lineSeparator());
		            line = br1.readLine();
		        }
		        negativeWords = sb1.toString();
		       
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
		        positiveWords = sb2.toString();
		       
		    } finally {
		        br2.close();
		    }
		    
		  
		    
		    ArrayList<String> negativeWordList= new ArrayList<String>(Arrays.asList(negativeWords.split("\n")));
		    ArrayList<String> positiveWordList= new ArrayList<String>(Arrays.asList(positiveWords.split("\n")));
		    System.out.println(negativeWordList);
		    for (int i = 0; i < negativeWordList.size(); i++) {
		    	 negativeSet.add(negativeWordList.get(i));
		    //	 System.out.println("$$$$$$$$$$$$$$$$$"+negativeWordList);
		    }
		    for (int i = 0; i < positiveWordList.size(); i++) {
		    	 positiveSet.add(positiveWordList.get(i));
		    }
		    System.out.println("HashSet is :"+  positiveSet);
		   
	}
   
    public static class Map extends Mapper<LongWritable, Text, Text, DoubleWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        private HashMap<String,Integer> hashMap = new HashMap<String, Integer>();
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
             hashMap.clear();
        	//System.out.println(set);
            Double negativeCount=0.0;
            Double positiveCount=0.0;
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
            	
         	StringBuilder valueBuilder = new StringBuilder();
        	String k=tokenizer.nextToken();
//        	if(hashMap.containsKey(k)){
//        		hashMap.put(k, hashMap.get(k)+1);
//        	} else {
//       		hashMap.put(k,1);
//        	}
        	System.out.println(k);
        	if (negativeSet.contains(k)){
        		negativeCount = negativeCount + 1;
        	}
        	if (positiveSet.contains(k)){
        		positiveCount = positiveCount + 1;
        		System.out.println("@@@@@@@@@@@@@works@@@@@@@@@@@@@");
        	}
        	
        	// context.write(word, new IntWritable());
          //  context.write(word, one);
        } 
            
            System.out.println(positiveCount);
            if (negativeCount == 0){
            	negativeCount = 0.001;
            }
            if (positiveCount == 0){
            	positiveCount = 0.001;
            }
            
            System.out.println(positiveCount);
            System.out.println(negativeCount);
            double rating = ((double)positiveCount - (double)negativeCount) /((double)positiveCount + (double)negativeCount);
        	//word.set(fileName+"#"+k);
            word.set("teddy bear");
           // word.set(tokenizer.nextToken());
        	//context.write(word, one);
            
            context.write(word, new DoubleWritable(rating));
//            String fileName = context.getInputSplit().toString();
//     
//            
//            System.out.println("key is !!!!!!!!!!!!! :"+context.getInputSplit());
//            StringTokenizer tokenizer = new StringTokenizer(line);
//            while (tokenizer.hasMoreTokens()) {
//            	//StringBuilder valueBuilder = new StringBuilder();
//            	String k=tokenizer.nextToken();
//            	if(hashMap.containsKey(k)){
//            		hashMap.put(k, hashMap.get(k)+1);
//            	} else {
//            		hashMap.put(k,1);
//            	}
//            	word.set(fileName+"#"+k);
            //	 word.set(k);
              //  word.set(tokenizer.nextToken());
            //	context.write(word, one);
            	// context.write(word, new IntWritable(40));
              //  context.write(word, one);
//            }
          //  System.out.println("Hope it runs well!!!!!!!!!!!!!!!!");
          //  System.out.println(hashMap);
        }
    } 
 
    public static class Reduce extends Reducer<Text, IntWritable, Text, DoubleWritable> {
        public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (DoubleWritable val : values) {
                sum += val.get();
            }
            System.out.println(key);
            System.out.println(sum);
            context.write(key, new DoubleWritable(sum));
            
        }
    }
 
    public static void main(String[] args) throws Exception {
    	 initialize();
        Configuration conf = new Configuration();
       
        Job job = new Job(conf, "WordCount");
        job.setJarByClass(WordCount.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
 
        job.setMapperClass(Map.class);
        job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);
 
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
 
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
 
        job.waitForCompletion(true);
    }        
}
