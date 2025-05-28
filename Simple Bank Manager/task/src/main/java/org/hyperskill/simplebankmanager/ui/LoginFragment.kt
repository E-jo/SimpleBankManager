package org.hyperskill.simplebankmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.hyperskill.simplebankmanager.R
import org.hyperskill.simplebankmanager.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameEditText = binding.loginUsername
        val passwordEditText = binding.loginPassword
        val loginButton = binding.loginButton

        loginButton.setOnClickListener {
            val validUsername = activity?.intent?.getStringExtra("username") ?: "Lara"
            val validPassword = activity?.intent?.getStringExtra("password") ?: "1234"

            val submittedUsername = usernameEditText.text.toString()
            val submittedPassword = passwordEditText.text.toString()

            println("Username from arguments: $validUsername")
            println("Password from arguments: $validPassword")

            println("Submitted username: $submittedUsername")
            println("Submitted password: $submittedPassword")

            println()

            if (submittedUsername != validUsername ||
                submittedPassword != validPassword) {
                Toast.makeText(activity, "invalid credentials", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity, "logged in", Toast.LENGTH_LONG).show()
                val bundle = Bundle()
                bundle.putString("username", submittedUsername)
                findNavController().navigate(R.id.action_loginFragment_to_userMenuFragment,
                    bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}