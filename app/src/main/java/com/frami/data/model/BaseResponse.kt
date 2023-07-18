package com.frami.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


open class BaseResponse {

    @SerializedName("status")
    @Expose
    private var status: Int = 0

    @SerializedName("message")
    @Expose
    private var message: String = ""
//
//    @SerializedName("data")
//    @Expose
//    var data: Any? = null

    open fun isSuccess(): Boolean {
//        return (status != 0 && status != 404 && status != 400 && status != 500)
        return (status == 200 || status == 201 || status == 202 || status == 302)
    }

    open fun getMessage(): String {
        return message
    }

    open fun setMessage(message: String) {
        this.message = message
    }

    open fun getStatus(): Int {
        return status
    }

    open fun setStatus(status: Int) {
        this.status = status
    }
}