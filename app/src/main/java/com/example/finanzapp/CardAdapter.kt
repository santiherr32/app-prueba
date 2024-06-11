package com.example.finanzapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Currency
import java.util.Locale

class CardAdapter(
    private val dataList: List<Transaction>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(itemView: View, private val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val transactionName: TextView = itemView.findViewById(R.id.transaction_name)
        val categorySymbol: ImageView = itemView.findViewById(R.id.category_symbol)
        val transactionTypeBadge: ImageView = itemView.findViewById(R.id.transaction_type_badge)
        val transactionAmount: TextView = itemView.findViewById(R.id.transaction_amount)
        val transactionAccount: TextView = itemView.findViewById(R.id.transaction_account)
        val transactionDate: TextView = itemView.findViewById(R.id.transaction_date)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_item_card, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTransaction = dataList[position]

        // Format transaction amount as currency
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CO")) // Locale for Colombia
        format.currency = Currency.getInstance("COP") // Set currency to COP
        val formattedAmount = format.format(currentTransaction.amount)

        // Check if the decimal part is zero
        val amountString = format.format(currentTransaction.amount)
        val decimalPart = if ('.' in amountString) {
            amountString.split(".")[1]
        } else {
            ""
        }

        val displayAmount = if (decimalPart == "00") {
            amountString.split(",")[0] // Remove decimal part
        } else {
            formattedAmount
        }

        val amountTextColor = when (currentTransaction.type) {
            "income" -> 0xFF2C7756.toInt()
            "expense" -> 0xFFEB4639.toInt()
            else -> holder.transactionAmount.currentTextColor
        }

        holder.transactionAmount.apply {
            text = displayAmount
            setTextColor(amountTextColor)
        }
        holder.transactionAccount.text = currentTransaction.account.toString()

        val formattedDate = formatTransactionDate(currentTransaction.date)
        holder.transactionDate.text = formattedDate

        holder.transactionName.text = currentTransaction.name


        val badgeImageResource = when (currentTransaction.type) {
            "income" -> R.drawable.upward_arrow
            "expense" -> R.drawable.downward_arrow
            else -> null
        }
        if (badgeImageResource != null) {
            holder.transactionTypeBadge.visibility = View.VISIBLE
            holder.transactionTypeBadge.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context, badgeImageResource
                )
            )
        } else {
            holder.transactionTypeBadge.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun formatTransactionDate(date: LocalDateTime): String {
        val currentDateTime = LocalDateTime.now()
        val diffInMinutes = ChronoUnit.MINUTES.between(date, currentDateTime)
        val diffInHours = ChronoUnit.HOURS.between(date, currentDateTime)
        val diffInDays = ChronoUnit.DAYS.between(date, currentDateTime)

        return when {
            diffInMinutes < 60 -> "$diffInMinutes minutes ago"
            diffInHours < 24 -> "$diffInHours hours ago"
            diffInDays < 30 -> "$diffInDays days ago"
            else -> {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")
                date.format(formatter)
            }
        }
    }
}