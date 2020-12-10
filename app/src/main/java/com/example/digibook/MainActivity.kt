package com.example.digibook

import android.content.Intent
import android.graphics.Color.*
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.model.SliderPagerBuilder

class MainActivity : AppIntro2() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showIntroSlides();
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun showIntroSlides() {

        val pageZero = SliderPagerBuilder()
                .title("Welcome to Digibooks!")
                .description("A modern books search App & a place for books enthusiasts to share their experiences ")
                .imageDrawable(R.drawable.zeroslide)
                .backgroundColor (getColor(R.color.zeroslide))
                .build()

        val pageOne = SliderPagerBuilder()
                .title("Perform an image search!")
                .description("We provide two methods to search for books either by taking a direct picture or uploading an existing one")
                .imageDrawable(R.drawable.intro_slide1)
                .backgroundColor (getColor(R.color.firstslide))
                .build()

        val pageTwo = SliderPagerBuilder()
                .title("Review found research!")
                .description("you can review found books by likes/dislikes or comments")
                .imageDrawable(R.drawable.slide)
                .backgroundColor (getColor(R.color.secondslide))
                .build()

        val pageThree = SliderPagerBuilder()
                .title("Add found books into ur favorites!")
                .description("you can add found books that you seem to like into your bookshelf")
                .imageDrawable(R.drawable.slidee)
                .backgroundColor (getColor(R.color.thirdslide))
                .build()

        val pageFour = SliderPagerBuilder()
                .title("Share your experience!")
                .description("by making posts in the newsfeed, you can share your experiences in the book world with other books enthusiasts")
                .imageDrawable(R.drawable.slideee)
                .backgroundColor(getColor(R.color.Forthslide))
                .build()

        addSlide(AppIntroFragment.newInstance(pageZero))
        addSlide(AppIntroFragment.newInstance(pageOne))
        addSlide(AppIntroFragment.newInstance(pageTwo))
        addSlide(AppIntroFragment.newInstance(pageThree))
        addSlide(AppIntroFragment.newInstance(pageFour))
    }

    private fun goToMain() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToMain()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        goToMain()
    }

}