import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Main {
    public static void main(String[] args) {
        String accessKey = "df39576a-bbd0-4eab-a40b-27db8ebdfe08";
        int limit = 7;
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.yandex.ru/v2/forecast"))
                .header("X-Yandex-Weather-Key", accessKey)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            System.out.println(jsonResponse.toString(2));
            JSONObject fact = jsonResponse.getJSONObject("fact");
            System.out.printf("Температура в данный момент: %s °C \n", fact.getInt("temp"));
            JSONArray forecasts = jsonResponse.getJSONArray("forecasts");
            int tempSum = 0;
            for (int i=0; i < forecasts.length(); i++) {
                JSONObject day = forecasts.getJSONObject(i);
                JSONArray hours = day.getJSONArray("hours");
                for (int y=0; y < hours.length(); y++) {
                    JSONObject hour = hours.getJSONObject(y);
                    tempSum += hour.getInt("temp");
                }
            }
            int middleTemp = tempSum / (24 * forecasts.length());
            System.out.printf("Средняя температура за %s дней: %s", limit, middleTemp);
        } catch (Exception e) {
            System.err.println("Error making HTTP request: " + e.getMessage());
        }
    }
}