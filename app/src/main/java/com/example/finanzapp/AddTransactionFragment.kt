package com.example.finanzapp

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup


class AddTransactionFragment : BottomSheetDialogFragment() {

    private var currentTransactionType: String = "income"
    private var defaultButtonTextColor: Int = 0
    private lateinit var transactionAmountLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_add_transaction_sheet, container, false)
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
        val addButton = view.findViewById<MaterialButton>(R.id.add_button)

        addButton.setOnClickListener {
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



