package org.technowolves.otpradar.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.github.paolorotolo.appintro.AppIntro2;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.view.fragment.intro.NewSlide;
import org.technowolves.otpradar.view.fragment.intro.SlideThree;

public class InitialActivity extends AppIntro2
                                implements SlideThree.OnFragmentReturn{

    private Boolean isDonePressed = false;

    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(NewSlide.newInstance(R.layout.slide_one));
        addSlide(NewSlide.newInstance(R.layout.slide_two));
        addSlide(new SlideThree());

        // You can override bar/separator color if you want.
        /*setBarColor(getResources().getColor(R.color.colorPrimary));
        setSeparatorColor(getResources().getColor(R.color.colorPrimaryDark));*/

        setFlowAnimation();

        // You can also hide Skip button
        //showSkipButton(false);

    }

    /*@Override
    public void onSkipPressed() {
        this.finish();
    }*/

    @Override
    public void onDonePressed() {
        isDonePressed = true;
        this.finish();
    }

    public void storeSeasonPref(String number, String name, int spnIndex) {
        if (isDonePressed) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit()
                    .putString(SettingsActivity.PREF_NUMBER, number)
                    .putString(SettingsActivity.PREF_NAME, name)
                    .putInt(SettingsActivity.PREF_SEASON, spnIndex)
                    .apply();
        }
    }

}
