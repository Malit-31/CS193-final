# Purdue Menu JSON Fetcher

Fetches a dining court's menu from Purdue HFS and prints the API's raw JSON response. No parsing or profiles.

```powershell
mvn compile
java -cp "target\classes;$env:USERPROFILE\.m2\repository\com\google\code\gson\gson\2.11.0\gson-2.11.0.jar" mealplanner.MenuClient Earhart 2026-11-12
```

The request uses the [Purdue Dining Menus](https://dining.purdue.edu/menus/) GraphQL API.

