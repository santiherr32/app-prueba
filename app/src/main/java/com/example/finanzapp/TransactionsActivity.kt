package com.example.finanzapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime


class TransactionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (getResources().configuration.screenWidthDp >= 600) {
            setContentView(R.layout.activity_transactions)
        } else {
            setContentView(R.layout.activity_transactions)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView: RecyclerView = findViewById(R.id.trans_list)
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

        val extendedFab = findViewById<ExtendedFloatingActionButton>(R.id.add_transaction_button)
        extendedFab.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val navigationRail = findViewById<BottomNavigationView>(R.id.navigationRail)
        bottomNavigation.selectedItemId = R.id.nav_transactions
        // navigationRail.selectedItemId = R.id.navigation_home

        BottomNavigationHandler.setupBottomNavigation(bottomNavigation, this)


    }
}