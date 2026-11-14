import java.util.*;
public class Q4 {
 public static void main(String[]x){Map<String,Integer> h=new HashMap<>();Map<String,Integer> l=new LinkedHashMap<>();Map<String,Integer> t=new TreeMap<>();String[] k={"P3","P1","P2"};for(int i=0;i<k.length;i++){h.put(k[i],i+1);l.put(k[i],i+1);t.put(k[i],i+1);}System.out.println("HashMap (unspecified order): "+h);System.out.println("LinkedHashMap insertion order: "+l);System.out.println("TreeMap key order: "+t);System.out.println("missing key: "+h.get("P9"));}
}
