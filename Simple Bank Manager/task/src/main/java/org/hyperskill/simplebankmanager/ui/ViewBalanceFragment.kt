package org.hyperskill.simplebankmanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.hyperskill.simplebankmanager.R
import java.util.Locale


class ViewBalanceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_balance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val balance = arguments?.getDouble("balance") ?: 100.00
        val balanceTextView = view.findViewById<TextView>(R.id.viewBalanceAmountTextView)
        val balanceAmount = "$${String.format(Locale.US, "%.2f", balance)}"
        balanceTextView.text = balanceAmount
    }
}