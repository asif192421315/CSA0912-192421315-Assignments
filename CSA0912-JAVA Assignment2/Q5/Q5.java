import java.util.*;
public class Q5 {
 static class Song implements Comparable<Song>{String title;int duration;Song(String t,int d){title=t;duration=d;}public int compareTo(Song s){int c=Integer.compare(duration,s.duration);return c!=0?c:title.compareTo(s.title);}public String toString(){return title+"("+duration+")";}}
 static <T> void printAll(List<T> xs){for(T x:xs)System.out.print(x+" ");System.out.println();}
 public static void main(String[]a){List<Song>s=new ArrayList<>(Arrays.asList(new Song("Zen",180),new Song("Amber",200),new Song("Blue",180)));Collections.sort(s);System.out.println("Natural duration/title order:");printAll(s);s.sort(Comparator.comparing((Song x)->x.title).reversed());System.out.println("Title descending:");printAll(s);}
}
