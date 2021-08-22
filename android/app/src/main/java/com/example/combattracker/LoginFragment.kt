package com.example.combattracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.combattracker.Constants.FS_COL_USERS
import com.example.combattracker.Constants.FS_KEY_EMAIL
import com.example.combattracker.Constants.KEY_EMAIL_ADDRESS
import com.example.combattracker.Constants.KEY_SIGN_IN_LINK
import com.example.combattracker.Constants.LOG_TAG
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A [Fragment] that handles user login.
 */
class LoginFragment : Fragment() {
    private var inputEmail: EditText? = null
    private var buttonSubmit: Button? = null

    val actionCodeSettings = actionCodeSettings {
        url = "https://combat-tracker-3dd9d.web.app/finishSignUp"
        handleCodeInApp = true
        setAndroidPackageName(
            "com.example.combattracker",
            false,
            null
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Firebase.auth.currentUser != null) {
            goToDashboard()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        inputEmail = rootView.findViewById(R.id.edit_email)
        buttonSubmit = rootView.findViewById(R.id.button_submit)

        buttonSubmit?.setOnClickListener {
            val email = inputEmail?.text?.toString().orEmpty()
            if (!validateEmail(email)) {
                Log.w(LOG_TAG, "Invalid email address format.")
                return@setOnClickListener
            }
            Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Successfully sent email.", Toast.LENGTH_SHORT)
                            .show()
                        // Store email in case user continues sign-up here
                        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
                        with (prefs!!.edit()) {
                            putString(KEY_EMAIL_ADDRESS, email)
                            apply()
                        }
                    } else {
                        Log.e(LOG_TAG, task.exception?.message.orEmpty())
                    }
                }
        }

        return rootView
    }

    private fun validateEmail(email: String?): Boolean {
        if (email == null || email.length < 5) {
            return false
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun goToDashboard() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<DashboardFragment>(R.id.fragment_container)
        }
    }
}