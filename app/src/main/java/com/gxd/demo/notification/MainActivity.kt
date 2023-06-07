package com.gxd.demo.notification

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process

class MainActivity : Activity() {
    companion object {
        const val REQUEST_CODE = 3256
        const val BADGE_COUNT = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkPermission(
                Manifest.permission.POST_NOTIFICATIONS,
                Process.myPid(),
                Process.myUid()
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE)
        } else {
            // miui桌面角标在应用后台发送通知时才会显示
            Handler(Looper.getMainLooper()).postDelayed({
                showNotification()
            }, 5000)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) return super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        showNotification()
    }

    private fun showNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return
        val channelId = "123"
        val channelName = "运营通知"
        val channel = NotificationChannel(
            channelId, channelName, NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "渠道描述"
            setShowBadge(true)// 是否在桌面显示角标
        }
        notificationManager.createNotificationChannel(channel)
        val intent = Intent(this@MainActivity, NotificationActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this@MainActivity, REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE)
        val notificationAction = Notification.Action.Builder(
            Icon.createWithResource(this@MainActivity, android.R.mipmap.sym_def_app_icon), "去看看", pendingIntent
        ).build()
        val notification = Notification.Builder(this@MainActivity, channelId)
            .setContentTitle("通知标题")
            .setContentText("通知内容")
            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
            .setLargeIcon(BitmapFactory.decodeResource(resources, android.R.mipmap.sym_def_app_icon))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)// 点击后自动消失
            .addAction(notificationAction)
            .setNumber(BADGE_COUNT)
            .build()
        val notificationId = 123
        notificationManager.notify(notificationId, notification)
    }
}