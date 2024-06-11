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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finanzapp.R.drawable
import com.example.finanzapp.R.id
import com.example.finanzapp.R.layout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_main)

        FirebaseApp.initializeApp(this)
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

        val loginButton = findViewById<MaterialButton>(id.login_button)
        val registerButton = findViewById<MaterialButton>(id.register_button)
        val googleSignInButton = findViewById<MaterialButton>(id.google_signin)
        val emailInput = findViewById<TextInputEditText>(id.email_input)
        val passwordInput = findViewById<TextInputEditText>(id.password_input)


       // Initially, disable the login button
        loginButton.isEnabled = false

        // Define a TextWatcher
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                // Check if both email and password fields are not empty
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                val enableButton = email.isNotEmpty() && password.isNotEmpty()
                loginButton.isEnabled = enableButton
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

        fun isValidEmail(email: String): Boolean {
            val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
            return emailRegex.matches(email)
        }

        loginButton.setOnClickListener {
            // Check if both email and password fields are not empty

            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validate email format
            if (!isValidEmail(email)) {
                // Show error message or toast indicating invalid email format
                Toast.makeText(
                    baseContext, "Email is not a valid one.",
                    Toast.LENGTH_SHORT
                ).show()
                // You can implement isValidEmail(email) function to check the email format
                return@setOnClickListener
            }

            signInWithEmailAndPassword(email, password)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        googleSignInButton.setOnClickListener {
            // Start the Google Sign-In process
            signInWithGoogle()
        }

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


    }

    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI accordingly
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    // Navigate to the next screen or perform other actions
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    companion object {
        const val TAG = "LoginActiviy"
        const val TAG_GOOGLE = "GoogleSignInActivity"
    }

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            } else {
                Log.e(TAG_GOOGLE, "Sign-in result canceled or unsuccessful")
                // Handle failed sign-in
            }
        }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(result: Task<GoogleSignInAccount>) {
        try {
            val account = result.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            Log.e(TAG_GOOGLE, "Google sign-in failed: ${e.statusCode}")
            // Handle failed sign-in
        }
    }


    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        Log.d(TAG_GOOGLE, "firebaseAuthWithGoogle:${account?.id}")
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    // Update UI
                } else {
                    // Sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}