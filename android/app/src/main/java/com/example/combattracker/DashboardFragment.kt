package com.example.combattracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.combattracker.Constants.FN_C_ROLL
import com.example.combattracker.Constants.FN_D_SIDES
import com.example.combattracker.Constants.FN_R_ROLL
import com.example.combattracker.Constants.FS_COL_ROLLS
import com.example.combattracker.Constants.FS_DOC_USER
import com.example.combattracker.Constants.FS_DOC_VAL
import com.example.combattracker.Constants.LOG_TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

/**
 * A [Fragment] that houses general information/actions for the user.
 */
class DashboardFragment : Fragment() {
    var editSides: EditText? = null
    var txtRoll: TextView? = null
    var txtHistory: TextView? = null

    var db: FirebaseFirestore? = null
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Firebase.auth.currentUser == null) {
            goToLogin()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)

        rootView.findViewById<Button>(R.id.button_signout).setOnClickListener {
            Firebase.auth.signOut()
            goToLogin()
        }

        rootView.findViewById<Button>(R.id.button_compose).setOnClickListener {
            startActivity(Intent(context, PlayerView::class.java))
        }

        editSides = rootView.findViewById(R.id.edit_sides)
        txtRoll = rootView.findViewById(R.id.txt_roll)

        rootView.findViewById<Button>(R.id.button_roll).setOnClickListener {
            editSides?.let {
                val input = it.text
                if(input.isEmpty()) {
                    Toast.makeText(context, "Please enter number of sides.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val data = hashMapOf(FN_D_SIDES to input.toString().toInt())
                Firebase.functions.getHttpsCallable(FN_C_ROLL).call(data).continueWith { task ->
                    if (task.isSuccessful) {
                        val result = task.result?.data as? Map<String, Int>
                        val value = result?.get(FN_R_ROLL)
                        txtRoll!!.text = value.toString()
                        registerRoll(value)
                    } else {
                        Log.e(LOG_TAG, task.exception?.message.orEmpty())
                    }
                }
            }
        }

        txtHistory = rootView.findViewById(R.id.txt_history)
        db = Firebase.firestore
        auth = Firebase.auth
        db?.apply {
            collection(FS_COL_ROLLS)
                .whereNotEqualTo(FS_DOC_USER, auth?.currentUser?.uid)
                .addSnapshotListener {snapshots, e ->
                    if (e != null) {
                        Log.e(LOG_TAG, "listen:error", e)
                        return@addSnapshotListener
                    }

                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> txtHistory?.text = (dc.document.data.get(FS_DOC_VAL) as Long).toString()
                            else -> Log.d(LOG_TAG, "Ignoring roll from snapshot.")
                        }
                    }
                }
        }

        return rootView
    }

    private fun registerRoll(roll: Int?) {
        val data = hashMapOf(FS_DOC_USER to auth?.currentUser?.uid, FS_DOC_VAL to roll)
        db?.apply {
            collection(FS_COL_ROLLS).add(data).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "Stored roll to database.")
                } else {
                    Log.e(LOG_TAG, task.exception?.message.orEmpty())
                }
            }
        }
    }

    private fun goToLogin() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<LoginFragment>(R.id.fragment_container)
        }
    }
}