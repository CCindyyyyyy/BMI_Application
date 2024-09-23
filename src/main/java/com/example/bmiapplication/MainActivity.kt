package com.example.bmiapplication

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var inputWeight: EditText
    private lateinit var inputHeight: EditText
    private lateinit var submitButton: Button
    private lateinit var resetButton: Button
    private lateinit var bmiResult: TextView
    private lateinit var imageButtonMale: ImageButton
    private lateinit var imageButtonFemale: ImageButton
    private lateinit var genderSwitch: Switch
    private lateinit var genderLayout: LinearLayout

    private var isMaleSelected = false
    private var isFemaleSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Set up window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        inputWeight = findViewById(R.id.inputweight)
        inputHeight = findViewById(R.id.inputheight)
        submitButton = findViewById(R.id.submitbutton)
        resetButton = findViewById(R.id.resetbutton)
        bmiResult = findViewById(R.id.BMICaculator)
        imageButtonMale = findViewById(R.id.imageButtonmale)
        imageButtonFemale = findViewById(R.id.imageButtonfemale)
        genderSwitch = findViewById(R.id.genderswitch)
        genderLayout = findViewById(R.id.genderLayout) // LinearLayout for gender buttons

        // Set click listeners
        submitButton.setOnClickListener { handleSubmission() }
        resetButton.setOnClickListener { resetInputs() }

        imageButtonMale.setOnClickListener {
            selectMale()
        }

        imageButtonFemale.setOnClickListener {
            selectFemale()
        }
    }

    private fun selectMale() {
        if (isMaleSelected) {
            // If male is already selected, toggle it off
            isMaleSelected = false
            genderLayout.setBackgroundColor(Color.TRANSPARENT)
            imageButtonMale.setBackgroundColor(Color.TRANSPARENT)
            imageButtonFemale.setBackgroundColor(Color.TRANSPARENT)
            genderSwitch.isEnabled = true // Enable switch again

        } else {
            isMaleSelected = true
            isFemaleSelected = false
            imageButtonMale.setBackgroundColor(Color.LTGRAY) // Reset male button color
            imageButtonFemale.setBackgroundColor(Color.TRANSPARENT) // Reset female button color
            genderSwitch.isEnabled = false // Disable switch

        }
    }

    private fun selectFemale() {
        if (isFemaleSelected) {
            // If female is already selected, toggle it off
            isFemaleSelected = false
            genderLayout.setBackgroundColor(Color.TRANSPARENT)
            imageButtonFemale.setBackgroundColor(Color.TRANSPARENT)
            imageButtonMale.setBackgroundColor(Color.TRANSPARENT)
            genderSwitch.isEnabled = true // Enable switch again

        } else {
            isFemaleSelected = true
            isMaleSelected = false
            imageButtonFemale.setBackgroundColor(Color.LTGRAY) // Reset female button color
            imageButtonMale.setBackgroundColor(Color.TRANSPARENT) // Reset male button color
            genderSwitch.isEnabled = false // Disable switch

        }
    }

    private fun handleSubmission() {
        // Check if the user has selected either male or female or opted not to disclose
        if (!isMaleSelected && !isFemaleSelected && !genderSwitch.isChecked) {
            Toast.makeText(this, "Please select a gender or choose not to disclose", Toast.LENGTH_SHORT).show()
            return
        }

        // Get weight and height inputs
        val weight = inputWeight.text.toString().toFloatOrNull()
        val height = inputHeight.text.toString().toFloatOrNull()

        // Check if weight and height inputs are valid
        if (weight != null && height != null && height > 0) {
            val bmi = weight / (height / 100).pow(2)
            val formattedBmi = String.format("%.2f", bmi)
            val bmiCategory = getBMICategory(bmi)

            // Display the BMI result and category
            bmiResult.text = "BMI: $formattedBmi\nCategory: $bmiCategory"
        } else {
            bmiResult.text = "Invalid Input"
            Toast.makeText(this, "Please enter valid weight and height", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBMICategory(bmi: Float): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25 -> "Normal weight"
            bmi < 30 -> "Overweight"
            else -> "Obese"
        }
    }

    private fun resetInputs() {
        inputWeight.text.clear()
        inputHeight.text.clear()
        bmiResult.text = "Result" // Reset the result text
        isMaleSelected = false
        isFemaleSelected = false
        genderSwitch.isChecked = false // Reset switch
        genderLayout.setBackgroundColor(Color.TRANSPARENT) // Reset layout color
        imageButtonMale.setBackgroundColor(Color.TRANSPARENT) // Reset male button color
        imageButtonFemale.setBackgroundColor(Color.TRANSPARENT) // Reset female button color
        genderSwitch.isEnabled = true // Enable switch
    }
}