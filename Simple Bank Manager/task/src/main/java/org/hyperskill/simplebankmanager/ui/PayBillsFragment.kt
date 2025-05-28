package org.hyperskill.simplebankmanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.app.AlertDialog
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import org.hyperskill.simplebankmanager.R
import java.util.Locale


class PayBillsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pay_bills, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var balance = arguments?.getDouble("balance")
        val username = arguments?.getString("username")
        Log.d("PayBillsFragment", "Balance: $balance")
        Log.d("PayBillsFragment", "Username: $username")

        val billInfoMap = arguments?.getSerializable("billInfoMap")
                as HashMap<String, Triple<String, String, Double>>

        val payBillsCodeInputEditText = view.findViewById<TextView>(R.id.payBillsCodeInputEditText)
        val payBillsShowBillInfoButton = view.findViewById<TextView>(R.id.payBillsShowBillInfoButton)

        payBillsShowBillInfoButton.setOnClickListener {
            val code = payBillsCodeInputEditText.text.toString()

            if (!billInfoMap.containsKey(code)) {
                Log.d("PayBillsFragment", "Code: $code")
                context?.let { context ->
                    AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("Wrong code")
                        .setPositiveButton("Ok") {_, _ -> }
                        .show()
                }
            } else {
                val billInfo = billInfoMap[code]
                context?.let { context ->
                    AlertDialog.Builder(context)
                        .setTitle("Bill info")
                        .setMessage("Name: ${billInfo!!.first}\n" +
                                "Billcode: ${billInfo.second}\n" +
                                "Amount: $${String.format(Locale.US, "%.2f", billInfo.third)}")
                        .setPositiveButton("Confirm") {_, _ ->
                            if (balance!! >= billInfo.third) {
                                balance = balance!! - billInfo.third
                                Toast.makeText(
                                    activity,
                                    "Payment for bill ${billInfo.first}, was successful",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                context.let { context ->
                                    AlertDialog.Builder(context)
                                        .setTitle("Error")
                                        .setMessage("Not enough funds")
                                        .setPositiveButton("Ok") { _, _ -> }
                                        .show()
                                }
                            }
                        }
                        .setNegativeButton("Cancel") {_, _ -> }
                        .show()
                }
            }
        }

        requireActivity().
        onBackPressedDispatcher.
        addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bundle = Bundle()
                balance?.let { bundle.putDouble("balance", it) }
                bundle.putString("username", username)

                findNavController().navigate(R.id.userMenuFragment, bundle)
            }
        })
    }
}

