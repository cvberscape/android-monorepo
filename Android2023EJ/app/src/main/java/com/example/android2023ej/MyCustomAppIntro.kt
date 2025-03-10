package com.example.android2023ej

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class MyCustomAppIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hides action bar during intro
        getSupportActionBar()!!.hide()

        // Enables indicator and color transitions
        isIndicatorEnabled = true
        isColorTransitionsEnabled = true

        // Sets the progress indicator
        setProgressIndicator()
        // Builds the first slide
        addSlide(AppIntroFragment.createInstance(
            title = "AppIntro",
            description = "This intro was made by using AppIntro library, it can be used to launch before main activity in apps to for example show features of a new update. In this project it's set to run on button press for demonstration purposes" ,
            titleColorRes = R.color.black,
            descriptionColorRes = R.color.black,
            backgroundColorRes = R.color.teal_200,
            imageDrawable = R.drawable.bedge,
            titleTypefaceFontRes = R.font.lato_black,
            descriptionTypefaceFontRes = R.font.lato,
        ))
        // Builds Second slide
        addSlide(AppIntroFragment.createInstance(
            title = "MotionToast",
            description = "The other part of this fragment shows functionality of MotionToast library by calling them whenever the button is pressed, this library is also used in this project in Feedback Send fragment",
            imageDrawable = R.drawable.bedge,
            titleColorRes = R.color.black,
            descriptionColorRes = R.color.black,
            backgroundColorRes = R.color.pink,
            titleTypefaceFontRes = R.font.lato_black,
            descriptionTypefaceFontRes = R.font.lato,
        ))
        // Builds third slide
        addSlide(AppIntroFragment.createInstance(
            title = "SpinKit",
            description = "Third UI library used is SpinKit, it can be used as a placeholder for empty time whenever you are loading something in your app, it is used in MQTT Weatherstation fragment on this app while it waits for the first weather data entry and there is a small demonstration of how it works in this fragment",
            imageDrawable = R.drawable.bedge,
            titleColorRes = R.color.white,
            descriptionColorRes = R.color.white,
            backgroundColorRes = R.color.black,
            titleTypefaceFontRes = R.font.lato_black,
            descriptionTypefaceFontRes = R.font.lato,
        ))
    }

    // Finishes intro when done button is pressed
    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    // Finishes intro when done button is pressed
    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }
}