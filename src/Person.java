import java.util.Random;
import java.util.Scanner;

public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
	/*que1.Class defines equals() but not hashCode():
	 * */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Person person = (Person) obj;
        return age == person.age && name.equals(person.name);
    }
//    @Override
//    public int hashCode() {
//    	// TODO Auto-generated method stub
//    	return super.hashCode();
//    }

    public static void main(String[] args) {
    	
    	/*que 4:Only called constructer and rest methods are not called
    	 * */
        Person p1 = new Person("John", 30);
        Person p2 = new Person("John", 30);
        p1.getName();
        /*que3 :Unused Variable
         * */
        boolean condition = false; // introducing a variable with a constant value

        /* que2 :Conditon has no effect due to the variable type
         * */
        Random random = new Random();
        
        if(random.nextInt()== random.nextInt()) {
        	
        }
        /*Created Variables with constant bool to check
         * 
         * */
        if (true) { // this condition always produces the same result
            if (p1.equals(p2)) {
                System.out.println("Hello");
            }
        }
      
        /*Created Variables with constant experssion to check
         * 
         * */
        if (1==1) { // this condition always produces the same result
            if (p1.equals(p2)) {
                System.out.println("Hello");
            }
        }
        /*Created Variables without constant experssion to check
         * 
         * */
        int a;
        int b;
        Scanner scanner = new Scanner(System.in);
        a = scanner.nextInt();
        b = scanner.nextInt();
        if (a==b) { // this condition always produces the same result
            if (p1.equals(p2)) {
                System.out.println("Hello");
            }
        }
        /*que 5:This method contains a switch statement where 
         * one case branch will fall through to
         * the next case. Usually, you need to end this 
         * case with a break or return.
         * */
        String var ="Aksh";
        
        	switch (var) {
			case "Aksh":
				break;
			case "asda":
					break;

			default:
				break;
			}
        
    }
}
