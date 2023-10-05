package com.android.kvc.xiao.v2.miui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.ComponentName
import android.Manifest
import android.os.Build
import android.os.IBinder
import android.os.Handler
import android.net.TrafficStats
import android.os.Looper
import android.widget.RemoteViews
import android.widget.TextView
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import rikka.shizuku.ShizukuProvider
import rikka.shizuku.Shizuku

class MtService : Service() {

    private val NOTIFICATION_ID = 2
    private lateinit var notificationManager: NotificationManager
    private lateinit var handler: Handler
    private lateinit var updateRunnable: Runnable
    private var lastTotalRxBytes: Long = 0
    private var lastTotalTxBytes: Long = 0

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        handler = Handler(Looper.getMainLooper())
        updateRunnable = Runnable { updateNetworkSpeeds() }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        // 开始定时任务，每隔一秒更新一次网络速度
        handler.post(updateRunnable)
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val channelId = "androidx"
        val channelName = "正在运行的状态"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(notificationChannel)

        val customView = RemoteViews(packageName, R.layout.layout_mt)
        customView.setTextViewText(R.id.viTextView, "上传速度：0 KB/s")
        customView.setTextViewText(R.id.vvTextView, "下载速度：0 KB/s")

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContent(customView)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOngoing(true)

        return builder.build()
    }

    private fun updateNetworkSpeeds() {
        val totalRxBytes = TrafficStats.getTotalRxBytes()
        val totalTxBytes = TrafficStats.getTotalTxBytes()

        val rxSpeedBytes = totalRxBytes - lastTotalRxBytes
        val txSpeedBytes = totalTxBytes - lastTotalTxBytes

        val uploadSpeedText = formatSpeed(txSpeedBytes)
        val downloadSpeedText = formatSpeed(rxSpeedBytes)

        val customView = RemoteViews(packageName, R.layout.layout_mt)
        customView.setTextViewText(R.id.viTextView, "上传速度：$uploadSpeedText")
        customView.setTextViewText(R.id.vvTextView, "下载速度：$downloadSpeedText")

        val notification = createNotification().apply {
            contentView = customView
        }
        notificationManager.notify(NOTIFICATION_ID, notification)

        lastTotalRxBytes = totalRxBytes
        lastTotalTxBytes = totalTxBytes

        handler.postDelayed(updateRunnable, 1000)
    }

    private fun formatSpeed(speedBytes: Long): String {
        val kiloByte = 1024L
        val megaByte = kiloByte * 1024L
        val gigaByte = megaByte * 1024L

        return when {
            speedBytes >= gigaByte -> {
                val speed = speedBytes.toDouble() / gigaByte.toDouble()
                String.format("%.2f GB/s", speed)
            }
            speedBytes >= megaByte -> {
                val speed = speedBytes.toDouble() / megaByte.toDouble()
                String.format("%.2f MB/s", speed)
            }
            speedBytes >= kiloByte -> {
                val speed = speedBytes.toDouble() / kiloByte.toDouble()
                String.format("%.2f KB/s", speed)
            }
            else -> {
                "$speedBytes B/s"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}