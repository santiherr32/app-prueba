package com.example.finanzapp

import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Currency
import java.util.Locale

data class Transaction(
    val name: String,
    val type: String,
    val category: String,
    val amount: BigDecimal,
    val account: String,
    val date: LocalDateTime = LocalDate.now().atStartOfDay(),
) {
}
