package hu.drorszagkriszaxel.jokeprovider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;


/**
 * This class provides jokes from external source to the GCE.
 */
public class JokeProvider {

    // Common key-free API to get some jokes.
    private final static String API_PATH = "http://api.icndb.com/jokes/random/?escape=javascript";

    /**
     * Static function that provides a joke or a default message if no joke is received.
     *
     * @return Text, hopefully the joke
     */
    public static String tellMeJoke() {

        InputStream inputStream = null;
        JsonReader jsonReader = null;
        JsonObject jsonObject = null;
        String returnString = "No joke. I'm not kidding.";

        try {

            inputStream = new URL(API_PATH).openStream();

        } catch (IOException e) {

            e.printStackTrace();

        }

        if (inputStream != null) {

            jsonReader = Json.createReader(inputStream);
            jsonObject = jsonReader.readObject();

        }

        if (jsonObject != null && jsonObject.getString("type","").equals("success")) {

            if (jsonObject.getJsonObject("value") != null &&
                    jsonObject.getJsonObject("value").getString("joke",null) != null)
                returnString = jsonObject.getJsonObject("value").getString("joke");

        }

        return returnString;

    }

}
