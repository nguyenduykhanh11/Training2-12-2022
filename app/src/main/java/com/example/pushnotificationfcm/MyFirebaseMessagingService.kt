package com.example.pushnotificationfcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANNEL_ID = "CHANNEL_ID"
const val CHANNEL_NAME = "CHANNEL_NAME"
class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("this", "notifi: "+message.notification )
            if (message.notification != null){
                Log.d("this", this.toString())
                generateNotification(message.notification!!.title!!, message.notification!!.body!!)
            }
        super.onMessageReceived(message)
    }

    fun generateNotification(title: String, message: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        builder = builder.setContent(getRemoView(title, message))
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoView(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews("com.example.pushnotificationfcm", R.layout.custom_notification)
        remoteViews.setTextViewText(R.id.tv_titel, title)
        remoteViews.setTextViewText(R.id.tv_message, message)
        remoteViews.setImageViewResource(R.id.img_logo, com.google.android.material.R.drawable.abc_ic_arrow_drop_right_black_24dp)
        return remoteViews
    }
}