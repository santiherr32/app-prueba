package com.example.finanzapp

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup


class ViewTransactionFragment : BottomSheetDialogFragment() {

    private var currentTransactionType: String = "income"
    private lateinit var transactionAmountLabel: TextView
    private lateinit var transaction: Transaction



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_view_transaction_sheet, container, false)

        transaction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("transaction", Transaction::class.java)
        } else {
            arguments?.getParcelable("transaction")
        }!!

        // Check if transaction is not null before using it
        if (transaction != null) {
            val formattedTransactionDate = transaction.formatTransactionDate()
            val formattedTransactionAmount= transaction.formatTransactionAmount()
            // Now you can use the properties and methods of the Transaction object
            val transactionName = transaction.name
            val transactionType = transaction.type
            val transactionAccount = transaction.account
            val transactionCategory = transaction.category
            // Access other properties as needed

            val transactionNameView = rootView.findViewById<TextView>(R.id.transaction_name)
            val transactionAmountView = rootView.findViewById<TextView>(R.id.transaction_amount)
            val transactionDateView = rootView.findViewById<TextView>(R.id.transaction_date)
            val transactionCategoryView = rootView.findViewById<TextView>(R.id.transaction_category)
            val transactionAccountView = rootView.findViewById<TextView>(R.id.transaction_account)

            transactionNameView?.text = transactionName
            currentTransactionType = transactionType
            transactionAmountView?.text = formattedTransactionAmount
            transactionDateView.text = formattedTransactionDate
            transactionAccountView.text = transactionAccount.toString()
            transactionCategoryView.text = transactionCategory.toString()

        }

        val editButton = rootView.findViewById<MaterialButton>(R.id.edit_button)
        editButton.setOnClickListener {
            val editTransactionFragment = EditTransactionFragment()
            editTransactionFragment.arguments = Bundle().apply {
                putParcelable("transaction", transaction)
            }
            editTransactionFragment.show(parentFragmentManager, "EditTransactionFragment")
        }


        return rootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.peekHeight =
            resources.getDimensionPixelSize(R.dimen.add_transaction_bottom_sheet_height)
        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionAmountLabel = view.findViewById(R.id.transaction_amount_label)

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)

        val incomeOption = view.findViewById<MaterialButton>(R.id.incomeOption)
        val expenseOption = view.findViewById<MaterialButton>(R.id.expenseOption)

        when (currentTransactionType) {
            "income" -> {
                incomeOption.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
                incomeOption.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            "expense" -> {
                expenseOption.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
                expenseOption.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }



        val deleteButton = view.findViewById<MaterialButton>(R.id.delete_button)

        deleteButton?.setOnClickListener {
            dismiss()
        }

        updateTransactionAmountLabel()

        val transactionTypeToggleGroup: MaterialButtonToggleGroup =
            view.findViewById(R.id.transaction_type_group)
        //transactionTypeToggleGroup.check()
        /*transactionTypeToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
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
        }*/
    }

    private fun updateTransactionAmountLabel() {
        transactionAmountLabel.text = when (currentTransactionType) {
            "income" -> "Recibiste:"
            "expense" -> "Gastaste:"
            else -> "Recibiste:"
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



