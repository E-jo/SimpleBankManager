package org.hyperskill.simplebankmanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import org.hyperskill.simplebankmanager.R


class UserMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val username = arguments?.getString("username")
        val welcomeTextView = view.findViewById<TextView>(R.id.userMenuWelcomeTextView)
        val welcomeMessage = "Welcome $username"
        welcomeTextView.text = welcomeMessage

        var balance = activity?.intent?.getDoubleExtra("balance", 100.0)

        if (arguments?.containsKey("balance") == true) {
            println("Argument balance: ${arguments?.getDouble("balance")}")
            balance = arguments?.getDouble("balance")
        }

        val bundle = Bundle()
        balance?.let { bundle.putDouble("balance", it) }
        bundle.putString("username", username)

        println("Balance: $balance")

        val viewBalanceButton = view.findViewById<Button>(R.id.userMenuViewBalanceButton)
        viewBalanceButton.setOnClickListener {
            findNavController().navigate(R.id.action_userMenuFragment_to_viewBalanceFragment,
                bundle)
        }

        val transferFundsButton = view.findViewById<Button>(R.id.userMenuTransferFundsButton)
        transferFundsButton.setOnClickListener {
            findNavController().navigate(R.id.action_userMenuFragment_to_transferFundsFragment,
                bundle)
        }

        val calculateExchangeButton =
            view.findViewById<Button>(R.id.userMenuExchangeCalculatorButton)
        calculateExchangeButton.setOnClickListener {
            val defaultMap = hashMapOf(
                "EUR" to hashMapOf(
                    "GBP" to 0.5,
                    "USD" to 2.0
                ),
                "GBP" to hashMapOf(
                    "EUR" to 2.0,
                    "USD" to 4.0
                ),
                "USD" to hashMapOf(
                    "EUR" to 0.5,
                    "GBP" to 0.25
                )
            )
            val exchangeMapFromIntent = activity?.intent?.getSerializableExtra("exchangeMap")
                    as? HashMap<String, Map<String, Double>>
            val exchangeMap = exchangeMapFromIntent ?: defaultMap
            bundle.putSerializable("exchangeMap", exchangeMap)
            findNavController().navigate(R.id.action_userMenuFragment_to_calculateExchangeFragment,
                bundle)
        }

        val payBillsButton = view.findViewById<Button>(R.id.userMenuPayBillsButton)
        payBillsButton.setOnClickListener {
            val defaultBillInfoMap = hashMapOf(
                "ELEC" to Triple("Electricity", "ELEC", 45.0),
                "GAS" to Triple("Gas", "GAS", 20.0),
                "WTR" to Triple("Water", "WTR", 25.5)
            )

            println("Intent extras: ${activity?.intent?.extras}")
            val extras = activity?.intent?.extras
            extras?.keySet()?.forEach { key ->
                println("Key: $key, Value: ${extras.get(key)}, " +
                        "Type: ${extras.get(key)?.javaClass?.name}")
            }

            val rawBillInfo = extras?.getSerializable("billInfo") as? Map<String, Any>
            println("Raw bill info: $rawBillInfo")

            // convert SingletonMap to HashMap
            val billInfoMapFromIntent: HashMap<String, Triple<String, String, Double>>? =
                rawBillInfo?.mapValues { entry ->
                val value = entry.value.toString()
                val parts = value.removeSurrounding("(", ")").split(", ")
                Triple(parts[0], parts[1], parts[2].toDouble())
            }?.let { HashMap(it) }

            println("Bill info as HashMap: $billInfoMapFromIntent")

            val billInfoMap = billInfoMapFromIntent ?: defaultBillInfoMap

            bundle.putSerializable("billInfoMap", billInfoMap)
            balance?.let { it1 -> bundle.putDouble("balance", it1) }

            findNavController().navigate(R.id.action_userMenuFragment_to_payBillsFragment,
                bundle)
        }
    }
}