package cloud;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.regex.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;


public class Twokenize {
    static Pattern Contractions = Pattern.compile("(?i)(\\w+)(n['’′]t|['’′]ve|['’′]ll|['’′]d|['’′]re|['’′]s|['’′]m)$");
    static Pattern Whitespace = Pattern.compile("[\\s\\p{Zs}]+");

    static String punctChars = "['\"“”‘’.?!…,:;]"; 
    static String punctSeq   = "['\"“”‘’]+|[.?!,…]+|[:;]+";	
    static String entity     = "&(?:amp|lt|gt|quot);";
 

    static String urlStart1  = "(?:https?://|\\bwww\\.)";
    static String commonTLDs = "(?:com|org|edu|gov|net|mil|aero|asia|biz|cat|coop|info|int|jobs|mobi|museum|name|pro|tel|travel|xxx)";
    static String ccTLDs	 = "(?:ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|" +
    "bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|" +
    "er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|" +
    "hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|" +
    "lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|" +
    "nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|" +
    "sl|sm|sn|so|sr|ss|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|" +
    "va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|za|zm|zw)";	
    static String urlStart2  = "\\b(?:[A-Za-z\\d-])+(?:\\.[A-Za-z0-9]+){0,3}\\." + "(?:"+commonTLDs+"|"+ccTLDs+")"+"(?:\\."+ccTLDs+")?(?=\\W|$)";
    static String urlBody    = "(?:[^\\.\\s<>][^\\s<>]*?)?";
    static String urlExtraCrapBeforeEnd = "(?:"+punctChars+"|"+entity+")+?";
    static String urlEnd     = "(?:\\.\\.+|[<>]|\\s|$)";
    public static String url        = "(?:"+urlStart1+"|"+urlStart2+")"+urlBody+"(?=(?:"+urlExtraCrapBeforeEnd+")?"+urlEnd+")";



    static String timeLike   = "\\d+(?::\\d+){1,2}";

    static String numberWithCommas = "(?:(?<!\\d)\\d{1,3},)+?\\d{3}" + "(?=(?:[^,\\d]|$))";
    static String numComb	 = "\\p{Sc}?\\d+(?:\\.\\d+)+%?";

    static String boundaryNotDot = "(?:$|\\s|[“\\u0022?!,:;]|" + entity + ")";
    static String aa1  = "(?:[A-Za-z]\\.){2,}(?=" + boundaryNotDot + ")";
    static String aa2  = "[^A-Za-z](?:[A-Za-z]\\.){1,}[A-Za-z](?=" + boundaryNotDot + ")";
    static String standardAbbreviations = "\\b(?:[Mm]r|[Mm]rs|[Mm]s|[Dd]r|[Ss]r|[Jj]r|[Rr]ep|[Ss]en|[Ss]t)\\.";
    static String arbitraryAbbrev = "(?:" + aa1 +"|"+ aa2 + "|" + standardAbbreviations + ")";
    static String separators  = "(?:--+|―|—|~|–|=)";
    static String decorations = "(?:[♫♪]+|[★☆]+|[♥❤♡]+|[\\u2639-\\u263b]+|[\\ue001-\\uebbb]+)";
    static String thingsThatSplitWords = "[^\\s\\.,?\"]";
    static String embeddedApostrophe = thingsThatSplitWords+"+['’′]" + thingsThatSplitWords + "*";
    
    public static String OR(String ... parts) {
        String prefix="(?:";
        StringBuilder sb = new StringBuilder();
        for (String s:parts){
            sb.append(prefix);
            prefix="|";
            sb.append(s);
        }
        sb.append(")");
        return sb.toString();
    }
    
  
    static String normalEyes = "(?iu)[:=]"; 
    static String wink = "[;]";
    static String noseArea = "(?:|-|[^a-zA-Z0-9 ])"; 
    static String happyMouths = "[D\\)\\]\\}]+";
    static String sadMouths = "[\\(\\[\\{]+";
    static String tongue = "[pPd3]+";
    static String otherMouths = "(?:[oO]+|[/\\\\]+|[vV]+|[Ss]+|[|]+)"; 

   

    static String bfLeft = "(♥|0|o|°|v|\\$|t|x|;|\\u0CA0|@|ʘ|•|・|◕|\\^|¬|\\*)";
    static String bfCenter = "(?:[\\.]|[_-]+)";
    static String bfRight = "\\2";
    static String s3 = "(?:--['\"])";
    static String s4 = "(?:<|&lt;|>|&gt;)[\\._-]+(?:<|&lt;|>|&gt;)";
    static String s5 = "(?:[.][_]+[.])";
    static String basicface = "(?:(?i)" +bfLeft+bfCenter+bfRight+ ")|" +s3+ "|" +s4+ "|" + s5;

    static String eeLeft = "[＼\\\\ƪԄ\\(（<>;ヽ\\-=~\\*]+";
    static String eeRight= "[\\-=\\);'\\u0022<>ʃ）/／ノﾉ丿╯σっµ~\\*]+";
    static String eeSymbol = "[^A-Za-z0-9\\s\\(\\)\\*:=-]";
    static String eastEmote = eeLeft + "(?:"+basicface+"|" +eeSymbol+")+" + eeRight;

    
    public static String emoticon = OR(
           
    		"(?:>|&gt;)?" + OR(normalEyes, wink) + OR(noseArea,"[Oo]") + 
            	OR(tongue+"(?=\\W|$|RT|rt|Rt)", otherMouths+"(?=\\W|$|RT|rt|Rt)", sadMouths, happyMouths),

         
            "(?<=(?: |^))" + OR(sadMouths,happyMouths,otherMouths) + noseArea + OR(normalEyes, wink) + "(?:<|&lt;)?",

            eastEmote.replaceFirst("2", "1"), basicface

    );

    static String Hearts = "(?:<+/?3+)+"; 

    static String Arrows = "(?:<*[-―—=]*>+|<+[-―—=]*>*)|\\p{InArrows}+";

    static String Hashtag = "#[a-zA-Z0-9_]+";  
  
    static String AtMention = "[@＠][a-zA-Z0-9_]+"; 

    static String Bound = "(?:\\W|^|$)";
    public static String Email = "(?<=" +Bound+ ")[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}(?=" +Bound+")";

    static Pattern Protected  = Pattern.compile(
            OR(
                    Hearts,
                    url,
                    Email,
                    timeLike,
                    numberWithCommas,
                    numComb,
                    emoticon,
                    Arrows,
                    entity,
                    punctSeq,
                    arbitraryAbbrev,
                    separators,
                    decorations,
                    embeddedApostrophe,
                    Hashtag,  
                    AtMention
            ));

   
    static String edgePunctChars    = "'\"“”‘’«»{}\\(\\)\\[\\]\\*&"; 
    static String edgePunct    = "[" + edgePunctChars + "]";
    static String notEdgePunct = "[a-zA-Z0-9]"; 
    static String offEdge = "(^|$|:|;|\\s|\\.|,)";  
    static Pattern EdgePunctLeft  = Pattern.compile(offEdge + "("+edgePunct+"+)("+notEdgePunct+")");
    static Pattern EdgePunctRight = Pattern.compile("("+notEdgePunct+")("+edgePunct+"+)" + offEdge);

    public static String splitEdgePunct (String input) {
        Matcher m1 = EdgePunctLeft.matcher(input);
        input = m1.replaceAll("$1$2 $3");
        m1 = EdgePunctRight.matcher(input);
        input = m1.replaceAll("$1 $2$3");
        return input;
    }
    
    private static class Pair<T1, T2> {
        public T1 first;
        public T2 second;
        public Pair(T1 x, T2 y) { first=x; second=y; }
    }

  
    private static List<String> simpleTokenize (String text) {

        String splitPunctText = splitEdgePunct(text);

        int textLength = splitPunctText.length();
       
        Matcher matches = Protected.matcher(splitPunctText);
      
        List<List<String>> bads = new ArrayList<List<String>>();	
        List<Pair<Integer,Integer>> badSpans = new ArrayList<Pair<Integer,Integer>>();
        while(matches.find()){
           
            if (matches.start() != matches.end()){ 
                List<String> bad = new ArrayList<String>(1);
                bad.add(splitPunctText.substring(matches.start(),matches.end()));
                bads.add(bad);
                badSpans.add(new Pair<Integer, Integer>(matches.start(),matches.end()));
            }
        }

        List<Integer> indices = new ArrayList<Integer>(2+2*badSpans.size());
        indices.add(0);
        for(Pair<Integer,Integer> p:badSpans){
            indices.add(p.first);
            indices.add(p.second);
        }
        indices.add(textLength);

      
        List<List<String>> splitGoods = new ArrayList<List<String>>(indices.size()/2);
        for (int i=0; i<indices.size(); i+=2) {
            String goodstr = splitPunctText.substring(indices.get(i),indices.get(i+1));
            List<String> splitstr = Arrays.asList(goodstr.trim().split(" "));
            splitGoods.add(splitstr);
        }

     
        List<String> zippedStr= new ArrayList<String>();
        int i;
        for(i=0; i < bads.size(); i++) {
            zippedStr = addAllnonempty(zippedStr,splitGoods.get(i));
            zippedStr = addAllnonempty(zippedStr,bads.get(i));
        }
        zippedStr = addAllnonempty(zippedStr,splitGoods.get(i));

        
        return zippedStr;
    }  

    private static List<String> addAllnonempty(List<String> master, List<String> smaller){
        for (String s : smaller){
            String strim = s.trim();
            if (strim.length() > 0)
                master.add(strim);
        }
        return master;
    }

    public static String squeezeWhitespace (String input){
        return Whitespace.matcher(input).replaceAll(" ").trim();
    }

  
    private static List<String> splitToken (String token) {

        Matcher m = Contractions.matcher(token);
        if (m.find()){
        	String[] contract = {m.group(1), m.group(2)};
        	return Arrays.asList(contract);
        }
        String[] contract = {token};
        return Arrays.asList(contract);
    }

    public static List<String> tokenize(String text){
        return simpleTokenize(squeezeWhitespace(text));
    }



    public static String normalizeTextForTagger(String text) {
    	text = text.replaceAll("&amp;", "&");
    	text = StringEscapeUtils.unescapeHtml(text);
    	return text;
    }

    
    public static List<String> tokenizeRawTweetText(String text) {
        List<String> tokens = tokenize(normalizeTextForTagger(text));
        return tokens;
    }

  
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        PrintStream output = new PrintStream(System.out, true, "UTF-8");
    	String line;
    	while ( (line = input.readLine()) != null) {
    		List<String> toks = tokenizeRawTweetText(line);
    		for (int i=0; i<toks.size(); i++) {
    			output.print(toks.get(i));
    			if (i < toks.size()-1) {
    				output.print(" ");
    			}
    		}
    		output.print("\n");
    	}
    }
    
}
