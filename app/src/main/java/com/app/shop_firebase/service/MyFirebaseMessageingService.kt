package com.app.shop_firebase.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessageingService : FirebaseMessagingService(){

    override fun onNewToken(token: String) {
        Log.d("jimmy","onNewToken: $token")

    }
}