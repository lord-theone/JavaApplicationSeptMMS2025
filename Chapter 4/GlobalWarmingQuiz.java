
import java.util.Scanner;

public class GlobalWarmingQuiz {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] questions = {
            "1. What gas is most responsible for the greenhouse effect?\n" +
            "   1) Oxygen  2) Carbon Dioxide  3) Nitrogen  4) Hydrogen",

            "2. Which year was identified as the hottest on record (as of 2023)?\n" +
            "   1) 2005  2) 2012  3) 2016  4) 2023",

            "3. The IPCC stands for:\n" +
            "   1) International Panel on Climate Control\n" +
            "   2) Intergovernmental Panel on Climate Change\n" +
            "   3) International Protocol on Carbon Contributions\n" +
            "   4) Independent Panel on Climate Conditions",

            "4. Which human activity contributes most to CO2 emissions globally?\n" +
            "   1) Farming  2) Transportation  3) Burning fossil fuels  4) Deforestation",

            "5. Some scientists argue global warming data is uncertain because:\n" +
            "   1) Satellites cannot measure temperature\n" +
            "   2) Historical climate records are incomplete\n" +
            "   3) Oceans absorb all CO2\n" +
            "   4) Solar cycles fully explain temperature changes"
        };

        int[] correctAnswers = {2, 4, 2, 3, 2};
        int score = 0;

        for (int i = 0; i < questions.length; i++) {
            System.out.println(questions[i]);
            System.out.print("Your answer: ");
            int answer = scanner.nextInt();
            if (answer == correctAnswers[i]) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.printf("Wrong. Correct answer: %d%n", correctAnswers[i]);
            }
            System.out.println();
        }

        System.out.printf("%nYour score: %d/5%n", score);
        if (score == 5)
            System.out.println("Excellent!");
        else if (score == 4)
            System.out.println("Very good!");
        else
            System.out.println("Time to brush up on your knowledge of global warming!");

        scanner.close();
    }
}
