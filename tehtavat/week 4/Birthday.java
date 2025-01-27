import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Birthday {
    public static void main(String[] args) {

        String birthdateStr = System.getenv("BIRTHDATE");
        if (birthdateStr == null || birthdateStr.isEmpty()) {
            System.out.println("Birthdate is not set, please setup environment variable BIRTHDATE");
            return;
        }

        LocalDate birthdate = LocalDate.parse(birthdateStr);
        LocalDate today = LocalDate.now();

        if (birthdate.isAfter(today)) {
            System.out.println("Your Birthdate is in the future.");
            return;
        }

        long days = ChronoUnit.DAYS.between(birthdate, today);
        LocalDate nextBirthday = birthdate.withYear(today.getYear());
        LocalDate latestBirthday = nextBirthday.minusYears(1);

        if(birthdate.getYear() == today.getYear()){
            latestBirthday = birthdate.withYear(today.getYear());
        }

        long daysFromBirthday = ChronoUnit.DAYS.between(latestBirthday, today);

        if (nextBirthday.isBefore(today) || nextBirthday.isEqual(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }

        long daysUntilNextBirthday = ChronoUnit.DAYS.between(today, nextBirthday);

        if (today.getMonth() == birthdate.getMonth() && today.getDayOfMonth() == birthdate.getDayOfMonth() && today.getYear() >= birthdate.getYear()){
            System.out.println("Happy Birthday!");
            System.out.println("You are " + days + " days old");
            if (days % 1000 == 0) {
                System.out.println("Nice round number");
            }
            return;
        }

        System.out.println("You are " + days + " days old");
        if (days % 1000 == 0) {
            System.out.println("Nice round number");
        }
        System.out.println("Your next birthday is " + daysUntilNextBirthday + " days away");
        System.out.println("Your last birthday was " + daysFromBirthday + " days ago");
    }
}