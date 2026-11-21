package mealplanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class VegetarianMenuFilter {
    private VegetarianMenuFilter() {
    }

    public static String filter(String menuJson, boolean vegetarianOnly) {
        if (!vegetarianOnly) {
            return menuJson;
        }

        JsonObject response = requireObject(JsonParser.parseString(menuJson), "response");
        JsonObject data = requireObject(response.get("data"), "data");
        JsonElement courtValue = data.get("diningCourtByName");
        if (courtValue == null || courtValue.isJsonNull()) {
            return menuJson;
        }

        JsonObject court = requireObject(courtValue, "data.diningCourtByName");
        JsonElement dailyMenuValue = court.get("dailyMenu");
        if (dailyMenuValue == null || dailyMenuValue.isJsonNull()) {
            return menuJson;
        }

        JsonObject dailyMenu = requireObject(dailyMenuValue, "dailyMenu");
        JsonArray meals = requireArray(dailyMenu.get("meals"), "dailyMenu.meals");
        for (JsonElement mealValue : meals) {
            JsonObject meal = requireObject(mealValue, "meal");
            JsonArray stations = requireArray(meal.get("stations"), "meal.stations");
            for (JsonElement stationValue : stations) {
                JsonObject station = requireObject(stationValue, "station");
                JsonArray items = requireArray(station.get("items"), "station.items");
                for (int i = items.size() - 1; i >= 0; i--) {
                    if (!hasVegetarianTag(items.get(i))) {
                        items.remove(i);
                    }
                }
            }
        }
        return response.toString();
    }

    private static boolean hasVegetarianTag(JsonElement menuEntryValue) {
        JsonObject menuEntry = requireObject(menuEntryValue, "menu item");
        JsonElement itemValue = menuEntry.get("item");
        if (itemValue == null || itemValue.isJsonNull()) {
            return false;
        }

        JsonObject item = requireObject(itemValue, "menu item.item");
        JsonElement traitsValue = item.get("traits");
        if (traitsValue == null || traitsValue.isJsonNull()) {
            return false;
        }

        JsonArray traits = requireArray(traitsValue, "menu item.item.traits");
        for (JsonElement traitValue : traits) {
            JsonObject trait = requireObject(traitValue, "trait");
            JsonElement name = trait.get("name");
            if (name != null && name.isJsonPrimitive()
                    && name.getAsString().equalsIgnoreCase("Vegetarian")) {
                return true;
            }
        }
        return false;
    }

    private static JsonObject requireObject(JsonElement element, String path) {
        if (element == null || !element.isJsonObject()) {
            throw new IllegalArgumentException("Expected an object at " + path);
        }
        return element.getAsJsonObject();
    }

    private static JsonArray requireArray(JsonElement element, String path) {
        if (element == null || !element.isJsonArray()) {
            throw new IllegalArgumentException("Expected an array at " + path);
        }
        return element.getAsJsonArray();
    }
}
