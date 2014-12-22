import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import au.com.bytecode.opencsv.CSVWriter;
 
public class ClassWordCount {
	private static ArrayList<String> dic;
	private static HashMap<String,Integer> dicMap;
	private static int count = 0;
	private static int sumAll = 0;
	public static List<String[]> data;
	public static CSVWriter writer;
//	private static HashSet<String> negativeSet;
//	private static HashSet<String> positiveSet;
	public static void initialize() throws IOException{
		    BufferedReader br = new BufferedReader(new FileReader("/Users/qianyizhong/Downloads/dictionary.txt"));
		    dicMap = new HashMap<String,Integer>();
		    data = new ArrayList<String[]>();
		    //BufferedReader br2 = new BufferedReader(new FileReader("/Users/qianyizhong/Desktop/positive.txt"));
		    String dicWords="";
//		    String positiveWords="";
//		    negativeSet = new HashSet<String>();
//		    positiveSet = new HashSet<String>();
		    dic =  new ArrayList<String>();
		    writer = new CSVWriter(new FileWriter("/Users/qianyizhong/Documents/beauti.csv"));
		    try {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();
                
		        while (line != null) {
		            sb.append(line);
		            sb.append(System.lineSeparator());
		            line = br.readLine();
		        }
		        dicWords = sb.toString();
		       
		    } finally {
		        br.close();
		    }
//		    try {
//		        StringBuilder sb2 = new StringBuilder();
//		        String line2 = br2.readLine();
//
//		        while (line2 != null) {
//		            sb2.append(line2);
//		            sb2.append(System.lineSeparator());
//		            line2 = br2.readLine();
//		        }
//		        positiveWords = sb2.toString();
//		       
//		    } finally {
//		        br2.close();
//		    }
//		    
		  
		    
		    dic= new ArrayList<String>(Arrays.asList(dicWords.split("\n")));
		    System.out.println(dic);
		    
		    for (int i = 0; i < dic.size(); i++) {
		    	 dicMap.put(dic.get(i),0);
		    //	 System.out.println("$$$$$$$$$$$$$$$$$"+negativeWordList);
		    }

		//    ArrayList<String> positiveWordList= new ArrayList<String>(Arrays.asList(positiveWords.split("\n")));

		   
	}
   
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        private HashMap<String,Integer> hashMap = new HashMap<String, Integer>();
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
             hashMap.clear();
        	//System.out.println(set);
//            Double negativeCount=0.0;
//            Double positiveCount=0.0;
             String line = value.toString();
             String stringToBeTokenized = line.replace('#', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('?', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('<', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('>', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('[', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace(']', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('{', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('}', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('@', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('(', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace(')', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace(',', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('!', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('&', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace(':', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('?', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('/', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('\\', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('\'', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('"', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('+', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('=', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('_', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('-', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('*', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('$', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('^', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('%', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('.', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace(';', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('1', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('2', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('3', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('4', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('5', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('6', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('7', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('8', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('9', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('0', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('\n', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('\t', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('`', ' ');
     		stringToBeTokenized = stringToBeTokenized.replace('~', ' ');

     		String stopwords="	a about above above across after afterwards again against all almost alone along already "
     				+ "also although always am among amongst amoungst amount  an and another any anyhow anyone"
     				+ " anything anyway anywhere are around as  at back be became because become becomes becoming"
     				+ " been before beforehand behind being below beside besides between beyond both bottom but by "
     				+ "call can cannot cant co con could couldnt cry de describe detail do done down due during each "
     				+ "eg eight either eleven else elsewhere empty enough etc even ever every everyone everything"
     				+ " everywhere except few fifteen fify fill find fire first five for former formerly forty found "
     				+ "four from front full further get give go had has hasnt have he hence her here hereafter hereby "
     				+ "herein hereupon hers herself him himself his how however hundred ie if in inc indeed interest into "
     				+ "is it its itself keep last latter latterly least less ltd made many may me meanwhile might mill mine"
     				+ " more moreover most mostly move much must my myself name namely neither never nevertheless next new nine"
     				+ " no nobody none noone nor not nothing now nowhere of off often on once one only onto or other others "
     				+ "otherwise our ours ourselves out over own part per perhaps please put rather re same see seem seemed seeming "
     				+ "seems serious several she should show side since sincere six sixty so some somehow someone something sometime"
     				+ " sometimes somewhere still such system take ten than that the their them themselves then thence there"
     				+ " thereafter thereby therefore therein thereupon these they thickv thin third this those though three through"
     				+ " throughout thru thus to together too top toward towards twelve twenty two un under until up upon us very via"
     				+ " was we well were what whatever when whence whenever where whereafter whereas whereby wherein whereupon"
     				+ " wherever whether which while whither who whoever whole whom whose why will with within without would yet"
     				+ " you your yours yourself yourselves the 1 2 3 4 5 6 7 8 9 0 00 000 0000 00000 000000 0000000 \\ / j ya di tu " 
     				+ "tr da q http apa uu ? fa ik nya";

     		ArrayList<String> wordset = new ArrayList<String>();
     		String[] tokens = stringToBeTokenized.split(" |-|,|\"|\\.|\\(|\\)|\\||&|\\?|:\n\t");
             
             
          //   System.out.println("line is "+ line);
//             for (String str:dic){
////     			System.out.println("$$$$$$$$$$$$$");
////     			System.out.println(str);
//            	 word.set(str);
//            	 context.write(word, new IntWritable(0));
            	 for (int i = 0; i < tokens.length; i++) {
         			String token = tokens[i].trim().toLowerCase();
//         			System.out.println("##############");
//         			System.out.println(token);
//         			System.out.println("$$$$$$$$$$$$$");
//         			System.out.println(str);
         			if (dicMap.containsKey(token)){
         				dicMap.put(token, dicMap.get(token)+1);
               		    word.set(token);
//               		    System.out.println("!!!!!!!!!!!!!!!");
//                 		 System.out.println(str);
                 		 context.write(word, one);
                 	 }  
                 		
                 	 
         			}
            	 
            //	 System.out.println(str);
//            	 if(line.contains(str)){
//            		 word.set(str);
//            	//	 System.out.println(str);
//            		 context.write(word, one);
//            	 }
             }
          
          
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
 
    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
//            System.out.println(key);
//            System.out.println(sum);
            sumAll = sumAll+sum;
            context.write(key, new IntWritable(sum));
            
        }
    }
 
    public static void main(String[] args) throws Exception {
    	 initialize();
        Configuration conf = new Configuration();
       
        Job job = new Job(conf, "WordCount");
        job.setJarByClass(WordCount.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
 
        job.setMapperClass(Map.class);
        job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);
 
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
 
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
       
        job.waitForCompletion(true);
        		System.out.println("sum is"+ sumAll);
        Iterator it = dicMap.entrySet().iterator();
        while (it.hasNext()) {
        	Entry pairs = (Entry)it.next();
         //   System.out.println(pairs.getKey() + " = " + pairs.getValue());
        	Double freq = ((double)(Integer.parseInt(pairs.getValue().toString())))/((double)(sumAll));
            //data.add(new String[]{(String) pairs.getKey(),pairs.getValue().toString()});
        	data.add(new String[]{(String) pairs.getKey(),freq.toString()});
            it.remove(); // avoids a ConcurrentModificationException
        }
        writer.writeAll(data);
    	writer.close();
    }        
}
