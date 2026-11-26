package mealplanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Locale;

public final class MenuPrinter {
    private MenuPrinter() {
    }

    public static String formatFoodItems(String menuJson) {
        JsonObject response = JsonParser.parseString(menuJson).getAsJsonObject();
        JsonObject court = response.getAsJsonObject("data").getAsJsonObject("diningCourtByName");
        JsonArray meals = court.getAsJsonObject("dailyMenu").getAsJsonArray("meals");
        StringBuilder output = new StringBuilder();

        for (JsonElement mealValue : meals) {
            JsonObject meal = mealValue.getAsJsonObject();
            String mealName = meal.get("name").getAsString();
            JsonArray stations = meal.getAsJsonArray("stations");
            for (JsonElement stationValue : stations) {
                JsonObject station = stationValue.getAsJsonObject();
                String stationName = station.get("name").getAsString();
                JsonArray items = station.getAsJsonArray("items");
                for (JsonElement itemValue : items) {
                    JsonObject entry = itemValue.getAsJsonObject();
                    JsonObject item = entry.getAsJsonObject("item");
                    JsonElement specialName = entry.get("specialName");
                    String itemName = specialName != null && !specialName.isJsonNull()
                            && !specialName.getAsString().isBlank()
                            ? specialName.getAsString()
                            : item.get("name").getAsString();
                    output.append(mealName)
                            .append(" - ")
                            .append(stationName)
                            .append(": ")
                            .append(itemName)
                            .append(" (")
                            .append(formatNutrition(item))
                            .append(")")
                            .append(System.lineSeparator());
                }
            }
        }

        return output.length() == 0 ? "No menu items found." + System.lineSeparator()
                : output.toString();
    }

    private static String formatNutrition(JsonObject item) {
        JsonElement factsValue = item.get("nutritionFacts");
        if (factsValue == null || !factsValue.isJsonArray()) {
            return "Calories: unavailable, Protein: unavailable";
        }

        String calories = "unavailable";
        String protein = "unavailable";
        String servingSize = null;
        for (JsonElement factValue : factsValue.getAsJsonArray()) {
            JsonObject fact = factValue.getAsJsonObject();
            String name = fact.get("name").getAsString();
            JsonElement value = fact.get("value");
            if (name.equalsIgnoreCase("Calories") && value != null && value.isJsonPrimitive()) {
                calories = formatAmount(value.getAsDouble()) + " kcal";
            } else if (name.equalsIgnoreCase("Protein")
                    && value != null && value.isJsonPrimitive()) {
                protein = formatAmount(value.getAsDouble()) + " g";
            } else if (name.equalsIgnoreCase("Serving Size")) {
                JsonElement label = fact.get("label");
                if (label != null && label.isJsonPrimitive() && !label.getAsString().isBlank()) {
                    servingSize = label.getAsString();
                }
            }
        }

        String nutrition = "Calories: " + calories + ", Protein: " + protein;
        return servingSize == null ? nutrition : nutrition + " per " + servingSize;
    }

    private static String formatAmount(double amount) {
        return String.format(Locale.ROOT, "%.1f", amount);
    }
}
