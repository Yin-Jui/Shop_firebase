package com.app.shop_firebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient    //must be created in the future
    private val RC_GOOGLE_SIGN_IN = 102
    private val TAG = SignIn::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)    //登入相關選項
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        google_sign_in.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, RC_GOOGLE_SIGN_IN)
        }

        signup.setOnClickListener { view ->
            signUp()
        }
        login.setOnClickListener {
            login()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_GOOGLE_SIGN_IN)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)   //登入工作完的物件:data
            val account = task.getResult(ApiException::class.java)
            Log.d(TAG,"onActivityResult:  ${account?.id}")     //account might be null
            val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
            FirebaseAuth.getInstance()
                .signInWithCredential(credential)
                .addOnCompleteListener {task->
                    if(task.isSuccessful){
                        setResult(Activity.RESULT_OK)
                        finish()
                    }else{
                        Log.d(TAG,"onActivityResult:  ${task.exception?.message}")
                        Snackbar.make(emaill,"Firebase authentication failed",Snackbar.LENGTH_LONG)
                    }

                }
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
