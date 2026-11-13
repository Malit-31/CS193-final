package mealplanner;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public final class MenuClient {
    private static final URI ENDPOINT =
            URI.create("https://api.hfs.purdue.edu/menus/v3/GraphQL");
    private static final String MENU_QUERY = """
            query getLocationMenu($name: String!, $date: Date!) {
              diningCourtByName(name: $name) {
                name
                dailyMenu(date: $date) {
                  notes
                  meals {
                    endTime
                    startTime
                    notes
                    name
                    status
                    stations {
                      iconUrl
                      id
                      name
                      notes
                      items {
                        specialName
                        itemMenuId
                        hasComponents
                        item {
                          isNutritionReady
                          itemId
                          name
                          traits {
                            name
                            svgIcon
                            svgIconWithoutBackground
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            """;

    // stop instances of this static utility class
    private MenuClient() {
    }

    // gets the raw menu JSON for wtv court and date.
    private static String fetchMenuRawJson(String diningCourt, String date)
            throws IOException, InterruptedException {
        Map<String, Object> payload = Map.of(
                "operationName", "getLocationMenu",
                "variables", Map.of("name", diningCourt, "date", date),
                "query", MENU_QUERY);
        HttpRequest request = HttpRequest.newBuilder(ENDPOINT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(payload)))
                .build();
        HttpResponse<String> response =
                HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Purdue API returned HTTP " + response.statusCode()
                    + ": " + response.body());
        }
        return response.body();
    }

    // gets the menu + prints the raw json
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 2 || args[0].isBlank() || args[1].isBlank()) {
            System.err.println("Usage: MenuClient <dining-court> <yyyy-MM-dd>");
            System.exit(2);
            return;
        }
        System.out.print(fetchMenuRawJson(args[0], args[1]));
    }
}