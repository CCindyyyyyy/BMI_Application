package com.example.bmiapplication

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BmiResultActivity : AppCompatActivity() {

    private lateinit var bmiResultTextView: TextView
    private lateinit var bmiCategoryTextView: TextView
    private lateinit var bmiCategoryImageView: ImageView
    private lateinit var backButton: Button
    private lateinit var genderTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var heightTextView: TextView
    private lateinit var weightTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_result)

        // Initialize Views
        bmiResultTextView = findViewById(R.id.bmiResultTextView)
        bmiCategoryTextView = findViewById(R.id.bmiCategoryTextView)
        bmiCategoryImageView = findViewById(R.id.bmiCategoryImageView)
        backButton = findViewById(R.id.backButton)

        // Initialize new TextViews
        genderTextView = findViewById(R.id.genderTextView)
        ageTextView = findViewById(R.id.ageTextView)
        heightTextView = findViewById(R.id.heightTextView)
        weightTextView = findViewById(R.id.weightTextView)

        // Retrieve data from Intent
        val bmiValue = intent.getFloatExtra("BMI_RESULT", 0f)
        val bmiCategory = intent.getStringExtra("BMI_CATEGORY") ?: "N/A"
        val gender = intent.getStringExtra("GENDER") ?: "N/A"
        val age = intent.getIntExtra("AGE", 0)
        val height = intent.getFloatExtra("HEIGHT", 0f)
        val weight = intent.getFloatExtra("WEIGHT", 0f)

        // Set data to TextViews
        bmiResultTextView.text = String.format("%.2f", bmiValue)
        bmiCategoryTextView.text = bmiCategory
        genderTextView.text = "$gender"
        ageTextView.text = "$age"
        heightTextView.text = "%.2fcm".format(height)
        weightTextView.text = "%.2fkg".format(weight)

        // Set the corresponding image based on the BMI category
        setBmiCategoryImage(bmiCategory)

        // Set up the OnClickListener for the back button
        backButton.setOnClickListener {
            finish() // This will finish the current activity and return to the previous one
        }
    }

    private fun setBmiCategoryImage(bmiCategory: String) {
        when (bmiCategory) {
            "Underweight" -> bmiCategoryImageView.setImageResource(R.drawable.sad)
            "Normal weight" -> bmiCategoryImageView.setImageResource(R.drawable.happy)
            "Overweight" -> bmiCategoryImageView.setImageResource(R.drawable.sad)
            "Obese" -> bmiCategoryImageView.setImageResource(R.drawable.worried)
            else -> bmiCategoryImageView.setImageResource(R.drawable.angry) // Default image or error image
        }
    }
}