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
 
public class WordCount {
   
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        private HashMap<String,Integer> hashMap = new HashMap<String, Integer>();
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
             hashMap.clear();
        	//System.out.println(set);
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

    		for (int i = 0; i < tokens.length; i++) {
    			String token = tokens[i].trim().toLowerCase();
    			if (stopwords.contains(token)) {

    			} else {
    				wordset.add(token);
    		     	word.set(token);
    		    	context.write(word, one);
    			}
    		}
            
         //   String fileName = context.getInputSplit().toString();
     
            
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
//           // 	word.set(fileName+"#"+k);
//                word.set(k);
//              //  word.set(tokenizer.nextToken());
//            	context.write(word, one);
//            	// context.write(word, new IntWritable(40));
//              //  context.write(word, one);
//            }
//            System.out.println("Hope it runs well!!!!!!!!!!!!!!!!");
//            System.out.println(hashMap);
        }
    } 
 
    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
            
        }
    }
 
    public static void main(String[] args) throws Exception {
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
    }        
}
