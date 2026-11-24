package mealplanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MenuPrinterTest {
    @Test
    void formatsItemsOnePerLineWithTheirMealAndStation() {
        String json = """
                {
                  "data": {
                    "diningCourtByName": {
                      "dailyMenu": {
                        "meals": [{
                          "name": "Lunch",
                          "stations": [{
                            "name": "The Gallery",
                            "items": [
                              {
                                "specialName": null,
                                "item": {"name": "Veggie Bowl"}
                              },
                              {
                                "specialName": "Chef's Special",
                                "item": {"name": "Special Item"}
                              }
                            ]
                          }]
                        }]
                      }
                    }
                  }
                }
                """;

        assertEquals(
                "Lunch - The Gallery: Veggie Bowl" + System.lineSeparator()
                        + "Lunch - The Gallery: Chef's Special" + System.lineSeparator(),
                MenuPrinter.formatFoodItems(json));
    }
}
