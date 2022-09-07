package com.proj3ct.IanAutomatonBot.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    //    http://api.openweathermap.org/data/2.5/weather?q=Toronto&appid=91675de81550173925be2ba8f13f0102&units=metric
    public static String getWeather(String message, WeatherModel model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=cd720815216b4df547ce919be606a6c8");

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));


        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONObject wind = object.getJSONObject("wind");
        model.setSpeed(wind.getDouble("speed"));


        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setDescription((String) obj.get("description"));
        }

        return "Weather now in " + model.getName() + "\n" +
                "Temperature:  " + model.getTemp() + "°C" + "\n" +
                "Description: " + model.getDescription() + "\n" +
                "Wind speed: " + model.getSpeed() + " km/h" + "\n" +
                "Humidity: " + model.getHumidity() + "%" + "\n";
    }
}
