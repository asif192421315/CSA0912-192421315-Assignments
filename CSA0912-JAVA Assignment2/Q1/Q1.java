import java.util.*;
public class Q1 {
  static String plus(String[] f){ String s=""; for(String x:f) s += x; return s; }
  static String builder(String[] f){ StringBuilder b=new StringBuilder(); for(String x:f)b.append(x); return b.toString(); }
  static String buffer(String[] f){ StringBuffer b=new StringBuffer(); for(String x:f)b.append(x); return b.toString(); }
  static volatile int sink;
  static long measure(int mode,int tickets,int fields){ String[] f=new String[fields]; Arrays.fill(f,"field|"); long t=System.nanoTime(); int h=0; for(int i=0;i<tickets;i++){String s=mode==0?plus(f):mode==1?builder(f):buffer(f); h+=s.length();} sink=h; return System.nanoTime()-t; }
  public static void main(String[] a){ int[][] w={{1,6},{1000,50},{10000,50}}; String[] n={"short","medium","large"}; for(int k=0;k<w.length;k++){System.out.println(n[k]+" tickets="+w[k][0]+" fields="+w[k][1]); for(int m=0;m<3;m++){long sum=0;for(int r=0;r<5;r++)sum+=measure(m,w[k][0],w[k][1]);System.out.printf("%s avg_ms=%.3f%n",new String[]{"String+","StringBuilder","StringBuffer"}[m],sum/5e6);}} }
}
