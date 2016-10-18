package edu.orangecoastcollege.cs273.vnguyen629.cs273superheroes2;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * SettingsActivityFragment is a subclass of PreferenceFragment for managing
 * the app settings.
 */
public class SettingsActivityFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.preferences);
    }
}