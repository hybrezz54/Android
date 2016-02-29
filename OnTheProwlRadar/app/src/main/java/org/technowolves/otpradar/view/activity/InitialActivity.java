package org.technowolves.otpradar.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.view.fragment.intro.NewSlide;
import org.technowolves.otpradar.view.fragment.intro.SlideThree;

public class InitialActivity extends AppIntro {

    private Boolean isDonePressed = false;

    @Override
    public void init(Bundle savedInstanceState) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNextInit = prefs.getBoolean(OldMainActivity.PREFS_OPEN_APP, false);

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(NewSlide.newInstance(R.layout.slide_one));
        addSlide(NewSlide.newInstance(R.layout.slide_two));

        if (!isNextInit)
            addSlide(new SlideThree());

        // You can override bar/separator color if you want.
        /*setBarColor(getResources().getColor(R.color.colorPrimary));
        setSeparatorColor(getResources().getColor(R.color.colorPrimaryDark));*/

        setFlowAnimation();

        // You can also hide Skip button
        showSkipButton(false);

    }

    @Override
    public void onSkipPressed() {
        finish();
    }

    @Override
    public void onDonePressed() {
        isDonePressed = true;
        finish();
    }

    @Override
    public void onNextPressed() {
    }

    @Override
    public void onSlideChanged() {
    }

    public void storeSeasonPref(String number, String name, int spnIndex) {
        if (isDonePressed) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit()
                    .putString(SettingsActivity.PREF_NUMBER, number)
                    .putString(SettingsActivity.PREF_NAME, name)
                    .putString(SettingsActivity.PREF_SEASON, String.valueOf(spnIndex))
                    .apply();
        }
    }

}
