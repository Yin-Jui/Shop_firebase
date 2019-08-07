package com.app.shop_firebase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {


    private val RC_SIGNIN = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        verify_email.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(it, "verify email sent", Snackbar.LENGTH_LONG).show()

                    }
                }                                                                    //not sure to be created successfully
        }
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_signin -> {
                startActivityForResult(Intent(this, SignIn::class.java), RC_SIGNIN)
                true
            }
            R.id.action_signout -> {
                FirebaseAuth.getInstance().signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user_info.setText("Email: ${user.email} / ${user.isEmailVerified}")
            verify_email.visibility = if (user.isEmailVerified) View.GONE else View.VISIBLE
        } else {
            user_info.setText("Not Login")
            verify_email.visibility = View.GONE
        }
    }
}

