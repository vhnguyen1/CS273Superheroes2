package edu.orangecoastcollege.cs273.vnguyen629.cs273superheroes2;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Class loads Superhero data from a formatted JSON (JavaScript Object Notation) file.
 * Populates data model (Superhero) with data.
 */
public class JSONLoader {
    /**
     * Loads JSON data from a file in the assets directory.
     * @param context The activity from which the data is loaded.
     * @throws IOException If there is an error reading from the JSON file.
     */
    public static ArrayList<Superhero> loadJSONFromAsset(Context context) throws IOException {
        ArrayList<Superhero> allSuperheroes = new ArrayList<>();
        String json = null;
            InputStream is = context.getAssets().open("cs273superheroes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        try {
            JSONObject jsonRootObject = new JSONObject(json);
            JSONArray allSuperheroesJSON = jsonRootObject.getJSONArray("Superheroes");
            int numberOfHeroes = allSuperheroesJSON.length();

            for (int i = 0; i < numberOfHeroes; i++) {
                JSONObject superheroJSON = allSuperheroesJSON.getJSONObject(i);

                Superhero hero = new Superhero();

                hero.setUserName(superheroJSON.getString("Username"));
                hero.setName(superheroJSON.getString("Name"));
                hero.setSuperPower(superheroJSON.getString("Superpower"));
                hero.setOneThing(superheroJSON.getString("OneThing"));
                hero.setImageName(superheroJSON.getString("ImageName"));

                allSuperheroes.add(hero);
            }
        }
        catch (JSONException e)
        {
            Log.e("CS273 Superheroes", e.getMessage());
        }

        return allSuperheroes;
    }
}
