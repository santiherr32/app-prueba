package com.example.finanzapp

import android.content.Context
import android.content.Intent
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView

object BottomNavigationHandler {

    fun setupBottomNavigation(
        bottomNavigationView: BottomNavigationView,
        context: Context
    ) {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> navigateToActivity(HomeActivity::class.java, context)
                R.id.nav_transactions -> navigateToActivity(TransactionsActivity::class.java, context)
            }
            true // Return true to indicate that the item selection event is handled
        }
    }

    private fun navigateToActivity(activityClass: Class<*>, context: Context) {
        val intent = Intent(context, activityClass)
        context.startActivity(intent)
    }
}