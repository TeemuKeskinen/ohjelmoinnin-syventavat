import java.util.Scanner;

public class NumberGuessingGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int min = 1;
        int max = 100;
        int numberToGuess = (int) (Math.random() * max) + min;
        int maxnumberOfTries = 7;
        int numberOfTries = 0;
        boolean hasGuessedCorrectly = false;

        while (numberOfTries < maxnumberOfTries && !hasGuessedCorrectly) {
            System.out.println("Guess the number between " + min + " and " + max + ":");
            int userGuess = scanner.nextInt();
            numberOfTries++;

            if (userGuess == numberToGuess) {
                hasGuessedCorrectly = true;
                System.out.println("Correct number.");
            } else if (userGuess < numberToGuess) {
                System.out.println("The number is higher.");
            } else {
                System.out.println("The number is lower.");
            }
        }

        if (!hasGuessedCorrectly) {
            System.out.println("The correct number was " + numberToGuess);
        }

        scanner.close();
    }
}