package com.ranzn.firebasenotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_SOUND
import androidx.core.app.NotificationCompat.DEFAULT_VIBRATE
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
            showNotification(title, body, image)

        } else {
            showNotification(title, body, image)
        }
    }

//    private fun showNotification(title: String?, body: String?, image: String?) {
//        Log.e("TAG", "showNotification: $title $body $image")
//
//    }

    private fun showNotification(title: String?, body: String?, image: String?) {
        Log.e("TAG", "showNotification: $title $body $image")

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_HIGH //Important for heads-up notification
            val channel = NotificationChannel("1", "name", importance)
            channel.description = "description"
            channel.setShowBadge(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentTitle(title)
            .setContentText(body)
            .setDefaults(DEFAULT_SOUND or DEFAULT_VIBRATE) //Important for heads-up notification
            .setPriority(Notification.PRIORITY_MAX) //Important for heads-up notification

        val buildNotification: Notification = mBuilder.build()
        val mNotifyMgr = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify(1, buildNotification)
    }


    /**Check if the application is in foreground or not*/
    private fun isAppOnForeground(): Boolean {
        return ProcessLifecycleOwner.get().lifecycle.currentState
            .isAtLeast(Lifecycle.State.STARTED)
    }
}