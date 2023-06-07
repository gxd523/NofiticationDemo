package com.gxd.demo.notification

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class NotificationActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TextView(this).apply {
            setBackgroundColor(0xff00ff)
            text = "Notification"
            textSize = 60f
        }.let(::setContentView)
    }
}