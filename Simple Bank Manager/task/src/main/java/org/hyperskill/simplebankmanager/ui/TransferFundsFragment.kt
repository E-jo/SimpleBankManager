package org.hyperskill.simplebankmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.hyperskill.simplebankmanager.R
import java.util.Locale

class TransferFundsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transfer_funds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var balance = arguments?.getDouble("balance")
        val username = arguments?.getString("username")

        val transferFundsButton = view.findViewById<TextView>(R.id.transferFundsButton)
        val accountText = view.findViewById<TextView>(R.id.transferFundsAccountEditText)
        val amountText = view.findViewById<TextView>(R.id.transferFundsAmountEditText)

        transferFundsButton.setOnClickListener {
            if (accountText.text.isEmpty()) {
                accountText.error = "Invalid account number"
                return@setOnClickListener
            }
            if (amountText.text.isEmpty()) {
                amountText.error = "Invalid amount"
                return@setOnClickListener
            }

            val account = accountText
                .text
                .toString()
            val amount = amountText
                .text
                .toString()
                .toDouble()

            println("Amount: $amount")
            println("Account number: $account")

            if (!account.matches("^(sa|ca)\\d{4}\$".toRegex())) {
                println("Invalid account number: $account")
                accountText.error = "Invalid account number"
                return@setOnClickListener
            }

            if (amount <= 0.0) {
                println("Zero or negative amount")
                amountText.error = "Invalid amount"
                return@setOnClickListener
            }

            if (amount > balance!!) {
                println("Not enough funds to transfer $amount")
                Toast.makeText(
                    activity,
                    "Not enough funds to transfer $${String.format(Locale.US, "%.2f", amount)}",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            println("Transferring ${String.format(Locale.US, "%.2f", amount)} to $account")

            balance = balance!! - amount
            Toast.makeText(
                activity,
                "Transferred $${String.format(Locale.US, "%.2f", amount)} to account $account",
                Toast.LENGTH_LONG
            ).show()

            val bundle = Bundle()
            bundle.putDouble("balance", balance.toDouble())
            bundle.putString("username", username)

            findNavController().navigate(
                R.id.action_transferFundsFragment_to_userMenuFragment,
                bundle)
        }
    }
}

