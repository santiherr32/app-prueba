package com.example.finanzapp

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Matrix
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finanzapp.R.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_register)
        auth = FirebaseAuth.getInstance()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fun getAdjustedAlphaValue(): Int {
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                // Dark mode
                3
            } else {
                // Light mode
                20
            }
        }

        val constraintLayout = findViewById<ConstraintLayout>(id.main)
        val bitmap = BitmapFactory.decodeResource(resources, drawable.ic_logo_img)
        val tileWidth = bitmap.width / 4
        val tileHeight = bitmap.height / 4

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, tileWidth, tileHeight, true)
        val shader = BitmapShader(scaledBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)

        val matrix = Matrix()
        matrix.setScale(1f, 1f)
        shader.setLocalMatrix(matrix)

        val bitmapDrawable = BitmapDrawable(resources, scaledBitmap).apply {
            paint.shader = shader
            setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
            alpha = getAdjustedAlphaValue()
        }

        constraintLayout.background = bitmapDrawable

        val emailInput = findViewById<TextInputEditText>(id.email_input)
        val passwordInput = findViewById<TextInputEditText>(id.password_input)
        val passwordConfirmInput = findViewById<TextInputEditText>(id.password_confirm_input)
        val registerButton = findViewById<MaterialButton>(id.register_button)
        val loginButton = findViewById<MaterialButton>(id.login_button)

        // Initially, disable the login button
        registerButton.isEnabled = false

        // Define a TextWatcher
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                // Check if both email and password fields are not empty
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()
                val passwordConfirm = passwordConfirmInput.text.toString().trim()

                val enableButton = email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()
                registerButton.isEnabled = enableButton
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed, but must be overridden
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed, but must be overridden
            }
        }

        // Add TextChangedListener to the email and password EditText fields
        emailInput.addTextChangedListener(textWatcher)
        passwordInput.addTextChangedListener(textWatcher)
        passwordConfirmInput.addTextChangedListener(textWatcher)

        fun isValidEmail(email: String): Boolean {
            val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
            return emailRegex.matches(email)
        }

        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val passwordConfirm = passwordConfirmInput.text.toString().trim()

            // Validate email format
            if (!isValidEmail(email)) {
                // Show error message or toast indicating invalid email format
                Toast.makeText(
                    baseContext, "Email is not a valid one.",
                    Toast.LENGTH_SHORT
                ).show()
                // You can implement isValidEmail(email) function to check the email format
                return@setOnClickListener
            } else if (passwordConfirm != password) {
                // Show error message or toast indicating invalid email format
                Toast.makeText(
                    baseContext, "Passwords don't match.",
                    Toast.LENGTH_SHORT
                ).show()
                // You can implement isValidEmail(email) function to check the email format
                return@setOnClickListener
            }

            createAccountWithEmailAndPassword(email, password)
        }

        loginButton.setOnClickListener {
            finish()
        }

    }

    private fun createAccountWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration success, update UI accordingly
                    Log.d(MainActivity.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    // Navigate to the next screen or perform other actions
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // If registration fails, display a message to the user.
                    Log.w(MainActivity.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Registration failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}