package com.app.shop_firebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {


    private val RC_SIGNIN = 100
    private lateinit var adapter: FirestoreRecyclerAdapter<Item, itemHolder>
    var categories = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        verify_email.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                ?.addOnCompleteListener { task ->
                    //not sure if the email is created successfully
                    if (task.isSuccessful) {
                        Snackbar.make(it, "verify email sent", Snackbar.LENGTH_LONG).show()

                    }
                }
        }
        FirebaseFirestore.getInstance().collection("category")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        for (doc in it) {
                            categories.add((Category(doc.id, doc.data.get("name").toString())))
                        }
                        var adapter = ArrayAdapter<Category>(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item,
                            categories
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                        spinner.adapter = adapter
                    }
                }
            }



        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        //setUpRecyclerView
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        val query = FirebaseFirestore.getInstance()
            .collection("items2")
            .orderBy("viewCount", Query.Direction.DESCENDING)
            .limit(10)

        val options = FirestoreRecyclerOptions.Builder<Item>()  //選項設計
            .setQuery(query, Item::class.java)
            .build()
        adapter = object : FirestoreRecyclerAdapter<Item, itemHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_row, parent, false)
                return itemHolder(view)
            }

            override fun onBindViewHolder(holder: itemHolder, position: Int, item: Item) {
                item.id = snapshots.getSnapshot(position).id
                holder.bindTo(item)
                holder.itemView.setOnClickListener {
                    itemClick(item, position)
                }
            }
        }
        recycler.adapter = adapter

    }

    private fun itemClick(item: Item, position: Int) {
        Log.d("Main", "itemclicked:  ${item.title}   position: ${position}")
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("ITEM", item)
        startActivity(intent)
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
                /*   startActivityForResult(Intent(this, SignIn::class.java), RC_SIGNIN)
                   */
                val whiteList = listOf<String>("tw", "hk", "cn", "au")
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                            Arrays.asList(
                                AuthUI.IdpConfig.EmailBuilder().build(),
                                AuthUI.IdpConfig.GoogleBuilder().build(),
                                AuthUI.IdpConfig.FacebookBuilder().build(),
                                AuthUI.IdpConfig.PhoneBuilder()
                                    .setWhitelistedCountries(whiteList)
                                    .setDefaultCountryIso("tw")
                                    .build()
                            )
                        )
                        .setIsSmartLockEnabled(false)
                        .setTheme(R.style.SignUp)
                        .setLogo(R.drawable.iconfinder_cool_emoticon_emoticons_emoji_emote_2993651)
                        .build(), RC_SIGNIN
                )
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
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
        adapter.stopListening()
    }

    @SuppressLint("SetTextI18n")
    override fun onAuthStateChanged(auth: FirebaseAuth) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user_info.setText("Email: ${user.email} / ${user.isEmailVerified}")
            user_info.visibility = GONE

            //        verify_email.visibility = if (user.isEmailVerified) GONE else View.VISIBLE
        } else {
            user_info.text = "Not Login"
            verify_email.visibility = GONE
        }
    }
}

