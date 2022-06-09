package com.ranzn.firebasenotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            val pushToken = it.result
            Log.i("TAG", "pushToken: $pushToken")
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("com.ranzan.broadcast"))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 != null && p1.action != null && p1.action == "com.ranzan.broadcast") {
                val title = p1.getStringExtra("title")
                val body = p1.getStringExtra("body")
                val image = p1.getStringExtra("image")
                Glide.with(imageView).load(image).into(imageView)
                text.text = "$title $body"
            }
        }
    }
}