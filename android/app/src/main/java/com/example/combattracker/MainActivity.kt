package com.example.combattracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.combattracker.Constants.KEY_EMAIL_ADDRESS
import com.example.combattracker.Constants.KEY_SIGN_IN_LINK
import com.example.combattracker.Constants.LOG_TAG
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (auth.currentUser == null) { // No signed-in user
            val emailLink = intent.data.toString()
            if(auth.isSignInWithEmailLink(emailLink)) {
                // App was launched from sign in link
                handleSignIn(emailLink)
            } else {
                // App was normally launched
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<LoginFragment>(R.id.fragment_container)
                }
            }
        } else { // User is already signed-in
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<DashboardFragment>(R.id.fragment_container)
            }
        }
    }

    private fun handleSignIn(emailLink: String) {
        val prefs = getPreferences(Context.MODE_PRIVATE)
        val email = prefs.getString(KEY_EMAIL_ADDRESS, "").orEmpty()

        if(email.isEmpty()) {
            Toast.makeText(applicationContext, "Error trying to sign in. Please try again.", Toast.LENGTH_SHORT).show()
            return
        }

        // Sign the user in with the stored email address
        auth.signInWithEmailLink(email, emailLink)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val isNewUser = task.result.additionalUserInfo?.isNewUser == true
                    Log.i(LOG_TAG, "isNewUser: $isNewUser")
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        add<DashboardFragment>(R.id.fragment_container)
                    }
                } else {
                    Log.e(LOG_TAG, task.exception?.message.orEmpty())
                }
            }
    }
}