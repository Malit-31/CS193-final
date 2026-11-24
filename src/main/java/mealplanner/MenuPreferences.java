package mealplanner;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Scanner;

public class MenuPreferences {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length > 2) {
            System.err.println("Usage: MenuPreferences [dining-court] [yyyy-MM-dd]");
            System.exit(2);
            return;
        }

        String diningCourt = args.length > 0 ? args[0] : "Earhart";
        String date = args.length > 1 ? args[1]
                : LocalDate.now(ZoneId.of("America/Indiana/Indianapolis")).toString();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Are you vegetarian? (yes/no): ");
        boolean vegetarian = scanner.nextLine().equalsIgnoreCase("yes");

        System.out.print("How many grams of protein do you eat per day? ");
        double protein = scanner.nextDouble();

        System.out.print("How many calories do you eat per day? ");
        double calories = scanner.nextDouble();

        System.out.println("Vegetarian: " + (vegetarian ? "yes" : "no"));
        System.out.println("Daily protein: " + protein + " grams");
        System.out.println("Daily calories: " + calories);

        System.out.println("Fetching " + diningCourt + " menu for " + date + "...");
        String menuJson = MenuClient.fetchMenuRawJson(diningCourt, date);
        String filteredMenu = VegetarianMenuFilter.filter(menuJson, vegetarian);
        System.out.print(MenuPrinter.formatFoodItems(filteredMenu));
    }
}
