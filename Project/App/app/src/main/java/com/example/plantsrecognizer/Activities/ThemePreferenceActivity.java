package com.example.plantsrecognizer.Activities;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.example.plantsrecognizer.R;
import com.example.plantsrecognizer.Utils.PreferenceHandler;

public class ThemePreferenceActivity extends PreferenceActivity {

    public static final int RESULT_CODE_THEME_UPDATED = 1;
    public static final int RESULT_CODE_GALLERY_UPDATED = 1;

    PreferenceHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        findPreference("theme").
                setOnPreferenceChangeListener(new RefreshActivityOnPreferenceChangeListener(RESULT_CODE_THEME_UPDATED));
    }

    private class RefreshActivityOnPreferenceChangeListener implements OnPreferenceChangeListener {
        private final int resultCode;

        RefreshActivityOnPreferenceChangeListener(int resultCode) {
            this.resultCode = resultCode;
        }

        @Override
        public boolean onPreferenceChange(Preference p, Object newValue) {
            setResult(resultCode);
            return true;
        }
    }
}