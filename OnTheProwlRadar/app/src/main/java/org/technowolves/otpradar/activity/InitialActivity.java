package org.technowolves.otpradar.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;

import org.technowolves.otpradar.fragment.intro.SlideOne;

public class InitialActivity extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(new SlideOne(), getApplicationContext());
        addSlide(new SlideOne(), getApplicationContext());

        // You can override bar/separator color if you want.
        setBarColor(Color.parseColor("#D32F2F"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // You can also hide Skip button
        showSkipButton(true);

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
