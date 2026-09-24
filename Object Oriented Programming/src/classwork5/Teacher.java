
package classwork5;


public class Teacher extends Person{
    
    public Teacher(String name, int age){
        super(name, age);
    }
    

    @Override
    void displayDetails(){
        System.out.println("Teacher Name: " + name);
        System.out.println("Teacher age: " + age);
    }
    
    @Override
    void performDuty(){
        System.out.println("The Teacher's duty is to teach");
    }
}
