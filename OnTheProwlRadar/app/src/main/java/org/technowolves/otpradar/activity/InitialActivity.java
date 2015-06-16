package org.technowolves.otpradar.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;

import org.technowolves.otpradar.R;
import org.technowolves.otpradar.fragment.intro.SlideOne;
import org.technowolves.otpradar.fragment.intro.SlideTwo;

public class InitialActivity extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(new SlideOne(), getApplicationContext());
        addSlide(new SlideTwo(), getApplicationContext());

        // You can override bar/separator color if you want.
        setBarColor(getResources().getColor(R.color.colorPrimary));
        setSeparatorColor(getResources().getColor(R.color.colorPrimaryDark));

        // You can also hide Skip button
        showSkipButton(false);

    }

    @Override
    public void onSkipPressed() {
        this.finish();
    }

    @Override
    public void onDonePressed() {
        this.finish();
    }

}
