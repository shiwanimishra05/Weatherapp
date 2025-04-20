import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleWeatherApp extends Application {

    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    private TextField locationField;
    private Label weatherLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simple Weather App");

        locationField = new TextField();
        locationField.setPromptText("Enter city name");

        Button fetchButton = new Button("Fetch Weather");
        fetchButton.setOnAction(e -> fetchWeatherData());

        weatherLabel = new Label();

        VBox layout = new VBox(10);
        layout.getChildren().addAll(locationField, fetchButton, weatherLabel);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void fetchWeatherData() {
        String cityName = locationField.getText();
        String apiUrl = String.format(API_URL, cityName, API_KEY);

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response.toString());

            JSONObject main = (JSONObject) json.get("main");
            double temperature = (double) main.get("temp");
            String description = ((JSONObject) json.getJSONArray("weather").get(0)).get("description").toString();

            weatherLabel.setText("Temperature: " + (temperature - 273.15) + "°C\nDescription: " + description);

        } catch (Exception e) {
            weatherLabel.setText("Error fetching weather data");
        }
    }
}
