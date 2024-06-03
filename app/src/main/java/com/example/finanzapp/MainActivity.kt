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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finanzapp.R.*
import com.google.android.material.button.MaterialButton


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_main)
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

        loginButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }



    }
}