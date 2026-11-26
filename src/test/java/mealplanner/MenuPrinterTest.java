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
                                "item": {
                                  "name": "Veggie Bowl",
                                  "nutritionFacts": [
                                    {"name": "Serving Size", "label": "1 cup", "value": null},
                                    {"name": "Calories", "label": "220", "value": 219.7},
                                    {"name": "Protein", "label": "8g", "value": 8.2}
                                  ]
                                }
                              },
                              {
                                "specialName": "Chef's Special",
                                "item": {
                                  "name": "Special Item",
                                  "nutritionFacts": [
                                    {"name": "Calories", "label": "150", "value": 150.0},
                                    {"name": "Protein", "label": "5g", "value": 5.0}
                                  ]
                                }
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
                "Lunch - The Gallery: Veggie Bowl "
                        + "(Calories: 219.7 kcal, Protein: 8.2 g per 1 cup)"
                        + System.lineSeparator()
                        + "Lunch - The Gallery: Chef's Special "
                        + "(Calories: 150.0 kcal, Protein: 5.0 g)"
                        + System.lineSeparator(),
                MenuPrinter.formatFoodItems(json));
    }

    @Test
    void showsUnavailableWhenNutritionFactsAreMissing() {
        String json = """
                {
                  "data": {
                    "diningCourtByName": {
                      "dailyMenu": {
                        "meals": [{
                          "name": "Lunch",
                          "stations": [{
                            "name": "The Gallery",
                            "items": [{
                              "specialName": null,
                              "item": {"name": "Item", "nutritionFacts": []}
                            }]
                          }]
                        }]
                      }
                    }
                  }
                }
                """;

        assertEquals(
                "Lunch - The Gallery: Item "
                        + "(Calories: unavailable, Protein: unavailable)"
                        + System.lineSeparator(),
                MenuPrinter.formatFoodItems(json));
    }
}
