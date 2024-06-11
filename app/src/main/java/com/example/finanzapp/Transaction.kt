package com.example.finanzapp

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Currency
import java.util.Locale

enum class Account {
    BANCOLOMBIA,
    DAVIVIENDA,
    BANCO_DE_BOGOTA,
    BANCO_POPULAR,
    BANCO_DE_OCCIDENTE,
    BANCO_CAJA_SOCIAL,
    BANCO_AV_VILLAS,
    BANCOOMEVA,
    BANCO_FALABELLA,
    NEQUI,
    RAPPIPAY,
    DAVIPLATA;

    override fun toString(): String {
        return name.split("_").joinToString(" ") { it ->
            it.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                ) else it.toString()
            }
        }
    }
}

enum class Category {
    ALIMENTACION_Y_COMIDA,
    TRANSPORTE,
    COMPRAS,
    ENTRETENIMIENTO,
    SALUD,
    FACTURAS_Y_SERVICIOS_PUBLICOS,
    CASA,
    VIAJES,
    REGALOS_Y_DONACIONES,
    EDUCACION;

    override fun toString(): String {
        return name.split("_").joinToString(" ") { it ->
            it.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                ) else it.toString()
            }
        }
    }
}


data class Transaction(
    val name: String,
    val type: String,
    val category: Category,
    val amount: BigDecimal,
    val account: Account,
    var date: LocalDateTime = LocalDate.now().atStartOfDay(),
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readSerializable() as Category,
        BigDecimal(parcel.readString() ?: "0"),
        parcel.readSerializable() as Account,
        LocalDateTime.parse((parcel.readString() ?: LocalDate.now().atStartOfDay()).toString())
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeSerializable(category)
        parcel.writeSerializable(account)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }

    fun formatTransactionAmount(): String {
        val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        return formatter.format(amount)
    }

    fun formatTransactionDate(): String {
        val currentDateTime = LocalDateTime.now()
        val diffInMinutes = ChronoUnit.MINUTES.between(date, currentDateTime)
        val diffInHours = ChronoUnit.HOURS.between(date, currentDateTime)
        val diffInDays = ChronoUnit.DAYS.between(date, currentDateTime)

        return when {
            diffInMinutes < 60 -> "Hace $diffInMinutes minutos"
            diffInHours < 24 -> "Hace $diffInHours horas"
            diffInDays < 30 -> "Hace $diffInDays dias"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")
                date.format(formatter)
            }
        }
    }
}
