package com.example.tp3

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

const val channelId="notification_channel"
const val channelName="notification_name"

class MyFirebaseMessagingService:FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        if(message.notification!=null){
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }
    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title:String,message: String):RemoteViews{
        val remoteView=RemoteViews("com.example.tp3",R.layout.notification)
        remoteView.setTextViewText(R.id.title_notification,title)
        remoteView.setTextViewText(R.id.description_notification,message)
        remoteView.setImageViewResource(R.id.icon_notification,R.drawable.ic_baseline_notifications_24)
        return remoteView
    }
    fun generateNotification(title:String,message:String){
        // channel id, channel name
        var builder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext,
            channelId).setSmallIcon(R.drawable.ic_baseline_notifications_24).setAutoCancel(true).setVibrate(
            longArrayOf(1000,1000,1000,1000)).setOnlyAlertOnce(true)
        builder=builder.setContent(getRemoteView(title,message))
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel=NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(0,builder.build())
        }

    }
}