import java.util.*;
public class Q6 {
 static class Patient {String name;int severity,arrival;Patient(String n,int s,int a){name=n;severity=s;arrival=a;}public String toString(){return name+"(S"+severity+")";}}
 public static void main(String[]a){Comparator<Patient> order=Comparator.comparingInt((Patient p)->p.severity).thenComparingInt(p->p.arrival);PriorityQueue<Patient> q=new PriorityQueue<>(order);String[] names={"Ravi","Meena","John","Kumar","Lata","Asha"};int[] sev={3,1,3,1,2,5};for(int i=0;i<names.length;i++)q.add(new Patient(names[i],sev[i],i));while(!q.isEmpty())System.out.print(q.remove()+" ");System.out.println();}
}
