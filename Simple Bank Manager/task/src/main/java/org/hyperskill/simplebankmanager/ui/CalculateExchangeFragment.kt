package org.hyperskill.simplebankmanager.ui

import androidx.fragment.app.Fragment
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.BundleCompat.getSerializable
import org.hyperskill.simplebankmanager.R
import java.util.Locale

class CalculateExchangeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculate_exchange, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exchangeMap = arguments?.getSerializable(
            "exchangeMap",
            HashMap::class.java
        )
                as HashMap<String, Map<String, Double>>

        val fromSpinner = view.findViewById<Spinner>(R.id.calculateExchangeFromSpinner)
        val toSpinner = view.findViewById<Spinner>(R.id.calculateExchangeToSpinner)
        val amountEditText = view.findViewById<EditText>(R.id.calculateExchangeAmountEditText)
        val calculateButton = view.findViewById<Button>(R.id.calculateExchangeButton)
        val displayTextView = view.findViewById<TextView>(R.id.calculateExchangeDisplayTextView)

        fromSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            exchangeMap.keys.toList()
        )

        toSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            exchangeMap.keys.toList()
        )

        toSpinner.setSelection(1)

        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val fromCurrency = fromSpinner.selectedItemPosition
                val toCurrency = toSpinner.selectedItemPosition
                if (fromCurrency == toCurrency) {
                    Toast.makeText(
                        activity,
                        "Cannot convert to same currency",
                        Toast.LENGTH_LONG
                    ).show()

                    val lastIndex = toSpinner.adapter.count - 1

                    val newPosition = if ((toSpinner.selectedItemPosition + 1) > lastIndex)
                        0 else toSpinner.selectedItemPosition + 1
                    toSpinner.setSelection(newPosition)
                }
            }


        }

        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val fromCurrency = fromSpinner.selectedItemPosition
                val toCurrency = toSpinner.selectedItemPosition
                if (fromCurrency == toCurrency) {
                    Toast.makeText(
                        activity,
                        "Cannot convert to same currency",
                        Toast.LENGTH_LONG
                    ).show()

                    val lastIndex = toSpinner.adapter.count - 1

                    val newPosition = if ((toSpinner.selectedItemPosition + 1) > lastIndex)
                        0 else toSpinner.selectedItemPosition + 1
                    toSpinner.setSelection(newPosition)
                }
            }
        }

        calculateButton.setOnClickListener {
            val fromCurrency = fromSpinner.selectedItem.toString()
            val toCurrency = toSpinner.selectedItem.toString()

            val amount = amountEditText.text.toString().toDoubleOrNull()
            if (amount == null) {
                Toast.makeText(
                    activity,
                    "Enter amount",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val gbpCharacter = '\u00A3'
            val euroCharacter = '\u20AC'
            val usdCharacter = '\u0024'

            val currencyMap = mapOf(
                "USD" to usdCharacter,
                "GBP" to gbpCharacter,
                "EUR" to euroCharacter
            )

            val exchangeRate = exchangeMap[fromCurrency]?.get(toCurrency)
            val convertedAmount = amount * exchangeRate!!

            val amountString = String.format(Locale.US, "%.2f", amount)
            val convertedAmountString = String.format(Locale.US, "%.2f", convertedAmount)

            val resultText = "${currencyMap[fromCurrency]}$amountString =" +
                    " ${currencyMap[toCurrency]}$convertedAmountString"

            displayTextView.text = resultText

        }
    }
}


