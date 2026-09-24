
package classwork5;


public class Student extends Person {
    
    public Student(String name, int age){
        this.name = name;
        this.age = age;
    }
    
    @Override
    void performDuty(){
        System.out.println("The student duty is to study");
    }
    
    @Override
    void displayDetails(){
        System.out.println("Student Name: " + name);
        System.out.println("Student Age: " + age);
    }
}
