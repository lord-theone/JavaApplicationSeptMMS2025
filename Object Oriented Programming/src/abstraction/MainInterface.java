package abstraction;


public class MainInterface {
    public static void main(String[] args){
        SmartPhone smartPhone = new SmartPhone();
        
        Nokia3310 nokia = new Nokia3310();
        
        System.out.println("\nSmartphone interface\n");
        smartPhone.makeCall();
        smartPhone.playGame();
        smartPhone.playMusic();
        smartPhone.playMovie();
        smartPhone.takePicture();
        smartPhone.connectToWiFi();
        
        System.out.println("\nNokia3310 interface\n");
        nokia.makeCall();
        nokia.playGame();
        nokia.playMusic();
    }
}
