package aggregation;

import java.util.ArrayList;
import java.util.List;

public class School {
    private String schoolName;
    private List<Student> student;
    
    public School(String schoolName, ArrayList<Student> student){
        this.schoolName = schoolName;
        this.student = student;
    }
    
    public void displayStudentDetails(){
        System.out.printf("School Name:", schoolName);
        
        for(Student students: student){
            students.displayStudentDetails();
        }
    }
    
}
