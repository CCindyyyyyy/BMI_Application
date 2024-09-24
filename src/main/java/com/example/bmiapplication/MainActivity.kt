package com.example.bmiapplication

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var inputWeight: EditText
    private lateinit var inputHeight: EditText
    private lateinit var submitButton: Button
    private lateinit var resetButton: Button
    private lateinit var imageButtonMale: ImageButton
    private lateinit var imageButtonFemale: ImageButton
    private lateinit var genderLayout: LinearLayout
    private lateinit var ageText: TextView
    private lateinit var weightUnitSpinner: Spinner
    private lateinit var heightUnitSpinner: Spinner

    private var gender: String? = null // Store gender as "M" or "F"
    private var age = 20

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
        imageButtonMale = findViewById(R.id.imageButtonmale)
        imageButtonFemale = findViewById(R.id.imageButtonfemale)
        genderLayout = findViewById(R.id.genderLayout) // LinearLayout for gender buttons
        ageText = findViewById(R.id.ageText)
        weightUnitSpinner = findViewById(R.id.weightUnitSpinner)
        heightUnitSpinner = findViewById(R.id.heightUnitSpinner)

        val plusButton: ImageButton = findViewById(R.id.imageplus)
        val minusButton: ImageButton = findViewById(R.id.imageminus)

        plusButton.setOnClickListener { incrementAge() }
        minusButton.setOnClickListener { decrementAge() }

        setupSpinners()

        // Set click listeners
        submitButton.setOnClickListener { handleSubmission() }
        resetButton.setOnClickListener { resetInputs() }

        imageButtonMale.setOnClickListener { selectMale() }
        imageButtonFemale.setOnClickListener { selectFemale() }
    }

    private fun setupSpinners() {
        // Setup weight unit spinner
        val weightUnits = arrayOf("kg", "lb")
        val weightAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, weightUnits)
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        weightUnitSpinner.adapter = weightAdapter

        // Setup height unit spinner
        val heightUnits = arrayOf("cm", "ft", "in")
        val heightAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, heightUnits)
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        heightUnitSpinner.adapter = heightAdapter
    }

    private fun incrementAge() {
        age++
        updateAgeDisplay()
    }

    private fun decrementAge() {
        if (age > 0) {
            age--
            updateAgeDisplay()
        }
    }

    private fun updateAgeDisplay() {
        ageText.text = "Age: $age"
    }

    private fun selectMale() {
        gender = "M" // Set gender for Male
        imageButtonMale.setBackgroundColor(Color.LTGRAY)
        imageButtonFemale.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun selectFemale() {
        gender = "F" // Set gender for Female
        imageButtonFemale.setBackgroundColor(Color.LTGRAY)
        imageButtonMale.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun handleSubmission() {
        if (gender == null) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
            return
        }

        // Get weight and height inputs
        val weightString = inputWeight.text.toString()
        val heightString = inputHeight.text.toString()

        val weight = weightString.toFloatOrNull()
        val height = heightString.toFloatOrNull()

        // Check if weight and height inputs are valid
        if (weight != null && height != null && height > 0) {
            // Convert weight and height based on selected units
            val convertedWeight = convertWeight(weight, weightUnitSpinner.selectedItem.toString())
            val convertedHeight = convertHeight(height, heightUnitSpinner.selectedItem.toString())

            if (convertedHeight > 0) {
                val bmi = convertedWeight / (convertedHeight / 100).pow(2)
                val bmiCategory = getBMICategory(bmi)

                // Create an Intent to start BmiResultActivity
                val intent = Intent(this, BmiResultActivity::class.java).apply {
                    putExtra("BMI_RESULT", bmi) // Pass the BMI as a Float
                    putExtra("BMI_CATEGORY", bmiCategory) // Pass the BMI category
                    putExtra("GENDER", gender ?: "Prefer not to say") // Pass the selected gender
                    putExtra("AGE", age)  // Pass the age
                    putExtra("WEIGHT", convertedWeight) // Pass converted weight
                    putExtra("HEIGHT", convertedHeight) // Pass converted height
                }
                startActivity(intent) // Start the new activity
            } else {
                Toast.makeText(this, "Invalid height provided", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter valid weight and height", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertWeight(weight: Float, unit: String): Float {
        return when (unit) {
            "kg" -> weight // No conversion needed
            "lb" -> weight * 0.453592f // Convert pounds to kilograms
            else -> weight // fallback
        }
    }

    private fun convertHeight(height: Float, unit: String): Float {
        return when (unit) {
            "cm" -> height // No conversion needed
            "ft" -> height * 30.48f // Convert feet to centimeters
            "in" -> height * 2.54f // Convert inches to centimeters
            else -> height // fallback
        }
    }

    private fun getBMICategory(bmi: Float): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25 -> "Normal weight"
            bmi < 30 -> "Overweight"
            bmi < 34.9 -> "Class I Obese"
            bmi < 39.9 -> "Class II Obese"
            else -> "Class III Obese"
        }
    }

    private fun resetInputs() {
        inputWeight.text.clear()
        inputHeight.text.clear()
        gender = null // Reset gender
        imageButtonMale.setBackgroundColor(Color.TRANSPARENT) // Reset male button color
        imageButtonFemale.setBackgroundColor(Color.TRANSPARENT) // Reset female button color
        age = 20 // Reset age
        updateAgeDisplay() // Update age display
    }
}