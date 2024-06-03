package com.example.finanzapp

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (getResources().configuration.screenWidthDp >= 600) {
            setContentView(R.layout.activity_home)
        } else {
            setContentView(R.layout.activity_home)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val seeMoreMonthsButton = findViewById<MaterialButton>(R.id.see_more_months_button)
        val spannableString = SpannableString("Ver otros meses>")
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Handle the click event here
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        spannableString.setSpan(
            clickableSpan,
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        seeMoreMonthsButton.text = spannableString
        seeMoreMonthsButton.movementMethod = LinkMovementMethod.getInstance()

        val recyclerView: RecyclerView = findViewById(R.id.recent_trans_list)
        val linearLayoutManager =
            LinearLayoutManager(this) // Use LinearLayoutManager for vertical list
        recyclerView.layoutManager = linearLayoutManager

        val dataList = listOf(
            Transaction(
                "Movimiento 1",
                "expense",
                "health_cat",
                BigDecimal("50000"),
                "AV Villas",
                LocalDate.of(2024, 6, 1).atStartOfDay()
            ),
            Transaction(
                "Movimiento 2",
                "income",
                "salary_cat",
                BigDecimal("10000"),
                "Nequi",
                LocalDateTime.of(2024, 6, 1, 5, 30)
            )
        ) // Example data
        val adapter = CardAdapter(dataList)
        recyclerView.adapter = adapter


        val hideBalanceIcon = findViewById<MaterialButton>(R.id.hideBalanceIcon)
        val totalBalance = findViewById<TextView>(R.id.totalBalance)
        var isBalanceHidden = false

        // Save the original balance text
        val originalBalance = totalBalance.text.toString()

        hideBalanceIcon.setOnClickListener {
            isBalanceHidden = !isBalanceHidden
            if (isBalanceHidden) {
                hideBalanceIcon.setIconResource(R.drawable.eye_off)
                val hiddenBalance = "*".repeat(originalBalance.length)
                totalBalance.text = hiddenBalance
            } else {
                hideBalanceIcon.setIconResource(R.drawable.eye)
                totalBalance.text = originalBalance
            }
        }

        val addIncomeButton = findViewById<MaterialButton>(R.id.add_income_button)
        val addExpenseButton = findViewById<MaterialButton>(R.id.add_expense_button)

        val onClickListener = View.OnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
        addIncomeButton.setOnClickListener(onClickListener)
        addExpenseButton.setOnClickListener(onClickListener)


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val navigationRail = findViewById<BottomNavigationView>(R.id.navigationRail)
        bottomNavigation.selectedItemId = R.id.nav_home
        // navigationRail.selectedItemId = R.id.navigation_home
        BottomNavigationHandler.setupBottomNavigation(bottomNavigation, this)


    }
}