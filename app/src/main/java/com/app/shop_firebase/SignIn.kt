package com.app.shop_firebase

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        signup.setOnClickListener { view ->
            signUp()
        }
        login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AlertDialog.Builder(this@SignIn)
                        .setTitle("Log In")
                        .setPositiveButton("OK") { dialog, which ->
                            setResult(Activity.RESULT_OK)
                            finish()
                        }.show()
                } else {
                    AlertDialog.Builder(this@SignIn)
                        .setTitle("Sign In")
                        .setMessage(task.exception?.message)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
    }

    private fun signUp() {
        val email = email.text.toString()
        val password = password.text.toString()
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                //The result of the authority can be gained from task
                if (task.isSuccessful) {
                    AlertDialog.Builder(this@SignIn)
                        .setTitle("Sign In")
                        .setMessage("Account Created")
                        .setPositiveButton("OK") { dialog, which ->
                            setResult(Activity.RESULT_OK)
                            finish()
                        }.show()
                } else {
                    AlertDialog.Builder(this@SignIn)
                        .setTitle("Sign In")
                        .setMessage(task.exception?.message)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
    }
}
