import java.io.*;
import java.util.*;
public class Main{
    public static void main(String [ ] args) {
    	HashMap<Character, StringBuilder>  BNF = new HashMap<Character,StringBuilder>(); 
    	try {
        	File file = new File(args[0]);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
	    	while(line != null) {
	    		StringBuilder s = new StringBuilder(line.substring(3));
	    		char c = line.substring(0,1).charAt(0);
	    		BNF.put(c, s);
	    		line = br.readLine();
	    	}
			br.close();
	    	System.out.println(recursion(BNF, 'S'));
		} catch (FileNotFoundException e) {
			e.printStackTrace();	
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    	public static String recursion(HashMap<Character, StringBuilder> hmap, char ch){
    		String str = "(";
    		for(char i : hmap.keySet()) {
    			if(i == ch) {
    				char[] charList = hmap.get(ch).toString().toCharArray();
    				for(char c:charList) {
    					if(!(c == '|')) {
    						if(Character.isUpperCase(c)) {
    							str = str + recursion(hmap, c);
    						}else {
    							str = str + c;
    						}
    					}else {
    						str = str + "|";
    					}
    				}
    				return str + ")";
    			}
    		}
    		return "0";
    	}
}