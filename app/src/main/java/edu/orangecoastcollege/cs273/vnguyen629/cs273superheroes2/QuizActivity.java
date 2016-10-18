package edu.orangecoastcollege.cs273.vnguyen629.cs273superheroes2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Handles the layouts to display (whether or not to inflate
 * due to the devices specs and orientation). Also handles
 * whether ot not the quiz settings/type is changed and adjusts
 * appropriately.
 *
 * @author Vincent Nguyen
 */
public class QuizActivity extends AppCompatActivity {

    public static final String TYPE = "pref_quiz_type";

    private boolean phoneDevice = true;
    private boolean preferencesChanged = true;

    private Context context = this;
    private ArrayList<Superhero> allHeroes;

    /**
     * onCreate generates the appropriate layout to inflate, depending on the
     * screen size. IF the device is large or x-large, it will load the content_main.xml
     * (sw700dp-land) which includes both the fragment_quiz.xml and fragment_settings.xml.
     * Otherwise, it just inflates the standard content_main.xml with the fragment quiz.
     * <p>
     * All default preferences are set using the preferences.xml file.
     *
     * @param savedInstanceState The saved state to restore (not being used)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(
                        preferenceChangeListener);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            phoneDevice = false;

        if (phoneDevice)
            setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {
            allHeroes = JSONLoader.loadJSONFromAsset(context);
        }
        catch (IOException ex) {
            Log.e("CS273 Superheroes", "Error Loading JSON Data." + ex.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (preferencesChanged) {
            QuizActivityFragment quizFragment = (QuizActivityFragment)
                    getSupportFragmentManager().findFragmentById(
                            R.id.quizFragment);
            quizFragment.updateGuessRows();
            quizFragment.resetQuiz();
            preferencesChanged = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            getMenuInflater().inflate(R.menu.menu_quiz, menu);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Listener to handle changes in the app's shared preferences (preferences.xml)
     *
     * If quiz type is changed, the quiz will restart with the
     * new settings.
     */
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(
                        SharedPreferences sharedPreferences, String key) {
                    preferencesChanged = true;

                    QuizActivityFragment quizFragment = (QuizActivityFragment)
                            getSupportFragmentManager().findFragmentById(
                                    R.id.quizFragment);

                    if (key.equals(TYPE)) {
                        Set<String> quizTypeSet =
                                sharedPreferences.getStringSet(TYPE, null);

                        if (quizTypeSet != null & quizTypeSet.size() > 0) {
                            quizFragment.updateType(sharedPreferences);
                            quizFragment.resetQuiz();
                        }
                        else {
                            SharedPreferences.Editor editor =
                                    sharedPreferences.edit();
                            quizTypeSet.add(getString(R.string.default_type));
                            editor.putStringSet(TYPE, quizTypeSet);
                            editor.apply();

                            Toast.makeText(QuizActivity.this,
                                    R.string.default_type,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    Toast.makeText(QuizActivity.this,
                            R.string.restarting_quiz,
                            Toast.LENGTH_SHORT).show();
                }
            };
}