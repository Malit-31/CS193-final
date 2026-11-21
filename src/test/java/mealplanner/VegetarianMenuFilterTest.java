package mealplanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class VegetarianMenuFilterTest {
    private static final String MENU_JSON = """
            {
              "data": {
                "diningCourtByName": {
                  "name": "Earhart",
                  "dailyMenu": {
                    "meals": [{
                      "name": "Lunch",
                      "stations": [{
                        "name": "Gallery",
                        "items": [
                          {"item": {"name": "Tofu Bowl", "traits": [{"name": "Vegetarian"}]}},
                          {"item": {"name": "Chicken", "traits": []}},
                          {"item": {"name": "Unlabeled", "traits": []}}
                        ]
                      }]
                    }]
                  }
                }
              }
            }
            """;

    @Test
    void removesItemsWithoutVegetarianTag() {
        String filtered = VegetarianMenuFilter.filter(MENU_JSON, true);

        var items = JsonParser.parseString(filtered)
                .getAsJsonObject()
                .getAsJsonObject("data")
                .getAsJsonObject("diningCourtByName")
                .getAsJsonObject("dailyMenu")
                .getAsJsonArray("meals")
                .get(0).getAsJsonObject()
                .getAsJsonArray("stations")
                .get(0).getAsJsonObject()
                .getAsJsonArray("items");

        assertEquals(1, items.size());
        assertEquals("Tofu Bowl", items.get(0).getAsJsonObject()
                .getAsJsonObject("item").get("name").getAsString());
    }

    @Test
    void keepsAllItemsWhenVegetarianRestrictionIsOff() {
        assertEquals(MENU_JSON, VegetarianMenuFilter.filter(MENU_JSON, false));
    }
}
