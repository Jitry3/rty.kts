package com.android.kvc.xiao.v2.miui

import rikka.shizuku.Shizuku

import android.Manifest
import android.graphics.Color
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import android.os.IBinder
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.RemoteException

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.ComponentName
import android.content.ServiceConnection

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import android.app.Service
import android.app.PendingIntent
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager

import android.net.Uri
import android.net.ConnectivityManager

import android.provider.Settings
import java.text.DecimalFormat
import kotlin.random.Random

/**
 * @Author 小猫猫
 * @Date 2023/09/17 22:54
 */
class NotificationService : Service() {

    private val NOTIFICATION_ID = 1
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        
        // 启动服务
        val targetServiceIntent = Intent(this, MtService::class.java)
        startService(targetServiceIntent)
        
        return START_STICKY
    }

    private fun createNotification(): Notification {
        // 创建通知渠道
        val channelId = "android"
        val channelName = "Android System Notification"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, channelName, importance)

        // 注册通知渠道
        notificationManager.createNotificationChannel(notificationChannel)

        // 创建 RemoteViews 对象，并加载自定义布局
        val customView = RemoteViews(packageName, R.layout.custom_notification_layout)
        

         // 设置自定义布局中的文本内容
        val titleText = "运行状态：正在运行"
         customView.setTextViewText(R.id.titleTextView, titleText)

        // 设置自定义布局中的应用图标
    val iconResource = R.drawable.ic_settings
    customView.setImageViewResource(R.id.appIconImageView, iconResource)
         
         
         // 获取 EditText 和 Button 的实例
         customView.setTextViewText(R.id.tuButton, "涩涩")
         customView.setTextViewText(R.id.tyButton, "返回")
         customView.setTextViewText(R.id.tiButton, "退出")
         
        // 创建通知构建器，并应用自定义布局和图标
    val builder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(iconResource) // 通知的小图标
        .setContent(customView)
        .setContentTitle(titleText) // 通知标题
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setColor(getResources().getColor(R.color.notification_color))
        .setAutoCancel(true) // 设置为可取消
        .setOngoing(true) // 设置为正在进行中的状态

        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}