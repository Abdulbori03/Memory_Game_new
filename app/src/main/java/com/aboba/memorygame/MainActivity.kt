package com.aboba.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.aboba.memorygame.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val listImageOpenClose = arrayOf(false, false, false, false, false, false)
    val imageIndex = arrayOfNulls<Int>(2)
    val imageId = arrayOfNulls<Int>(2)
    var openImage = 0
    var animationDoing = false
    private val images = arrayOf(R.drawable.clubs, R.drawable.diamonds, R.drawable.spades, R.drawable.clubs, R.drawable.diamonds, R.drawable.spades)
    private var startTime = 0L
    private val handler = Handler()
    private var timerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.image1.setOnClickListener {
            imageClick(binding.image1, images[0], 0)
        }
        binding.image2.setOnClickListener {
            imageClick(binding.image2, images[1], 1)
        }
        binding.image3.setOnClickListener {
            imageClick(binding.image3, images[2], 2)
        }
        binding.image4.setOnClickListener {
            imageClick(binding.image4, images[3], 3)
        }
        binding.image5.setOnClickListener {
            imageClick(binding.image5, images[4], 4)
        }
        binding.image6.setOnClickListener {
            imageClick(binding.image6, images[5], 5)
        }

        binding.btnRestart.setOnClickListener {
            restartGame()
        }

        startTimer()
    }

    private fun imageClick(imageView: ImageView, rasm: Int, index: Int){
        if (!listImageOpenClose[index]){
            animationOpen(imageView, rasm, index)
        }else{
            animationClose(imageView, rasm, index)
        }
    }

    private fun animationOpen(imageView: ImageView, rasm:Int, index: Int){
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_1)
        imageView.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                animationDoing = false
            }

            override fun onAnimationEnd(animation: Animation?) {
                val animation2 = AnimationUtils.loadAnimation(this@MainActivity, R.anim.anim_2)
                imageView.startAnimation(animation2)
                imageView.setImageResource(rasm)
                animation2.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationStart(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        listImageOpenClose[index] = true
                        imageIndex[openImage] = index
                        imageId[openImage] = rasm
                        openImage++

                        if (openImage == 2){
                            if (imageId[0] == imageId[1]){
                                imageViewAniqla(imageIndex[0]).visibility = View.INVISIBLE
                                openImage--
                                imageViewAniqla(imageIndex[1]).visibility = View.INVISIBLE
                                openImage--
                            } else {
                                animationClose(imageViewAniqla(imageIndex[0]), -1, imageIndex[0]!!)
                                animationClose(imageViewAniqla(imageIndex[1]), -1, imageIndex[1]!!)
                            }
                        }
                        animationDoing = false
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun animationClose(imageView: ImageView, rasm:Int, index: Int){
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_1)
        imageView.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                animationDoing = true
            }

            override fun onAnimationEnd(animation: Animation?) {
                val animation2 = AnimationUtils.loadAnimation(this@MainActivity, R.anim.anim_2)
                imageView.startAnimation(animation2)
                imageView.setImageResource(R.drawable.poker)
                animation2.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationStart(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        animationDoing = false
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        listImageOpenClose[index] = false
        openImage--
    }

    private fun imageViewAniqla(index: Int?):ImageView{
        return when(index){
            0 -> binding.image1
            1 -> binding.image2
            2 -> binding.image3
            3 -> binding.image4
            4 -> binding.image5
            5 -> binding.image6
            else -> throw IllegalArgumentException("Invalid index")
        }
    }

    private fun restartGame() {
        randomizeImages()
        resetGame()
        startTimer()
    }

    private fun randomizeImages() {
        images.shuffle()
        binding.image1.setImageResource(R.drawable.poker)
        binding.image2.setImageResource(R.drawable.poker)
        binding.image3.setImageResource(R.drawable.poker)
        binding.image4.setImageResource(R.drawable.poker)
        binding.image5.setImageResource(R.drawable.poker)
        binding.image6.setImageResource(R.drawable.poker)
    }

    private fun resetGame() {
        for (i in listImageOpenClose.indices) {
            listImageOpenClose[i] = false
        }
        openImage = 0
        imageIndex.fill(null)
        imageId.fill(null)
        for (i in 0..5) {
            imageViewAniqla(i).visibility = View.VISIBLE
        }
    }

    private fun startTimer() {
        if (timerRunning) {
            handler.removeCallbacks(timerRunnable)
        }
        startTime = SystemClock.uptimeMillis()
        handler.post(timerRunnable)
        timerRunning = true
    }

    private val timerRunnable: Runnable = object : Runnable {
        override fun run() {
            val elapsedMillis = SystemClock.uptimeMillis() - startTime
            val seconds = (elapsedMillis / 1000).toInt()
            val minutes = seconds / 60
            val displaySeconds = seconds % 60
            binding.txtTime.text = String.format("%02d:%02d", minutes, displaySeconds)
            handler.postDelayed(this, 1000)
        }
    }
}
