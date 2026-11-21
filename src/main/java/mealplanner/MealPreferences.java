package mealplanner;

import java.util.Scanner;

public class MealPreferences {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Are you vegetarian? (yes/no): ");
        String vegetarian = scanner.nextLine();

        System.out.print("How many grams of protein do you eat per day? ");
        double protein = scanner.nextDouble();

        System.out.print("How many calories do you eat per day? ");
        double calories = scanner.nextDouble();

        System.out.println("Vegetarian: " + vegetarian);
        System.out.println("Daily protein: " + protein + " grams");
        System.out.println("Daily calories: " + calories);
    }
}
