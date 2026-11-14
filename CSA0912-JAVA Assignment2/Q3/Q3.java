import java.util.*;
public class Q3 {
 static class Broken {String id,name; Broken(String i,String n){id=i;name=n;} public boolean equals(Object o){return o instanceof Broken && id.equals(((Broken)o).id);} /* intentionally missing hashCode */}
 static final class Student {final String id,name; Student(String i,String n){id=i;name=n;} public boolean equals(Object o){return o instanceof Student && id.equals(((Student)o).id);} public int hashCode(){return Objects.hash(id);} }
 public static void main(String[]x){Set<Broken>b=new HashSet<>();b.add(new Broken("A1","Asha"));b.add(new Broken("A1","Asha again"));System.out.println("Broken hash contract size (may appear 1 or 2): "+b.size());Set<Student>s=new HashSet<>();s.add(new Student("A1","Asha"));s.add(new Student("A1","Asha again"));System.out.println("Corrected size: "+s.size());System.out.println("Contract test: "+(new Student("X","x").equals(new Student("X","y")) && new Student("X","x").hashCode()==new Student("X","y").hashCode()));}
}
