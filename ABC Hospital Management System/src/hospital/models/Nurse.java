
package hospital.models;

import java.time.LocalDate;

public class Nurse extends Staff{
    private String nursingLicense;
    private String qulification;

    
    public Nurse(){
        
    }

    public Nurse(
            String firstName, 
            String lastName, 
            char gender, 
            LocalDate dateOfBirth, 
            String phone, 
            String email, 
            String street, 
            String city, 
            String country,
            String staffId, 
            LocalDate employmentDate, 
            double salary, 
            Department department,
            String nursingLicense, 
            String qulification 
            
            
          
    ) {
        super(firstName,
                lastName,
                gender, 
                dateOfBirth, phone, 
                email, street, city, country,
                staffId, employmentDate, salary, department);
        this.nursingLicense = nursingLicense;
        this.qulification = qulification;
    }

    public String getNursingLicense() {
        return nursingLicense;
    }

    public void setNursingLicense(String nursingLicense) {
        this.nursingLicense = nursingLicense;
    }

    public String getQulification() {
        return qulification;
    }

    public void setQulification(String qulification) {
        this.qulification = qulification;
    }
    
    
    
}
