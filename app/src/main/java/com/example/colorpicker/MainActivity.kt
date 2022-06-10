package com.example.colorpicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.colorpicker.colorWheel.ColorObserver
import com.example.colorpicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ColorObserver, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private val INITIAL_COLOR: Long = 0xFFFF8000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.colorPicker.subscribe(this)
        val color: Long = INITIAL_COLOR
        binding.colorPicker.setInitialColor(color.toInt())
        initOnClickListener()
    }

    private fun initOnClickListener(){
        binding.threeWaySelector.firstCard.setOnClickListener(this)
        binding.threeWaySelector.secondCard.setOnClickListener(this)
        binding.threeWaySelector.thirdCard.setOnClickListener(this)
    }

    override fun onColor(color: Int, fromUser: Boolean, shouldPropagate: Boolean) {
        binding.pickedColor.setBackgroundColor(color)
        binding.colorPicker.setSelectorColor(color)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.firstCard -> onFirstCardClick()
            R.id.secondCard -> onSecondCardClick()
            R.id.thirdCard -> onThirdCardClick()
        }
    }

    // todo find color of controller in color wheel

    private fun onFirstCardClick(){
        binding.threeWaySelector.firstCard.isSelected = true
        binding.threeWaySelector.secondCard.isSelected = false
        binding.threeWaySelector.thirdCard.isSelected = false
        val color = R.color.colorTeal
        binding.pickedColor.setBackgroundColor(resources.getColor(color))
    }

    private fun onSecondCardClick(){
        binding.threeWaySelector.firstCard.isSelected = false
        binding.threeWaySelector.secondCard.isSelected = true
        binding.threeWaySelector.thirdCard.isSelected = false
        val color = R.color.colorGreen
        binding.pickedColor.setBackgroundColor(resources.getColor(color))

    }

    private fun onThirdCardClick(){
        binding.threeWaySelector.firstCard.isSelected = false
        binding.threeWaySelector.secondCard.isSelected = false
        binding.threeWaySelector.thirdCard.isSelected = true
        val color = R.color.colorOrange
        binding.pickedColor.setBackgroundColor(resources.getColor(color))
    }
}