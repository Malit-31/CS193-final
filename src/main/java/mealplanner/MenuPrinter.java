package mealplanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
                            .append(System.lineSeparator());
                }
            }
        }

        return output.length() == 0 ? "No menu items found." + System.lineSeparator()
                : output.toString();
    }
}
