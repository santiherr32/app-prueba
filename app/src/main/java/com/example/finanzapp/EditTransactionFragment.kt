package com.example.finanzapp

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class EditTransactionFragment : BottomSheetDialogFragment() {

    private var currentTransactionType: String = "income"
    private var defaultButtonTextColor: Int = 0
    private lateinit var transactionAmountLabel: TextView
    private lateinit var transactionDateView: CardView
    private lateinit var selectedDateTextView: TextView
    private var transaction: Transaction? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_edit_transaction_sheet, container, false)
        transactionDateView = view.findViewById(R.id.transaction_date_edit)
        selectedDateTextView = view.findViewById(R.id.transaction_date)

        // Retrieve the transaction object from arguments
        transaction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("transaction", Transaction::class.java)
        } else {
            arguments?.getParcelable("transaction")
        }

        val transactionNameEditText = view.findViewById<TextInputEditText>(R.id.transaction_name_input)
        val transactionAmountEditText = view.findViewById<TextInputEditText>(R.id.transaction_amount_input)
        val transactionCategory = view.findViewById<TextView>(R.id.transaction_category)
        val transactionAccount = view.findViewById<TextView>(R.id.transaction_account)
        transactionNameEditText.setText(transaction?.name ?: "")
        transactionAmountEditText.setText(transaction?.amount.toString())
        transactionCategory.text = transaction?.category.toString()
        transactionAccount.text = transaction?.account.toString()

        // Use the transaction object to get the transaction type
        val currentTransactionType = transaction?.type

        // Find the button group
        val transactionTypeToggleGroup: MaterialButtonToggleGroup = view.findViewById(R.id.transactionTypeToggleGroup)

        // Check the corresponding button based on the transaction type
        when (currentTransactionType) {
            "income" -> {
                // Check the income button
                transactionTypeToggleGroup.check(R.id.incomeButton)
            }
            "expense" -> {
                // Check the expense button
                transactionTypeToggleGroup.check(R.id.expenseButton)
            }
        }

        transaction?.let {
            selectedDateTextView.text = it.formatTransactionDate()
        }

        transactionDateView.setOnClickListener {
            showDatePicker()
        }

        return view
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            // Convert the selected date to the required format
            val selectedDate = LocalDateTime.ofEpochSecond(selection / 1000, 0, ZoneOffset.UTC)

            transaction?.date = selectedDate
            selectedDateTextView.text = transaction?.formatTransactionDate()// Display the formatted date
            //(activity as? HomeActivity)?.updateTransaction(transaction)
        }

        datePicker.show(parentFragmentManager, "datePicker")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.peekHeight =
            resources.getDimensionPixelSize(R.dimen.add_transaction_bottom_sheet_height)
        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defaultButtonTextColor =
            view.findViewById<MaterialButton>(R.id.incomeButton).currentTextColor
        transactionAmountLabel = view.findViewById(R.id.transaction_amount_label)

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)

        // Add click listeners
        val doneButton = view.findViewById<MaterialButton>(R.id.done_button)

        doneButton.setOnClickListener {

        }

        val cancelButton = view.findViewById<MaterialButton>(R.id.cancel_button)

        cancelButton?.setOnClickListener {
            dismiss()
        }

        updateTransactionAmountLabel()

        val transactionTypeToggleGroup: MaterialButtonToggleGroup =
            view.findViewById(R.id.transactionTypeToggleGroup)
        transactionTypeToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                currentTransactionType = when (checkedId) {
                    R.id.incomeButton -> "income"
                    R.id.expenseButton -> "expense"
                    R.id.transferButton -> "transfer"
                    else -> "income"
                }
                when (checkedId) {
                    R.id.incomeButton -> {

                    }

                    R.id.expenseButton -> {

                    }

                    R.id.transferButton -> {

                    }
                }
                updateTransactionAmountLabel()
            }
        }
    }

    private fun updateTransactionAmountLabel() {
        transactionAmountLabel.text = when (currentTransactionType) {
            "income" -> "¿Cuanto recibiste?"
            "expense" -> "¿Cuanto gastaste?"
            "transfer" -> "¿Cuanto vas a transferir?"
            else -> "¿Cuanto recibiste?"
        }
    }


    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)

            behavior.isFitToContents = true
            behavior.isDraggable = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED


        }
    }

}



