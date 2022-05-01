package com.ranzn.firebasenotification

import android.content.Intent
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("TAG", "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.data["title"]
        val body = message.data["body"]
        val image = message.data["image"]

        if (isAppOnForeground()) {
            Log.i("TAG", "onMessageReceived: $title $body $image")
            val intent = Intent("com.ranzan.broadcast")
            intent.putExtra("title", title)
            intent.putExtra("body", body)
            intent.putExtra("image", image)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
        } else {
            showNotification(title,body,image)
        }
    }

    private fun showNotification(title: String?, body: String?, image: String?) {
        Log.e("TAG", "showNotification: $title $body $image")

    }

    /**Check if the application is in foreground or not*/
    private fun isAppOnForeground(): Boolean {
        return ProcessLifecycleOwner.get().lifecycle.currentState
            .isAtLeast(Lifecycle.State.STARTED)
    }
}