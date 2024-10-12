package com.example.bmiapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var inputWeight: EditText
    private lateinit var inputHeight: EditText
    private lateinit var submitButton: Button
    private lateinit var resetButton: Button
    private lateinit var imageButtonMale: ImageButton
    private lateinit var imageButtonFemale: ImageButton
    private lateinit var preferNotToSaySwitch: Switch
    private lateinit var ageText: TextView
    private lateinit var weightUnitSpinner: Spinner
    private lateinit var heightUnitSpinner: Spinner

    private var gender: String? = null // Store gender as "M" or "F"
    private var age = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        inputWeight = findViewById(R.id.inputweight)
        inputHeight = findViewById(R.id.inputheight)
        submitButton = findViewById(R.id.submitbutton)
        resetButton = findViewById(R.id.resetbutton)
        imageButtonMale = findViewById(R.id.imageButtonmale)
        imageButtonFemale = findViewById(R.id.imageButtonfemale)
        preferNotToSaySwitch = findViewById(R.id.genderswitch)
        ageText = findViewById(R.id.ageText)
        weightUnitSpinner = findViewById(R.id.weightUnitSpinner)
        heightUnitSpinner = findViewById(R.id.heightUnitSpinner)

        // Setting up spinners
        setupSpinners()

        // Initial age display
        updateAgeDisplay()

        // Click listeners for gender selection
        imageButtonMale.setOnClickListener { selectMale() }
        imageButtonFemale.setOnClickListener { selectFemale() }

        // Click listener for the switch
        preferNotToSaySwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                gender = null // Clear selected gender on switch
                disableGenderSelection()
                // Reset buttons to default state
                imageButtonMale.setBackgroundColor(Color.TRANSPARENT)
                imageButtonFemale.setBackgroundColor(Color.TRANSPARENT)
            } else {
                enableGenderSelection()
            }
        }

        // Click listeners for submission and reset
        submitButton.setOnClickListener { handleSubmission() }
        resetButton.setOnClickListener { resetInputs() }

        // Click listeners for age increment and decrement
        findViewById<ImageButton>(R.id.imageplus).setOnClickListener { incrementAge() }
        findViewById<ImageButton>(R.id.imageminus).setOnClickListener { decrementAge() }
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
        if (gender == "M") {
            gender = null // Unselect if already selected
            imageButtonMale.setBackgroundColor(Color.TRANSPARENT) // Reset button color
            preferNotToSaySwitch.isEnabled = true // Enable the switch when no gender is selected
        } else {
            gender = "M" // Set the gender
            imageButtonMale.setBackgroundColor(Color.LTGRAY) // Mark as selected
            imageButtonFemale.setBackgroundColor(Color.TRANSPARENT) // Reset female button color
            preferNotToSaySwitch.isChecked = false // Uncheck the switch
            preferNotToSaySwitch.isEnabled = false // Disable the switch
        }
    }

    private fun selectFemale() {
        if (gender == "F") {
            gender = null // Unselect if already selected
            imageButtonFemale.setBackgroundColor(Color.TRANSPARENT) // Reset button color
            preferNotToSaySwitch.isEnabled = true // Enable the switch when no gender is selected
        } else {
            gender = "F" // Set the gender
            imageButtonFemale.setBackgroundColor(Color.LTGRAY) // Mark as selected
            imageButtonMale.setBackgroundColor(Color.TRANSPARENT) // Reset male button color
            preferNotToSaySwitch.isChecked = false // Uncheck the switch
            preferNotToSaySwitch.isEnabled = false // Disable the switch
        }
    }

    private fun handleSubmission() {
        if (gender == null && !preferNotToSaySwitch.isChecked) {
            Toast.makeText(this, "Please select a gender or prefer not to say", Toast.LENGTH_SHORT).show()
            return
        }

        // Get weight and height inputs
        val weightString = inputWeight.text.toString()
        val heightString = inputHeight.text.toString()

        val weight = weightString.toFloatOrNull()
        val height = heightString.toFloatOrNull()

        if (weight != null && height != null && height > 0) {
            val convertedWeight = convertWeight(weight, weightUnitSpinner.selectedItem.toString())
            val convertedHeight = convertHeight(height, heightUnitSpinner.selectedItem.toString())

            if (convertedHeight > 0) {
                val bmi = convertedWeight / (convertedHeight / 100).pow(2)
                val bmiCategory = getBMICategory(bmi)

                // Create an Intent to start BmiResultActivity
                val intent = Intent(this, BmiResultActivity::class.java).apply {
                    putExtra("BMI_RESULT", bmi)
                    putExtra("BMI_CATEGORY", bmiCategory)
                    putExtra("GENDER", gender ?: "Prefer not to say")
                    putExtra("AGE", age)
                    putExtra("WEIGHT", convertedWeight)
                    putExtra("HEIGHT", convertedHeight)
                }
                startActivity(intent)
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
            else -> weight // Fallback
        }
    }

    private fun convertHeight(height: Float, unit: String): Float {
        return when (unit) {
            "cm" -> height // No conversion needed
            "ft" -> height * 30.48f // Convert feet to centimeters
            "in" -> height * 2.54f // Convert inches to centimeters
            else -> height // Fallback
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
        preferNotToSaySwitch.isChecked = false // Reset the switch
        preferNotToSaySwitch.isEnabled = true // Re-enable the switch
        age = 20 // Reset age
        updateAgeDisplay() // Update age display
        enableGenderSelection() // Re-enable gender buttons when resetting
    }

    private fun disableGenderSelection() {
        imageButtonMale.isEnabled = false
        imageButtonFemale.isEnabled = false
    }

    private fun enableGenderSelection() {
        imageButtonMale.isEnabled = true
        imageButtonFemale.isEnabled = true
    }
}