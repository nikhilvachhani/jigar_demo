package com.frami.data.model.home

import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MessageData(
    @field:SerializedName("id")
    var id: Int = 1,
    @field:SerializedName("sender_name")
    var senderName: String = "Ronald Richards",
    @field:SerializedName("message")
    var message: String = "So, what's your activity plan this weekend?",
    @field:SerializedName("message_time")
    var messageTime: String = "5 minutes ago",
    @field:SerializedName("message_time")
    var isSender: Boolean = false,
) : Serializable