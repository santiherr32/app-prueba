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


class TransactionsActivity : AppCompatActivity(), CardAdapter.OnItemClickListener {

    private lateinit var dataList: MutableList<Transaction>
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

        dataList = mutableListOf(
            Transaction(
                "Movimiento 1",
                "expense",
                Category.SALUD,
                BigDecimal("50000"),
                Account.BANCO_AV_VILLAS,
                LocalDate.of(2024, 6, 1).atStartOfDay()
            ),
            Transaction(
                "Movimiento 2",
                "income",
                Category.SALUD,
                BigDecimal("10000"),
                Account.DAVIVIENDA,
                LocalDateTime.of(2024, 6, 1, 5, 30)
            )
        ) // Example data
        val adapter = CardAdapter(dataList, this)
        recyclerView.adapter = adapter

        val extendedFab = findViewById<ExtendedFloatingActionButton>(R.id.add_transaction_button)
        extendedFab.setOnClickListener {
            val addTransactionFragment = AddTransactionFragment()
            addTransactionFragment.show(supportFragmentManager, addTransactionFragment.tag)
        }


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val navigationRail = findViewById<BottomNavigationView>(R.id.navigationRail)
        bottomNavigation.selectedItemId = R.id.nav_transactions
        // navigationRail.selectedItemId = R.id.navigation_home

        BottomNavigationHandler.setupBottomNavigation(bottomNavigation, this)


    }

    override fun onItemClick(position: Int) {
        val clickedTransaction = dataList[position]
        val viewTransactionFragment = ViewTransactionFragment()

        // Pass data to the fragment using arguments
        val args = Bundle().apply {
            putParcelable("transaction", clickedTransaction)
        }
        viewTransactionFragment.arguments = args

        viewTransactionFragment.show(supportFragmentManager, viewTransactionFragment.tag)
    }
}