import java.util.*;
public class Q7 {
 static volatile int sink;
 static long linear(List<Integer>x,int[] queries){long t=System.nanoTime();int z=0;for(int q:queries)z+=x.indexOf(q);sink=z;return System.nanoTime()-t;}
 static long indexed(Map<Integer,Integer>m,int[]queries){long t=System.nanoTime();int z=0;for(int q:queries)z+=m.getOrDefault(q,-1);sink=z;return System.nanoTime()-t;}
 public static void main(String[]a){int n=100000;List<Integer>x=new ArrayList<>();Map<Integer,Integer>m=new HashMap<>();for(int i=0;i<n;i++){x.add(i);m.put(i,i);}int[]q=new int[5000];Random r=new Random(7);for(int i=0;i<q.length;i++)q[i]=r.nextInt(n);long aa=0,bb=0;for(int k=0;k<5;k++){aa+=linear(x,q);bb+=indexed(m,q);}System.out.printf("same workload n=%d queries=%d linear_avg_ms=%.3f indexed_avg_ms=%.3f%n",n,q.length,aa/5e6,bb/5e6);System.out.println("Check: "+(x.get(q[0]).equals(q[0])&&m.get(q[0]).equals(q[0])));}
}
