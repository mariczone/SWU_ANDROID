package com.giscompany.foodie2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.giscompany.foodie2.Util.getLoading
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth


    lateinit var navController: NavController
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var tvSwapMode: TextView
    lateinit var nextBtn: Button

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var isLoginForm: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        navController = findNavController()
        nextBtn = view.findViewById(R.id.goToOnbaordBtn) as Button;
        etEmail = view.findViewById<EditText>(R.id.etEmailInput)
        etPassword = view.findViewById<EditText>(R.id.etPasswordInput)
        tvSwapMode = view.findViewById<TextView>(R.id.swapToRegisterBtn)
        nextBtn.setOnClickListener {
            val email = etEmail.text.toString().trim { it <= ' ' }
            val password = etPassword.text.toString().trim { it <= ' ' }
//            Toast.makeText(activity, "Email: $email, Password: $password", Toast.LENGTH_SHORT).show()
            if (!isLoginForm) {
                register(email, password)
            } else {
                login(email, password)
            }
        }
        tvSwapMode.setOnClickListener {
            isLoginForm = !isLoginForm;
            if (isLoginForm) {
                nextBtn.text = "LOGIN"
                tvSwapMode.text = "SignUp"
            } else {
                nextBtn.text = "REGISTER"
                tvSwapMode.text = "SignIn"
            }
        }
    }

    private fun register(email: String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Case Success
                mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener{
                    Toast.makeText(activity, "Create account successfully!, Please check your email for verification", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, password: String) {
        var dialog = getLoading()
        dialog.show()
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            dialog.dismiss()
            if (!task.isSuccessful) {
                Toast.makeText(activity, "Authentication Failed: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
            } else {
                // Case Login Success
                navController.navigate(R.id.action_loginFragment_to_onboardingFragment)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}