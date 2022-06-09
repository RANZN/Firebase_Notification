package com.ranzn.firebasenotification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyMessagingHelper : FirebaseMessagingService() {
    private val channelId = "com.hm.mmmhmm.notification"
    private val channelName = "com.hm.mmmhmm.helper"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val type = message.data["type"]
        val likeCount = message.data["likeCount"]
        Log.d("ranzn", "onMessageReceived: $type $likeCount")
//            val image = message.data["image"]
//            val decodedString: ByteArray = Base64.decode(image, Base64.DEFAULT)
//            val decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//            showNotification(title, body, decodedImage)
        showNotification(type, likeCount)
    }


    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(channelId, channelName, importance)
//            channel.description = channelName
//            channel.setShowBadge(true)
//            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//            val notificationManager = getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//        }
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setLargeIcon(image)
            .setContentTitle(title)
            .setContentText(body)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)

        val buildNotification: Notification = mBuilder.build()
        val mNotifyMgr = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify((0..10000).random(), buildNotification)
    }

}