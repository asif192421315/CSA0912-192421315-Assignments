import java.util.*;
public class Q2 {
 static volatile long sink;
 static long run(List<Integer> x,int n,int op){for(int i=0;i<n;i++)x.add(i);long t=System.nanoTime();if(op==0){for(int i=0;i<n;i++)x.add(i);}else if(op==1){for(int i=0;i<n;i++)x.add(0,i);}else if(op==2){for(int i=0;i<n;i++)x.add(x.size()/2,i);}else if(op==3){for(int i=0;i<n;i++)x.remove(0);}else if(op==4){for(int i=0;i<n;i++)x.remove(x.size()/2);}else if(op==5){for(int i=0;i<n;i++)sink+=x.get(x.size()/2);}else{for(Integer v:x)sink+=v;}return System.nanoTime()-t;}
 public static void main(String[]z){int[] sizes={500,2000,5000};String[] ops={"addEnd","insertFront","insertMiddle","removeFront","removeMiddle","getKth","iterate"};for(int n:sizes){System.out.println("n="+n);for(String k:new String[]{"ArrayList","LinkedList"})for(int op=0;op<ops.length;op++){long s=0;for(int r=0;r<5;r++)s+=run(k.equals("ArrayList")?new ArrayList<>():new LinkedList<>(),n,op);System.out.printf("%s %s avg_ms=%.3f%n",k,ops[op],s/5e6);}}ArrayDeque<Integer> q=new ArrayDeque<>();q.addFirst(2);q.addLast(3);System.out.println("deque ends: "+q);}
}
