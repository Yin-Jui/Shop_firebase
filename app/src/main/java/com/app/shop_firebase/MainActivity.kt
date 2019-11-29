package com.app.shop_firebase

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.shop_firebase.DATA.ItemRepository
import com.app.shop_firebase.MODULE.Category
import com.app.shop_firebase.MODULE.Item
import com.app.shop_firebase.VIEW.ItemViewModel
import com.app.shop_firebase.VIEW.itemHolder
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.concurrent.schedule
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {


    private val RC_SIGNIN = 10000
    // private lateinit var adapter: FirestoreRecyclerAdapter<Item, itemHolder>
    var categories = mutableListOf<Category>()
    lateinit var adapter: ItemAdapter
    lateinit var itemViewModel: ItemViewModel

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

        //Classify

        FirebaseFirestore.getInstance().collection("categories")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        //不是null的時候才處理
                        categories.add(Category("", "All"))
                        for (doc in it) {
                            categories.add(
                                (Category(
                                    doc.id,
                                    doc.data.get("name").toString()
                                ))
                            )
                        }
                        spinner.adapter = ArrayAdapter<Category>(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item,
                            categories
                        )
                            .apply {
                                setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                            }
                        spinner.setSelection(0, false)
                        spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    position: Int,
                                    p3: Long
                                ) {
                                    //  setupAdapter()
                                    itemViewModel.setCategory(categories.get(position).id)
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {

                                }

                            }
                        /*   adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                           spinner.adapter = adapter  */
                    }
                }
            }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //setUpRecyclerView
        recycler.setHasFixedSize(true)  // recycler is the id of the recycler view in MainActivity
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter((mutableListOf<Item>()))
        recycler.adapter = adapter
        itemViewModel = ViewModelProviders.of(this)    //使用之前要先implement(build.gradle), 利用viewmodel簡化程式碼
            .get(ItemViewModel::class.java)
        itemViewModel.getItems().observe(this, androidx.lifecycle.Observer {
            //觀察裡面值的變化,要不要通知取決於mainactivity的生命週期

            Log.d("Jimmy", "observe:  ${it.size}")
            adapter.items = it
            adapter.notifyDataSetChanged()

        })
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
            R.id.favorite -> true
            R.id.action_signin -> {
                /*   startActivityForResult(Intent(this, SignIn::class.java), RC_SIGNIN)
                   */
                val whiteList = listOf<String>("tw", "hk", "cn", "au","us")
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                            Arrays.asList(
                                AuthUI.IdpConfig.EmailBuilder().build(),
                                AuthUI.IdpConfig.FacebookBuilder().build(),
                                AuthUI.IdpConfig.PhoneBuilder()
                                    .setWhitelistedCountries(whiteList)
                                    .setDefaultCountryIso("us")
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
        // adapter.startListening()
        //network signal
        val itemrepository = ItemRepository(application)

        Timer("Checking connection", false).schedule(10, 3000) {

            doAsync {
                var connection: Boolean
                val cm =
                    application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                connection = activeNetwork?.isConnectedOrConnecting == true
                uiThread {
                    if (connection) {
                        signal.setImageResource(R.drawable.lightbulbgreen)
                    } else {
                        signal.setImageResource(R.drawable.lightbulbred)
                    }
                }
            }
        }
    }

        override fun onStop() {
            super.onStop()
            FirebaseAuth.getInstance().removeAuthStateListener(this)
            //   adapter.stopListening()
        }

        @SuppressLint("SetTextI18n")
        override fun onAuthStateChanged(auth: FirebaseAuth) {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                user_info.setText("Email: ${user.email} / ${user.isEmailVerified}")
                user_info.visibility = GONE
                FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {task->
                    if(task.isSuccessful){
                        Log.d("jimmy"," FCM TOKEN ${task.result?.token}")
                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(user.uid)
                            .set(mapOf("token" to task.result?.token))
                    }
                }

                //        verify_email.visibility = if (user.isEmailVerified) GONE else View.VISIBLE
            } else {
                user_info.text = "Not Login"
                verify_email.visibility = GONE
            }
        }

        inner class ItemAdapter(var items: List<Item>) : RecyclerView.Adapter<itemHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemHolder {
                return itemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_row, parent, false)
                )
            }

            override fun getItemCount(): Int {
                return items.size
            }

            override fun onBindViewHolder(holder: itemHolder, position: Int) {

                holder.bindTo(items.get(position))
                holder.itemView.setOnClickListener {
                    itemClick(items.get(position), position)
                }
            }

        }
    }

